package com.example.socialnetwork.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserConfig {
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
//        return email -> userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException(String.format("UserDomain %s not found", email)));
        return null;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
