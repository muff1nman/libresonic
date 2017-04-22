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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.libresonic.player.domain.Playlist;
import org.libresonic.player.service.PlaylistService;
import org.libresonic.player.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.libresonic.player.domain.Playlist;
import org.libresonic.player.service.PlaylistService;
import org.libresonic.player.service.SecurityService;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @RequestMapping(method = RequestMethod.GET)
    public String doGet() throws Exception {
        return "importPlaylist";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String doPost(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            if (ServletFileUpload.isMultipartContent(request)) {

                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<?> items = upload.parseRequest(request);
                for (Object o : items) {
                    FileItem item = (FileItem) o;

                    if ("file".equals(item.getFieldName()) && !StringUtils.isBlank(item.getName())) {
                        if (item.getSize() > MAX_PLAYLIST_SIZE_MB * 1024L * 1024L) {
                            throw new Exception("The playlist file is too large. Max file size is " + MAX_PLAYLIST_SIZE_MB + " MB.");
                        }
                        String playlistName = FilenameUtils.getBaseName(item.getName());
                        String fileName = FilenameUtils.getName(item.getName());
                        String format = StringUtils.lowerCase(FilenameUtils.getExtension(item.getName()));
                        String username = securityService.getCurrentUsername(request);
                        Playlist playlist = playlistService.importPlaylist(username, playlistName, fileName,
                                                                           item.getInputStream(), null);
                        map.put("playlist", playlist);
                    }
                }
            }
        } catch (Exception e) {
            map.put("error", e.getMessage());
        }

        redirectAttributes.addFlashAttribute("model", map);
        return "redirect:importPlaylist.view";
    }


}
