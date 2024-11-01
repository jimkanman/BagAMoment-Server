package com.jkm.jimkanman.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkm.jimkanman.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    {
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("LoginFilter : attempting authentication");

        // 요청에서 id, password 추출
        String id, password;
        if (request.getHeader("content-type").startsWith("application/json")) {
            try {
                // json으로 요청한 경우 파싱해서 꺼낸다
                BufferedReader br = request.getReader();
                String jsonString = br.lines().collect(Collectors.joining(System.lineSeparator()));
                Map<String, String> jsonRequest = objectMapper.readValue(jsonString, Map.class);
                id = jsonRequest.get("accountId");
                password = jsonRequest.get("password");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // form으로 요청한 경우 파라미터에서 꺼낸다
            id = request.getParameter("accountId");
            password = request.getParameter("password");
        }
        System.out.println("LoginFilter: id : '" + id + "' password : '" + password + "'"); // test

        // 예외 처리 - id나 password가 null인 경우 기각
        if (StringUtils.isBlank(id) || StringUtils.isBlank(password)) {
            throw new AuthenticationException("invalid authentication") {};
        }

        // 스프링 시큐리티에서 username과 password를 검증하기 위해서 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password);
        // token 검증을 위해 AuthenticationManager에 전달
        return authenticationManager.authenticate(authToken);
    }

    /**
     * 로그인 성공 시 Response Body에 jwt 발급
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        if(!customUserDetails.isEnabled()){
            try {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().println("Account is locked");
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        String id = authentication.getName();
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());

        // test
        System.out.print("LoginFilter: Hello, " + id + " with role [ ");
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
