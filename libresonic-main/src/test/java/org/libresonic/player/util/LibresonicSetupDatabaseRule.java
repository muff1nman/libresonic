package org.libresonic.player.util;

import org.junit.rules.ExternalResource;
import org.libresonic.player.TestCaseUtils;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.util.Arrays;

public class LibresonicSetupDatabaseRule extends ExternalResource {

    private static final Logger logger = LoggerFactory.getLogger(LibresonicSetupDatabaseRule.class);

    private final String url;

    public LibresonicSetupDatabaseRule(String url) {
        this.url = url;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        logger.info("Setting up database from archive {}", url);
        String destinationS = TestCaseUtils.libresonicHomePathForTest();
        File destination = new File(destinationS);
        File source = new File(new URI(url));
        Archiver archiver = ArchiverFactory.createArchiver(source);
        archiver.extract(source, destination);
        Arrays.stream(destination.listFiles()).forEach(file -> logger.info("Listing file {}", file));
    }
}
