package org.libresonic.player.spring;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class AdditionalPropertySourceConfigurer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {
    public void initialize(ConfigurableWebApplicationContext ctx) {
        PropertySource ps = new CommonsConfigurationPropertySource("libresonic-pre-init-configs");
        ctx.getEnvironment().getPropertySources().addLast(ps);
    }
}
