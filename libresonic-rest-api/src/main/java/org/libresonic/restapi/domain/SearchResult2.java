

package org.libresonic.restapi.domain;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchResult2", propOrder = {
        "artist",
        "album",
        "song"
})
public class SearchResult2 {

    protected List<Artist> artist;
    protected List<Child> album;
    protected List<Child> song;

    public List<Artist> getArtist() {
        if (artist == null) {
            artist = new ArrayList<Artist>();
        }
        return this.artist;
    }

    public List<Child> getAlbum() {
        if (album == null) {
            album = new ArrayList<Child>();
        }
        return this.album;
    }

    public List<Child> getSong() {
        if (song == null) {
            song = new ArrayList<Child>();
        }
        return this.song;
    }

}
