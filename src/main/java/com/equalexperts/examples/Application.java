package com.equalexperts.examples;


import com.equalexperts.examples.controller.Controllers;
import com.equalexperts.examples.dao.Daos;
import com.equalexperts.examples.security.Security;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({Daos.class, Security.class, Controllers.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
