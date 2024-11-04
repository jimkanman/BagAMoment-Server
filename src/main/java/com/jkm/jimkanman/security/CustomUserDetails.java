package com.jkm.jimkanman.security;

import com.jkm.jimkanman.domain.Member;
import com.jkm.jimkanman.domain.enums.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Member member;


    public Member getMember(){ return member; }

    public String getLoginId(){ return member.getLoginId(); }

    public Long getId() { return member.getId(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !member.getStatus().equals(MemberStatus.SUSPENDED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !member.getStatus().equals(MemberStatus.INACTIVE); // 비밀번호 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return member.getStatus().equals(MemberStatus.ACTIVE);
    }
}
