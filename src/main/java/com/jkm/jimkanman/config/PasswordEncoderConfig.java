package com.jkm.jimkanman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    PasswordEncoder passwordEncoder() {
        /** 비밀번호를 평문 그대로 인코딩 -> 테스트 용 */
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return (String) rawPassword;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().matches(encodedPassword);
            }
        };
    }
}