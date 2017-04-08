package org.libresonic.player.dao;

import org.apache.commons.lang3.StringUtils;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.libresonic.player.domain.Player;
import org.libresonic.player.domain.PlayerTechnology;
import org.libresonic.player.domain.TranscodeScheme;
import org.libresonic.player.util.LibresonicHomeRule;
import org.libresonic.player.util.LibresonicSetupDatabaseRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit test of {@link PlayerDao}.
 *
 * @author Sindre Mehus
 */
@SuppressWarnings("Duplicates")
@ContextConfiguration(locations = {
        "/applicationContext-service.xml",
        "/applicationContext-cache.xml",
        "/applicationContext-testdb.xml",
        "/applicationContext-mockSonos.xml"})
public class BugDaoTestCase {

    @ClassRule
    public static final SpringClassRule classRule = new SpringClassRule() {
        LibresonicHomeRule libresonicRule = new LibresonicHomeRule();
        LibresonicSetupDatabaseRule databaseSetup = new LibresonicSetupDatabaseRule("file:///home/andrew/Downloads/db.tgz");
        @Override
        public Statement apply(Statement base, Description description) {
            Statement newBase = databaseSetup.apply(base, description);
            Statement newBase2 = libresonicRule.apply(newBase, description);
            return super.apply(newBase2, description);
        }
    };

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    GenericDaoHelper genericDaoHelper;


    JdbcTemplate getJdbcTemplate() {
        return genericDaoHelper.getJdbcTemplate();
    }

    @Autowired
    PlayerDao playerDao;

    @Test
    public void testCreatePlayer() {

        Player player = new Player();
        player.setIpAddress("ipaddress");
        player.setUsername("username");
        player.setClientId("android");
        player.setName("android");
        player.setTechnology(PlayerTechnology.EXTERNAL_WITH_PLAYLIST);

        playerDao.createPlayer(player);
        Player newPlayer = playerDao.getAllPlayers().stream()
                .filter(pl -> StringUtils.equals(pl.getName(), "android")).findFirst().get();
        assertPlayerEquals(player, newPlayer);

        Player newPlayer2 = playerDao.getPlayerById(newPlayer.getId());
        assertPlayerEquals(player, newPlayer2);
    }

    private void assertPlayerEquals(Player expected, Player actual) {
        assertEquals("Wrong ID.", expected.getId(), actual.getId());
        assertEquals("Wrong name.", expected.getName(), actual.getName());
        assertEquals("Wrong technology.", expected.getTechnology(), actual.getTechnology());
        assertEquals("Wrong client ID.", expected.getClientId(), actual.getClientId());
        assertEquals("Wrong type.", expected.getType(), actual.getType());
        assertEquals("Wrong username.", expected.getUsername(), actual.getUsername());
        assertEquals("Wrong IP address.", expected.getIpAddress(), actual.getIpAddress());
        assertEquals("Wrong dynamic IP.", expected.isDynamicIp(), actual.isDynamicIp());
        assertEquals("Wrong auto control enabled.", expected.isAutoControlEnabled(), actual.isAutoControlEnabled());
        assertEquals("Wrong last seen.", expected.getLastSeen(), actual.getLastSeen());
        assertEquals("Wrong transcode scheme.", expected.getTranscodeScheme(), actual.getTranscodeScheme());
    }
}