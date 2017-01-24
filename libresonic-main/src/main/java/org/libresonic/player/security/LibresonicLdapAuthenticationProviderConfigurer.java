package org.libresonic.player.security;

import org.libresonic.player.service.SecurityService;
import org.libresonic.player.service.SettingsService;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticator;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

public class LibresonicLdapAuthenticationProviderConfigurer<B extends ProviderManagerBuilder<B>> extends SecurityConfigurerAdapter<AuthenticationManager, B> {
    private final SettingsService settingsService;
    private final SecurityService securityService;
    private String groupRoleAttribute = "cn";
    private String groupSearchBase = "";
    private String groupSearchFilter = "(uniqueMember={0})";
    private String rolePrefix = "ROLE_";

    public LibresonicLdapAuthenticationProviderConfigurer(SettingsService settingsService, SecurityService securityService) {
        this.settingsService = settingsService;
        this.securityService = securityService;
    }

    @Override
    public void configure(B builder) throws Exception {
        LdapAuthenticationProvider provider = postProcess(build());
        builder.authenticationProvider(provider);
    }

    private LdapAuthenticationProvider build() throws Exception {
        BaseLdapPathContextSource contextSource = getContextSource();
        LdapAuthenticator ldapAuthenticator = createLdapAuthenticator(contextSource);

        LdapAuthoritiesPopulator authoritiesPopulator = getLdapAuthoritiesPopulator(contextSource);

        LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(
                ldapAuthenticator, authoritiesPopulator);
        ldapAuthenticationProvider.setAuthoritiesMapper(getAuthoritiesMapper());
        return ldapAuthenticationProvider;
    }

    public BaseLdapPathContextSource getContextSource() {
        String url = settingsService.getLdapUrl();
        String managerDn = settingsService.getLdapManagerDn();
        String managerPassword = settingsService.getLdapManagerPassword();
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(url);
        if (managerDn != null) {
            contextSource.setUserDn(managerDn);
            if (managerPassword == null) {
                throw new IllegalStateException(
                        "managerPassword is required if managerDn is supplied");
            }
            contextSource.setPassword(managerPassword);
        }
        contextSource = postProcess(contextSource);
        return contextSource;
    }

    private LdapAuthenticator createLdapAuthenticator(BaseLdapPathContextSource contextSource) {
        AbstractLdapAuthenticator ldapAuthenticator = new BindAuthenticator(contextSource);

        LdapUserSearch userSearch = createUserSearch(contextSource);
        if (userSearch != null) {
            ldapAuthenticator.setUserSearch(userSearch);
        }

        LibresonicLdapAuthenticatorWrapper libresonicLdapAuthenticator = new LibresonicLdapAuthenticatorWrapper(
                ldapAuthenticator,
                settingsService,
                securityService);
        return postProcess(libresonicLdapAuthenticator);
    }

    private LdapUserSearch createUserSearch(BaseLdapPathContextSource contextSource) {
        String userSearchFilter = settingsService.getLdapSearchFilter();
        return new FilterBasedLdapUserSearch("", userSearchFilter, contextSource);
    }

    private LdapAuthoritiesPopulator getLdapAuthoritiesPopulator(BaseLdapPathContextSource contextSource) {

        DefaultLdapAuthoritiesPopulator defaultAuthoritiesPopulator = new DefaultLdapAuthoritiesPopulator(
                contextSource, groupSearchBase);
        defaultAuthoritiesPopulator.setGroupRoleAttribute(groupRoleAttribute);
        defaultAuthoritiesPopulator.setGroupSearchFilter(groupSearchFilter);
        defaultAuthoritiesPopulator.setRolePrefix(rolePrefix);

        return defaultAuthoritiesPopulator;
    }

    protected GrantedAuthoritiesMapper getAuthoritiesMapper() throws Exception {
        SimpleAuthorityMapper simpleAuthorityMapper = new SimpleAuthorityMapper();
        simpleAuthorityMapper.setPrefix(rolePrefix);
        simpleAuthorityMapper.afterPropertiesSet();
        return simpleAuthorityMapper;
    }
}
