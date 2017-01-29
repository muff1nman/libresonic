package org.libresonic.player.security;

import org.libresonic.player.Logger;
import org.libresonic.player.domain.User;
import org.libresonic.player.service.SecurityService;
import org.libresonic.player.service.SettingsService;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.LdapAuthenticator;

public class LibresonicLdapAuthenticatorWrapper implements LdapAuthenticator {

    private static final Logger LOG = Logger.getLogger(LibresonicLdapAuthenticatorWrapper.class);

    private final LdapAuthenticator parent;
    private final SettingsService settingsService;
    private final SecurityService securityService;

    public LibresonicLdapAuthenticatorWrapper(LdapAuthenticator parent,
                                              SettingsService settingsService,
                                              SecurityService securityService) {
        this.parent = parent;
        this.settingsService = settingsService;
        this.securityService = securityService;
    }

    @Override
    public DirContextOperations authenticate(Authentication authentication) {
        preLdapAuth(authentication);
        DirContextOperations result = parent.authenticate(authentication);
        return postLdapAuth(authentication, result);
    }

    DirContextOperations postLdapAuth(Authentication authentication, DirContextOperations result) {
        String username = authentication.getName();
        authentication.getPrincipal();
        LOG.info("User '" + username + "' successfully authenticated in LDAP. DN: " + result.getDn());
        User user = securityService.getUserByName(username);
        if (user == null) {
            User newUser = new User(username, "", null, true, 0L, 0L, 0L);
            newUser.setStreamRole(true);
            newUser.setSettingsRole(true);
            securityService.createUser(newUser);
            LOG.info("Created local user '" + username + "' for DN " + result.getDn());
        }
        return result;
    }

    void preLdapAuth(Authentication authentication) {
        // LDAP authentication must be enabled on the system.
        if (!settingsService.isLdapEnabled()) {
            throw new BadCredentialsException("LDAP authentication disabled.");
        }

        // User must be defined in Libresonic, unless auto-shadowing is enabled.
        User user = securityService.getUserByName(authentication.getName());
        if (user == null && !settingsService.isLdapAutoShadowing()) {
            throw new BadCredentialsException("User does not exist.");
        }

        // LDAP authentication must be enabled for the given user.
        if (user != null && !user.isLdapAuthenticated()) {
            throw new BadCredentialsException("LDAP authentication disabled for user.");
        }
    }
}
