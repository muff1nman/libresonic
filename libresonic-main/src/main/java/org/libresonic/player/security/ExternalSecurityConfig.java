package org.libresonic.player.security;

import org.apache.commons.lang3.StringUtils;
import org.libresonic.player.service.SecurityService;
import org.libresonic.player.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

@Configuration
@EnableWebSecurity
public class ExternalSecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(ExternalSecurityConfig.class);

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

    @Override
    @Bean(name = "extAuthenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean(name = "jwtAuthenticationFilter")
    public JWTRequestParameterProcessingFilter jwtAuthFilter() throws Exception {
        JWTRequestParameterProcessingFilter filter = new JWTRequestParameterProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        String jwtKey = settingsService.getJWTKey();
        if(StringUtils.isBlank(jwtKey)) {
            logger.warn("Generating new jwt key");
            jwtKey = JWTSecurityUtil.generateKey();
            settingsService.setJWTKey(jwtKey);
            settingsService.save();
        }
        auth.authenticationProvider(new JWTAuthenticationProvider(jwtKey));
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http = http.addFilter(jwtAuthFilter());
        http = http.addFilter(new WebAsyncManagerIntegrationFilter());

        http
            .csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher).and()
            .headers().frameOptions().sameOrigin().and()
            .authorizeRequests()
                .antMatchers("/stream/**", "/coverArt.view", "/share/**", "/hls/**")
                .hasAnyRole("TEMP", "USER").and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling().and()
            .securityContext().and()
            .requestCache().and()
            .anonymous().and()
            .servletApi();
    }
}