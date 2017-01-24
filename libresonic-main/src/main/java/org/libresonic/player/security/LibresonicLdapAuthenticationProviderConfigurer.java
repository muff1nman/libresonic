package org.libresonic.player.security;

import org.libresonic.player.service.SecurityService;
import org.libresonic.player.service.SettingsService;
import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticator;

public class LibresonicLdapAuthenticationProviderConfigurer<B extends ProviderManagerBuilder<B>> extends LdapAuthenticationProviderConfigurer<B> {
    private final SettingsService settingsService;
    private final SecurityService securityService;

    public LibresonicLdapAuthenticationProviderConfigurer(SettingsService settingsService, SecurityService securityService) {
        this.settingsService = settingsService;
        this.securityService = securityService;
//        this.userDnPatterns(settingsService.getLdapManagerDn());
        this.contextSource()
                .managerDn(settingsService.getLdapManagerDn())
                .managerPassword(settingsService.getLdapManagerPassword())
                .url(settingsService.getLdapUrl());
        this.userSearchFilter(settingsService.getLdapSearchFilter());

    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T postProcess(T object) {
        if(object instanceof AbstractLdapAuthenticator) {
            LibresonicLdapAuthenticatorWrapper libresonicLdapAuthenticator = new LibresonicLdapAuthenticatorWrapper(
                    (AbstractLdapAuthenticator) object,
                    settingsService,
                    securityService);

//            libresonicLdapAuthenticator.setUserSearch(new FilterBasedLdapUserSearch("", settingsService
//                    .getLdapSearchFilter(), libresonicLdapAuthenticator.));

//            if (userSearchFilter == null) {
//                return null;
//            }
//            return new FilterBasedLdapUserSearch(userSearchBase, userSearchFilter,
//                    contextSource);
            return super.postProcess((T) libresonicLdapAuthenticator);
        } else {
            return super.postProcess(object);
        }
    }
}
