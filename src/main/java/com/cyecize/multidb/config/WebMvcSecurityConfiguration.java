package com.cyecize.multidb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebMvcSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public WebMvcSecurityConfiguration() {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().csrfTokenRepository(csrfTokenRepository())
                .and()
                // .csrf().disable()
                //.authorizeRequests()
                //.antMatchers("/register", "/login", "/styles/**", "/files/**").permitAll()
                //.antMatchers("/admin/**").hasAuthority("ADMIN")
                //.anyRequest().authenticated()
                //.and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/home")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .rememberMe()
                .rememberMeParameter("rememberMe")
                .key("theQuickBrownFoxJumpsOverEmLazyGods")
                .rememberMeCookieName("rememberMe")
                .tokenValiditySeconds(7200)
                //.userDetailsService(this.userService)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/unauthorized")
                .and();

    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }
}