package com.uit.realestate.configuration.config;

import com.uit.realestate.configuration.config.JpaAuditorAware;
import com.uit.realestate.domain.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class PersistentConfig {

    @Bean(name = "jpaAuditorProvider")
    public AuditorAware<User> jpaAuditorProvider() {
        return new JpaAuditorAware();
    }
}
