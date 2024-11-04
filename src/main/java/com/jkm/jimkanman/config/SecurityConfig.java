package com.jkm.jimkanman.config;

import com.jkm.jimkanman.repository.MemberRepository;
import com.jkm.jimkanman.security.JwtFilter;
import com.jkm.jimkanman.security.LoginFilter;
import com.jkm.jimkanman.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration configuration;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
//                .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("**").permitAll() // TODO 일시적으로 허용해놓음 -> 배포할 때 삭제
                        .requestMatchers("/favicon.ico", "/error", "/swagger-ui/**", "/swagger-ui.html/**", "/v3/api-docs/**",
                                "/signup", "/signup/**","/login").permitAll()
                )
//                .addFilterBefore(new LoginFilter(configuration.getAuthenticationManager(), jwtUtil, "/login"), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilter(authenticationManagerBuilder, jwtUtil, "/login", passwordEncoder), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(memberRepository, jwtUtil), LoginFilter.class)
                .build();
    }
}
