/*
 This file is part of Libresonic.

 Libresonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Libresonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Libresonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2016 (C) Libresonic Authors
 Based upon Subsonic, Copyright 2009 (C) Sindre Mehus
 */
package org.libresonic.player.util;

import org.libresonic.player.Logger;
import org.libresonic.player.service.SettingsService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

/**
 * Miscellaneous general utility methods.
 *
 * @author Sindre Mehus
 */
public final class Util {

    private static final Logger LOG = Logger.getLogger(Util.class);

    /**
     * Disallow external instantiation.
     */
    private Util() {
    }

    public static String getDefaultMusicFolder() {
        String def = isWindows() ? "c:\\music" : "/var/music";
        return System.getProperty("libresonic.defaultMusicFolder", def);
    }

    public static String getDefaultPodcastFolder() {
        String def = isWindows() ? "c:\\music\\Podcast" : "/var/music/Podcast";
        return System.getProperty("libresonic.defaultPodcastFolder", def);
    }

    public static String getDefaultPlaylistFolder() {
        String def = isWindows() ? "c:\\playlists" : "/var/playlists";
        return System.getProperty("libresonic.defaultPlaylistFolder", def);
    }

    public static boolean isWindows() {
        return System.getProperty("os.name", "Windows").toLowerCase().startsWith("windows");
    }

    /**
     * Similar to {@link ServletResponse#setContentLength(int)}, but this
     * method supports lengths bigger than 2GB.
     * <p/>
     * See http://blogger.ziesemer.com/2008/03/suns-version-of-640k-2gb.html
     *
     * @param response The HTTP response.
     * @param length   The content length.
     */
    public static void setContentLength(HttpServletResponse response, long length) {
        if (length <= Integer.MAX_VALUE) {
            response.setContentLength((int) length);
        } else {
            response.setHeader("Content-Length", String.valueOf(length));
        }
    }

    public static <T> List<T> subList(List<T> list, long offset, long max) {
        return list.subList((int) offset, Math.min(list.size(), (int) (offset + max)));
    }

    public static List<Integer> toIntegerList(int[] values) {
        if (values == null) {
            return Collections.emptyList();
        }
        List<Integer> result = new ArrayList<Integer>(values.length);
        for (int value : values) {
            result.add(value);
        }
        return result;
    }

    public static int[] toIntArray(List<Integer> values) {
        if (values == null) {
            return new int[0];
        }
        int[] result = new int[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.get(i);
        }
        return result;
    }
}