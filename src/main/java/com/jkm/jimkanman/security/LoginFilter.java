package com.jkm.jimkanman.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkm.jimkanman.dto.MemberRequest;
import com.jkm.jimkanman.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//    private final AuthenticationManager authenticationManager;
private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final String filterUrl;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper = new ObjectMapper();



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 요청에서 id, password 추출
        String loginId, password;
        try {
            // json으로 요청한 경우 파싱해서 꺼낸다
            String jsonString = request.getReader()
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            MemberRequest.LoginDto loginDto = objectMapper.readValue(jsonString, MemberRequest.LoginDto.class);
            loginId = loginDto.getLoginId();;
            password = loginDto.getPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("LoginFilter: login attempt with id : '" + loginId + "' password : '" + password + "'"); // test
        // 스프링 시큐리티에서 username과 password를 검증하기 위해서 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password);
        // token 검증을 위해 AuthenticationManager에 전달
        return authenticationManagerBuilder.getObject().authenticate(authToken);
    }

    /**
     * 로그인 성공 시 Response Body에 jwt 발급 (Long id, TokenCategory 담음)
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        if(!customUserDetails.isEnabled()){
            try {
                // TODO: json 형식으로 ApiResponse 반환 (writeOutput() 등 만들어서)
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().println("Account is locked");
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        Long id = customUserDetails.getId();
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());

        // test
        System.out.print("LoginFilter: Hello, " + authentication.getName() + " with role [ ");
        for (GrantedAuthority auth : authorities) {
            System.out.print(auth.getAuthority() + " ");
        }
        System.out.println("]");

        String accessToken = jwtUtil.issueAccessToken(id, TokenCategory.ACCESS);

        try {
            // body로 jwt 발급
            PrintWriter writer = response.getWriter();
            Map<String, String> jwtBody = new HashMap<>();
            jwtBody.put("authorization", "Bearer " + accessToken);
            writer.println(objectMapper.writeValueAsString(jwtBody));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** 로그인 실패 시 401 반환 */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        // 로그인 실패
        System.out.println("LoginFilter: Login attempt failed.");
        response.setStatus(401);
    }
}
