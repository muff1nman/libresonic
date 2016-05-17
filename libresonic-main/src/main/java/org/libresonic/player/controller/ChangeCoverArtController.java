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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import org.libresonic.player.domain.MediaFile;
import org.libresonic.player.service.MediaFileService;

/**
 * Controller for changing cover art.
 *
 * @author Sindre Mehus
 */
@Controller
@RequestMapping("changeCoverArt")
public class ChangeCoverArtController {

    private MediaFileService mediaFileService;

    @RequestMapping(method = RequestMethod.GET)
    public String doGet(HttpServletRequest request, ModelMap model) throws Exception {

        int id = ServletRequestUtils.getRequiredIntParameter(request, "id");
        String artist = request.getParameter("artist");
        String album = request.getParameter("album");
        MediaFile dir = mediaFileService.getMediaFile(id);

        if (artist == null) {
            artist = dir.getArtist();
        }
        if (album == null) {
            album = dir.getAlbumName();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("artist", artist);
        map.put("album", album);

        model.put("model", map);

        return "changeCoverArt";
    }

    public void setMediaFileService(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }
}
