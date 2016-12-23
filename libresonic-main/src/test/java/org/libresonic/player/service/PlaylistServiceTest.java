package org.libresonic.player.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.libresonic.player.dao.MediaFileDao;
import org.libresonic.player.dao.PlaylistDao;
import org.libresonic.player.domain.MediaFile;
import org.libresonic.player.domain.Playlist;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlaylistServiceTest {

    @InjectMocks
    PlaylistService playlistService;

    @Mock
    MediaFileDao mediaFileDao;

    @Mock
    PlaylistDao playlistDao;

    @Mock
    MediaFileService mediaFileService;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Captor
    ArgumentCaptor<Playlist> actual;

    @Captor
    ArgumentCaptor<List<MediaFile>> medias;

    @Test
    public void testExportToM3U() throws Exception {

        when(mediaFileDao.getFilesInPlaylist(eq(23))).thenReturn(getPlaylistFiles());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        playlistService.exportPlaylist(23, outputStream);
        String actual = outputStream.toString();
        Assert.assertEquals(IOUtils.toString(getClass().getResourceAsStream("/PLAYLISTS/23.m3u")), actual);
    }

    private List<MediaFile> getPlaylistFiles() {
        List<MediaFile> mediaFiles = new ArrayList<>();

        MediaFile mf1 = new MediaFile();
        mf1.setId(142);
        mf1.setPath("/some/path/to_album/to_artist/name - of - song.mp3");
        mf1.setPresent(true);
        mediaFiles.add(mf1);

        MediaFile mf2 = new MediaFile();
        mf2.setId(1235);
        mf2.setPath("/some/path/to_album2/to_artist/another song.mp3");
        mf2.setPresent(true);
        mediaFiles.add(mf2);

        MediaFile mf3 = new MediaFile();
        mf3.setId(198403);
        mf3.setPath("/some/path/to_album2/to_artist/another song2.mp3");
        mf3.setPresent(false);
        mediaFiles.add(mf3);

        return mediaFiles;
    }

    @Test
    public void testImportFromM3U() throws Exception {
        String username = "testUser";
        String playlistName = "test-playlist";
        StringBuilder builder = new StringBuilder();
        builder.append("#EXTM3U\n");
        File mf1 = folder.newFile();
        FileUtils.touch(mf1);
        File mf2 = folder.newFile();
        FileUtils.touch(mf2);
        File mf3 = folder.newFile();
        FileUtils.touch(mf3);
        builder.append(mf1.getAbsolutePath() + "\n");
        builder.append(mf2.getAbsolutePath() + "\n");
        builder.append(mf3.getAbsolutePath() + "\n");
        doAnswer(new PersistPlayList(23)).when(playlistDao).createPlaylist(any());
        doAnswer(new MediaFileHasEverything()).when(mediaFileService).getMediaFile(any(File.class));
        InputStream inputStream = new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));
        String path = "/path/to/"+playlistName+".m3u";
        playlistService.importPlaylist(username, playlistName, path, "m3u", inputStream, null);
        verify(playlistDao).createPlaylist(actual.capture());
        verify(playlistDao).setFilesInPlaylist(eq(23), medias.capture());
        Playlist expected = new Playlist();
        expected.setUsername(username);
        expected.setName(playlistName);
        expected.setComment("Auto-imported from " + path);
        expected.setImportedFrom(path);
        expected.setShared(true);
        expected.setId(23);
        assertTrue("\n" + ToStringBuilder.reflectionToString(actual.getValue()) + "\n\n did not equal \n\n" + ToStringBuilder.reflectionToString(expected), EqualsBuilder.reflectionEquals(actual.getValue(), expected, "created", "changed"));
        System.out.println(medias.getValue());
    }

    private class PersistPlayList implements Answer {
        private final int id;
        public PersistPlayList(int id) {
            this.id = id;
        }

        @Override
        public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
            Playlist playlist = invocationOnMock.getArgument(0);
            playlist.setId(id);
            return null;
        }
    }

    private class MediaFileHasEverything implements Answer {

        @Override
        public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
            File file = invocationOnMock.getArgument(0);
            MediaFile mediaFile = new MediaFile();
            mediaFile.setPath(file.getPath());
            return mediaFile;
        }
    }
}
