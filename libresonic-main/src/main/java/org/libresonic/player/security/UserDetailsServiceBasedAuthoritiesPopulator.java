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
package org.libresonic.player.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * An {@link LdapAuthoritiesPopulator} that retrieves the roles from the
 * database using the {@link UserDetailsService} instead of retrieving the roles
 * from LDAP. An instance of this class can be configured for the
 * {@link org.springframework.security.ldap.authentication.LdapAuthenticationProvider} when
 * authentication should be done using LDAP and authorization using the
 * information stored in the database.
 *
 * @author Thomas M. Hofmann
 */
@Component
public class UserDetailsServiceBasedAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    @Autowired
    private UserDetailsService userDetailsService;

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
        UserDetails details = userDetailsService.loadUserByUsername(username);
        return details.getAuthorities();
    }
}