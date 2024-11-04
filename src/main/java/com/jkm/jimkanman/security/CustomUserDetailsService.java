package com.jkm.jimkanman.security;

import com.jkm.jimkanman.repository.MemberRepository;
import com.jkm.jimkanman.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(memberRepository.findByLoginId(username)
                .orElseThrow(() -> new NoSuchElementException("해당 회원 정보를 찾을 수 없습니다")));
    }
}
