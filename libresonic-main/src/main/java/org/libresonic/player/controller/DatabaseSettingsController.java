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

import org.libresonic.player.command.DatabaseSettingsCommand;
import org.libresonic.player.command.MusicFolderSettingsCommand;
import org.libresonic.player.dao.AlbumDao;
import org.libresonic.player.dao.ArtistDao;
import org.libresonic.player.dao.MediaFileDao;
import org.libresonic.player.domain.MusicFolder;
import org.libresonic.player.service.MediaScannerService;
import org.libresonic.player.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/databaseSettings")
public class DatabaseSettingsController {

    @Autowired
    private SettingsService settingsService;
    @Autowired
    private MediaScannerService mediaScannerService;
    @Autowired
    private ArtistDao artistDao;
    @Autowired
    private AlbumDao albumDao;
    @Autowired
    private MediaFileDao mediaFileDao;

    @RequestMapping(method = RequestMethod.GET)
    protected String displayForm() throws Exception {
        return "databaseSettings";
    }

    @ModelAttribute
    protected void formBackingObject(Model model) throws Exception {
        DatabaseSettingsCommand command = new DatabaseSettingsCommand();
        // TODO:AD
        model.addAttribute("command", command);
    }

    @RequestMapping(method = RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("command") DatabaseSettingsCommand command, RedirectAttributes redirectAttributes) throws Exception {
        // TODO:AD
//        settingsService.setOrganizeByFolderStructure(command.isOrganizeByFolderStructure());
        settingsService.save();

        redirectAttributes.addFlashAttribute("settings_toast", true);
        redirectAttributes.addFlashAttribute("settings_reload", true);

        mediaScannerService.schedule();
        return "redirect:databaseSettings.view";
    }

}