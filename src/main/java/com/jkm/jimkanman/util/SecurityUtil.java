package com.jkm.jimkanman.util;

import com.jkm.jimkanman.domain.Member;
import com.jkm.jimkanman.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {
    public Member getMember() {
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            return null;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getMember();
    }

    public Long getMemberId() {
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            return null;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    public String getLoginId() {
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            return null;
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
