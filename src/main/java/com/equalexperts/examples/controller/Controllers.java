package com.equalexperts.examples.controller;

import com.equalexperts.examples.dao.UserDao;
import com.equalexperts.examples.security.CurrentLoggedInUserArgumentResolver;
import com.equalexperts.examples.security.VelocitySecurityHelper;
import org.springframework.boot.autoconfigure.velocity.VelocityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class Controllers extends WebMvcConfigurerAdapter {

    @Bean
    public VelocityLayoutViewResolver velocityViewResolver(VelocityProperties velocityProperties) {
        VelocityLayoutViewResolver velocityLayoutViewResolver = new VelocityLayoutViewResolver() {
            @Override
            protected Class<?> requiredViewClass() {
                return VelocityLayoutToolboxView.class;
            }
        };
        velocityProperties.applyToViewResolver(velocityLayoutViewResolver);
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("sec", new VelocitySecurityHelper());
        velocityLayoutViewResolver.setAttributesMap(attrs);
        return velocityLayoutViewResolver;
    }

    @Bean
    public UserAdminController userAdmin(UserDao userManager, PasswordEncoder encoder) {
        return new UserAdminController(userManager, encoder);
    }

    @Bean
    public HomeController home() {
        return new HomeController();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentLoggedInUserArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }
}
