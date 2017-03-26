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
package org.libresonic.player.service;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.DLNADoc;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.support.connectionmanager.ConnectionManagerService;
import org.fourthline.cling.support.model.ProtocolInfos;
import org.fourthline.cling.support.model.dlna.DLNAProfiles;
import org.fourthline.cling.support.model.dlna.DLNAProtocolInfo;
import org.libresonic.player.Logger;
import org.libresonic.player.exception.UpnpDisabledException;
import org.libresonic.player.service.upnp.ApacheUpnpServiceConfiguration;
import org.libresonic.player.service.upnp.FolderBasedContentDirectory;
import org.libresonic.player.service.upnp.MSMediaReceiverRegistrarService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Sindre Mehus
 * @version $Id$
 */
public class UPnPService {

    private static final Logger LOG = Logger.getLogger(UPnPService.class);

    private SettingsService settingsService;
    private UpnpService upnpService;
    private FolderBasedContentDirectory folderBasedContentDirectory;

    public void init() {
        startService();
    }

    public void startService() {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    LOG.info("Starting UPnP service...");
                    createService();
                    LOG.info("Starting UPnP service - Done!");
                } catch (Throwable x) {
                    LOG.error("Failed to start UPnP service: " + x, x);
                }
            }
        };
        new Thread(runnable).start();
    }

    private synchronized void createService() throws Exception {
        upnpService = new UpnpServiceImpl(new ApacheUpnpServiceConfiguration());

        // Asynch search for other devices (most importantly UPnP-enabled routers for port-mapping)
        upnpService.getControlPoint().search();

        // Start DLNA media server?
        setMediaServerEnabled(settingsService.isDlnaEnabled());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("Shutting down UPnP service...");
                upnpService.shutdown();
                System.err.println("Shutting down UPnP service - Done!");
            }
        });
    }

    public void setMediaServerEnabled(boolean enabled) throws UpnpDisabledException {
        if (enabled) {
            UpnpService safeUpnpnService = getSafeUpnpnService();
            try {
                safeUpnpnService.getRegistry().addDevice(createMediaServerDevice());
                LOG.info("Enabling UPnP/DLNA media server");
            } catch (Exception x) {
                LOG.error("Failed to start UPnP/DLNA media server: " + x, x);
            }
        } else {
            upnpService.getRegistry().removeAllLocalDevices();
            LOG.info("Disabling UPnP/DLNA media server");
        }
    }

    private LocalDevice createMediaServerDevice() throws Exception {

        String serverName = settingsService.getDlnaServerName();
        DeviceIdentity identity = new DeviceIdentity(UDN.uniqueSystemIdentifier(serverName));
        DeviceType type = new UDADeviceType("MediaServer", 1);

        // TODO: DLNACaps

        DeviceDetails details = new DeviceDetails(serverName, new ManufacturerDetails(serverName),
                new ModelDetails(serverName),
                new DLNADoc[]{new DLNADoc("DMS", DLNADoc.Version.V1_5)}, null);

        Icon icon = new Icon("image/png", 512, 512, 32, getClass().getResource("logo-512.png"));

        LocalService<FolderBasedContentDirectory> contentDirectoryservice = new AnnotationLocalServiceBinder().read(FolderBasedContentDirectory.class);
        contentDirectoryservice.setManager(new DefaultServiceManager<FolderBasedContentDirectory>(contentDirectoryservice) {

            @Override
            protected FolderBasedContentDirectory createServiceInstance() throws Exception {
                return folderBasedContentDirectory;
            }
        });

        final ProtocolInfos protocols = new ProtocolInfos();
        for (DLNAProfiles dlnaProfile : DLNAProfiles.values()) {
            if (dlnaProfile == DLNAProfiles.NONE) {
                continue;
            }
            try {
                protocols.add(new DLNAProtocolInfo(dlnaProfile));
            } catch (Exception e) {
                // Silently ignored.
            }
        }

        LocalService<ConnectionManagerService> connetionManagerService = new AnnotationLocalServiceBinder().read(ConnectionManagerService.class);
        connetionManagerService.setManager(new DefaultServiceManager<ConnectionManagerService>(connetionManagerService) {
            @Override
            protected ConnectionManagerService createServiceInstance() throws Exception {
                return new ConnectionManagerService(protocols, null);
            }
        });

        // For compatibility with Microsoft
        LocalService<MSMediaReceiverRegistrarService> receiverService = new AnnotationLocalServiceBinder().read(MSMediaReceiverRegistrarService.class);
        receiverService.setManager(new DefaultServiceManager<>(receiverService, MSMediaReceiverRegistrarService.class));

        return new LocalDevice(identity, type, details, new Icon[]{icon}, new LocalService[]{contentDirectoryservice, connetionManagerService, receiverService});
    }

    public List<String> getSonosControllerHosts() throws UpnpDisabledException {
        List<String> result = new ArrayList<>();
        for (Device device : getSafeUpnpnService().getRegistry().getDevices(new DeviceType("schemas-upnp-org", "ZonePlayer"))) {
            if (device instanceof RemoteDevice) {
                URL descriptorURL = ((RemoteDevice) device).getIdentity().getDescriptorURL();
                if (descriptorURL != null) {
                    result.add(descriptorURL.getHost());
                }
            }
        }
        return result;
    }

    private UpnpService getSafeUpnpnService() throws UpnpDisabledException {
        return Optional.ofNullable(getUpnpService()).orElseThrow(UpnpDisabledException::new);
    }

    public UpnpService getUpnpService() {
        return upnpService;
    }

    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public void setFolderBasedContentDirectory(FolderBasedContentDirectory folderBasedContentDirectory) {
        this.folderBasedContentDirectory = folderBasedContentDirectory;
    }
}
