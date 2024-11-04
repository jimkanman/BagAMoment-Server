package com.jkm.jimkanman.security;

import com.jkm.jimkanman.domain.Member;
import com.jkm.jimkanman.repository.MemberRepository;
import com.jkm.jimkanman.service.MemberService;
import com.jkm.jimkanman.util.JwtUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
/** jwt 검증 필터 */
public class JwtFilter extends OncePerRequestFilter {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Override
    /** 인증 가능한 경우 인증 진행, 그 외엔 인증 없이 Exception 던지지 않고 넘어간다 */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        // Authorization 헤더 체크
        if(StringUtils.isBlank(authorization)){
            filterChain.doFilter(request, response);
            return;
        }
        // jwt 카테고리 체크
        if(jwtUtil.getTokenCategory(authorization) != TokenCategory.ACCESS) {
            filterChain.doFilter(request, response);
            return;
        }
        // jwt expire 체크
        if(!jwtUtil.validateToken(authorization)){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Long userId = jwtUtil.getUserId(authorization);
            Member member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException(userId + "의 회원이 존재하지 않습니다."));
            CustomUserDetails userDetails = new CustomUserDetails(member);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // 출력 (디버그용)
            System.out.println("JwtFilter: authenticated user "
                    + SecurityContextHolder.getContext().getAuthentication().getName()
                    + " with role "
                    + Arrays.toString(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()));
        } catch (Exception e) {
            System.out.println("JwtFilter: exception thrown with message '" + e.getMessage() + "'");
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request,response);
    }
}
