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
package org.libresonic.player.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.libresonic.player.domain.Playlist;
import org.libresonic.player.service.PlaylistService;
import org.libresonic.player.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sindre Mehus
 */
@Controller
@RequestMapping("/importPlaylist")
public class ImportPlaylistController {

    private static final long MAX_PLAYLIST_SIZE_MB = 5L;

    @Autowired
    private SecurityService securityService;
    @Autowired
    private PlaylistService playlistService;

    @RequestMapping(method = RequestMethod.POST )
    protected String handlePost(@RequestParam("file") MultipartFile item, MultipartHttpServletRequest
            request, RedirectAttributes redirectAttributes) throws Exception {
        Map<String, Object> map = new HashMap<>();

        try {
            if (item.getSize() > MAX_PLAYLIST_SIZE_MB * 1024L * 1024L) {
                throw new Exception("The playlist file is too large. Max file size is " + MAX_PLAYLIST_SIZE_MB + " MB.");
            }
            String playlistName = FilenameUtils.getBaseName(item.getOriginalFilename());
            String fileName = FilenameUtils.getName(item.getOriginalFilename());
            String format = StringUtils.lowerCase(FilenameUtils.getExtension(item.getOriginalFilename()));
            String username = securityService.getCurrentUsername(request);
            Playlist playlist = playlistService.importPlaylist(username, playlistName, fileName, format, item.getInputStream(), null);
            map.put("playlist", playlist);
        } catch (Exception e) {
            map.put("error", e.getMessage());
        }

        redirectAttributes.addFlashAttribute("model", map);
        return "redirect:importPlaylist";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String handleGet() {
        return "importPlaylist";
    }


}
