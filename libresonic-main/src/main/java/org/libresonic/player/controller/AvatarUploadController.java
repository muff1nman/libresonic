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

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.libresonic.player.Logger;
import org.libresonic.player.domain.Avatar;
import org.libresonic.player.service.SecurityService;
import org.libresonic.player.service.SettingsService;
import org.libresonic.player.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller which receives uploaded avatar images.
 *
 * @author Sindre Mehus
 */
@Controller
@RequestMapping("/avatarUpload")
public class AvatarUploadController  {

    private static final Logger LOG = Logger.getLogger(AvatarUploadController.class);
    private static final int MAX_AVATAR_SIZE = 64;

    @Autowired
    private SettingsService settingsService;
    @Autowired
    private SecurityService securityService;

    @RequestMapping(method = { RequestMethod.GET })
    public String handleResult() {
        return "avatarUploadResult";
    }

    @RequestMapping(method = { RequestMethod.POST })
    protected String handleRequestInternal(
            RedirectAttributes redirectAttributes,
            MultipartHttpServletRequest request,
            @RequestParam("file") MultipartFile file) throws Exception {

        String username = securityService.getCurrentUsername(request);

        // Check that we have a file upload request.
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new Exception("Illegal request.");
        }

        Map<String, Object> map = new HashMap<>();

        createAvatar(file.getOriginalFilename(), file.getBytes(), username, map);

        map.put("username", username);
        map.put("avatar", settingsService.getCustomAvatar(username));
        redirectAttributes.addFlashAttribute("model", map);
        return "redirect:avatarUpload";
    }

    private void createAvatar(String fileName, byte[] data, String username, Map<String, Object> map) throws IOException {

        BufferedImage image;
        try {
            image = ImageIO.read(new ByteArrayInputStream(data));
            if (image == null) {
                throw new Exception("Failed to decode incoming image: " + fileName + " (" + data.length + " bytes).");
            }
            int width = image.getWidth();
            int height = image.getHeight();
            String mimeType = StringUtil.getMimeType(FilenameUtils.getExtension(fileName));

            // Scale down image if necessary.
            if (width > MAX_AVATAR_SIZE || height > MAX_AVATAR_SIZE) {
                double scaleFactor = (double) MAX_AVATAR_SIZE / (double) Math.max(width, height);
                height = (int) (height * scaleFactor);
                width = (int) (width * scaleFactor);
                image = CoverArtController.scale(image, width, height);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", out);
                data = out.toByteArray();
                mimeType = StringUtil.getMimeType("jpeg");
                map.put("resized", true);
            }
            Avatar avatar = new Avatar(0, fileName, new Date(), mimeType, width, height, data);
            settingsService.setCustomAvatar(avatar, username);
            LOG.info("Created avatar '" + fileName + "' (" + data.length + " bytes) for user " + username);

        } catch (Exception x) {
            LOG.warn("Failed to upload personal image: " + x, x);
            map.put("error", x);
        }
    }


}
