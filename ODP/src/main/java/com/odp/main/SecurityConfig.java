package com.odp.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.odp.main.Services.AdminDetailsService;
import com.odp.main.Services.CustomUserDetailsService;
import com.odp.main.Services.PublisherUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PublisherUserDetailsService publisherUserDetailsService;

    @Autowired
    private AdminDetailsService adminDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/loginAndRegister", "/publishersRegister", "/process_register", "/process_publisher_register", "/css/**", "/js/**", "/images/**", "/video/**", "/static/**").permitAll()
                // Admin erişimi
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/publisher/**").hasRole("PUBLISHER")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/loginAndRegister")
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler())
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll())
            .authenticationProvider(authenticationProvider())
            .authenticationProvider(publisherAuthenticationProvider())
            .authenticationProvider(adminAuthenticationProvider());
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/");
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PUBLISHER"))) {
                response.sendRedirect("/");
            } else {
                response.sendRedirect("/");
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public DaoAuthenticationProvider publisherAuthenticationProvider() {
        DaoAuthenticationProvider publisherAuthProvider = new DaoAuthenticationProvider();
        publisherAuthProvider.setUserDetailsService(publisherUserDetailsService);
        publisherAuthProvider.setPasswordEncoder(passwordEncoder());
        return publisherAuthProvider;
    }

    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider adminAuthProvider = new DaoAuthenticationProvider();
        adminAuthProvider.setUserDetailsService(adminDetailsService);
        adminAuthProvider.setPasswordEncoder(passwordEncoder());
        return adminAuthProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

