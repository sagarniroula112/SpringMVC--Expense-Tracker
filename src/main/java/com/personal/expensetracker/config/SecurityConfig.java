package com.personal.expensetracker.config;

import com.personal.expensetracker.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer->customizer.ignoringRequestMatchers("/user/login", "/user/signup", "/login/oauth2/**").disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/login", "/user/signup", "/user/logout", "/login/oauth2/**").permitAll()
                .anyRequest()
                .authenticated());




        http.formLogin(form -> form
                .loginPage("/user/login") // Your custom login page
                        .defaultSuccessUrl("/", true)
                .permitAll());

        http.oauth2Login(oauth ->oauth
                .loginPage("/user/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
        );

        http.logout(logout->logout
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/user/login")
                .invalidateHttpSession(true)  // Ensure session is invalidated
                .clearAuthentication(true)
        );

        // OAuth2 Login Configuration for Google
//        http.oauth2Login(oauth2 -> oauth2
//                .loginPage("/user/login") // Custom login page
//                .defaultSuccessUrl("/user/signup", true) // Redirect after successful login
//                .failureUrl("/user/login?error=true") // Redirect on login failure
//        );


//        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }


}
