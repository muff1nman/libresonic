package org.libresonic.player.security;

import org.apache.commons.lang3.StringUtils;
import org.libresonic.player.service.SecurityService;
import org.libresonic.player.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class GlobalSecurityConfig extends GlobalAuthenticationConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(GlobalSecurityConfig.class);

    static final String FAILURE_URL = "/login?error=1";

    @Autowired
    private SecurityService securityService;

    @Autowired
    private CsrfSecurityRequestMatcher csrfSecurityRequestMatcher;

    @Autowired
    LoginFailureLogger loginFailureLogger;

    @Autowired
    SettingsService settingsService;

    @Autowired
    LibresonicUserDetailsContextMapper libresonicUserDetailsContextMapper;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        if (settingsService.isLdapEnabled()) {
            auth.ldapAuthentication()
                    .contextSource()
                        .managerDn(settingsService.getLdapManagerDn())
                        .managerPassword(settingsService.getLdapManagerPassword())
                        .url(settingsService.getLdapUrl())
                    .and()
                    .userSearchFilter(settingsService.getLdapSearchFilter())
                    .userDetailsContextMapper(libresonicUserDetailsContextMapper);
        }
        auth.userDetailsService(securityService);
        String jwtKey = settingsService.getJWTKey();
        if(StringUtils.isBlank(jwtKey)) {
            logger.warn("Generating new jwt key");
            jwtKey = JWTSecurityUtil.generateKey();
            settingsService.setJWTKey(jwtKey);
            settingsService.save();
        }
        auth.authenticationProvider(new JWTAuthenticationProvider(jwtKey));
    }


    @Configuration
    @Order(1)
    public class ExtSecurityConfiguration extends WebSecurityConfigurerAdapter {

        public ExtSecurityConfiguration() {
            super(true);
        }

        @Bean(name = "jwtAuthenticationFilter")
        public JWTRequestParameterProcessingFilter jwtAuthFilter() throws Exception {
            return new JWTRequestParameterProcessingFilter(authenticationManager(), FAILURE_URL);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http = http.addFilter(new WebAsyncManagerIntegrationFilter());
            http = http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

            http
                    .antMatcher("/ext/**")
                    .csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher).and()
                    .headers().frameOptions().sameOrigin().and()
                    .authorizeRequests()
                    .antMatchers("/ext/stream/**", "/ext/coverArt.view", "/ext/share/**", "/ext/hls/**")
                    .hasAnyRole("TEMP", "USER").and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .exceptionHandling().and()
                    .securityContext().and()
                    .requestCache().and()
                    .anonymous().and()
                    .servletApi();
        }
    }

    @Configuration
    @Order(2)
    public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            RESTRequestParameterProcessingFilter restAuthenticationFilter = new RESTRequestParameterProcessingFilter();
            restAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
            restAuthenticationFilter.setSecurityService(securityService);
            restAuthenticationFilter.setLoginFailureLogger(loginFailureLogger);
            http = http.addFilterBefore(restAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            http
                    .csrf()
                    .requireCsrfProtectionMatcher(csrfSecurityRequestMatcher)
                    .and().headers()
                    .frameOptions()
                    .sameOrigin()
                    .and().authorizeRequests()
                    .antMatchers("recover.view", "accessDenied.view",
                            "/style/**", "/icons/**", "/flash/**", "/script/**",
                            "/sonos/**", "/crossdomain.xml", "/login", "/error")
                    .permitAll()
                    .antMatchers("/personalSettings.view", "/passwordSettings.view",
                            "/playerSettings.view", "/shareSettings.view", "/passwordSettings.view")
                    .hasRole("SETTINGS")
                    .antMatchers("/generalSettings.view", "/advancedSettings.view", "/userSettings.view",
                            "/musicFolderSettings.view", "/databaseSettings.view")
                    .hasRole("ADMIN")
                    .antMatchers("/deletePlaylist.view", "/savePlaylist.view")
                    .hasRole("PLAYLIST")
                    .antMatchers("/download.view")
                    .hasRole("DOWNLOAD")
                    .antMatchers("/upload.view")
                    .hasRole("UPLOAD")
                    .antMatchers("/createShare.view")
                    .hasRole("SHARE")
                    .antMatchers("/changeCoverArt.view", "/editTags.view")
                    .hasRole("COVERART")
                    .antMatchers("/setMusicFileInfo.view")
                    .hasRole("COMMENT")
                    .antMatchers("/podcastReceiverAdmin.view")
                    .hasRole("PODCAST")
                    .antMatchers("/**")
                    .hasRole("USER")
                    .anyRequest().authenticated()
                    .and().formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/index.view", true)
                    .failureUrl(FAILURE_URL)
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                    // see http://docs.spring.io/spring-security/site/docs/3.2.4.RELEASE/reference/htmlsingle/#csrf-logout
                    .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).logoutSuccessUrl(
                    "/login?logout")
                    .and().rememberMe().key("libresonic");
        }

    }
}