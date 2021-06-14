package com.uit.realestate.configuration.property;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@EnableAutoConfiguration
public class PropertyConfig {

    @Bean
    @ConfigurationProperties(prefix = "app-config")
    public ApplicationProperties authProperties() {
        return new ApplicationProperties();
    }
}