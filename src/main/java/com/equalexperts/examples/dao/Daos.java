package com.equalexperts.examples.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class Daos {

    @Bean
    public UserDao userDao(JdbcTemplate template) {
        return new UserDao(template);
    }
}
