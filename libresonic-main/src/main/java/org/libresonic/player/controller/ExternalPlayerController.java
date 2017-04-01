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

import org.apache.commons.lang3.StringUtils;
import org.libresonic.player.domain.*;
import org.libresonic.player.security.JWTSecurityUtil;
import org.libresonic.player.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the page used to play shared music (Twitter, Facebook etc).
 *
 * @author Sindre Mehus
 */
@Controller
@RequestMapping(value = {"/ext/share/**"})
public class ExternalPlayerController {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalPlayerController.class);

    @Autowired
    private SettingsService settingsService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ShareService shareService;
    @Autowired
    private MediaFileService mediaFileService;

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> map = new HashMap<>();

        String shareName = ControllerUtils.extractMatched(request);
        LOG.debug("Share name is {}", shareName);

        if(StringUtils.isBlank(shareName)) {
            LOG.warn("Could not find share with shareName " + shareName);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        Share share = shareService.getShareByName(shareName);

        if (share != null && share.getExpires() != null && share.getExpires().before(new Date())) {
            LOG.warn("Share " + shareName + " is expired");
            share = null;
        }

        if (share != null) {
            share.setLastVisited(new Date());
            share.setVisitCount(share.getVisitCount() + 1);
            shareService.updateShare(share);
        }

        Player player = playerService.getGuestPlayer(request);

        map.put("share", share);
        map.put("songs", getSongs(request, share, player));

        return new ModelAndView("externalPlayer", "model", map);
    }

    private List<MediaFileWithUrlInfo> getSongs(HttpServletRequest request, Share share, Player player) throws IOException {
        List<MediaFileWithUrlInfo> result = new ArrayList<>();

        List<MusicFolder> musicFolders = settingsService.getMusicFoldersForUser(player.getUsername());

        if (share != null) {
            for (MediaFile file : shareService.getSharedFiles(share.getId(), musicFolders)) {
                if (file.getFile().exists()) {
                    if (file.isDirectory()) {
                        List<MediaFile> childrenOf = mediaFileService.getChildrenOf(file, true, false, true);
                        result.addAll(childrenOf.stream().map(mf -> addUrlInfo(request, player, mf)).collect(Collectors.toList()));
                    } else {
                        result.add(addUrlInfo(request, player, file));
                    }
                }
            }
        }
        if(result.size() > 5) {
            return result.subList(0, 4);
        }
        return result;
    }


    private UriComponentsBuilder addJWTToken(UriComponentsBuilder builder) {
        String token = JWTSecurityUtil.createToken(settingsService.getJWTKey(), builder.toUriString());
        builder.queryParam(JWTSecurityUtil.JWT_PARAM_NAME, token);
        return builder;
    }

    public MediaFileWithUrlInfo addUrlInfo(HttpServletRequest request, Player player, MediaFile mediaFile) {
        String prefix = "/ext";
        String streamUrl = addJWTToken(
                UriComponentsBuilder
                        .fromHttpUrl(NetworkService.getBaseUrl(request) + prefix + "/stream")
                        .queryParam("id", mediaFile.getId())
                        .queryParam("player", player.getId())
                        .queryParam("maxBitRate", "1200"))
                .build()
                .toUriString();

        String coverArtUrl = addJWTToken(
                UriComponentsBuilder
                        .fromHttpUrl(NetworkService.getBaseUrl(request) + prefix + "/coverArt.view")
                        .queryParam("id", mediaFile.getId())
                        .queryParam("size", "500"))
                .build()
                .toUriString();
        return new MediaFileWithUrlInfo(mediaFile, coverArtUrl, streamUrl);
    }
}