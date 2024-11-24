package com.jkm.jimkanman.util;

import com.jkm.jimkanman.domain.Member;
import com.jkm.jimkanman.global.error.ErrorCode;
import com.jkm.jimkanman.global.error.exception.BusinessException;
import com.jkm.jimkanman.security.CustomUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {
    public Member getMember() {
        if(isUnauthorized())
            return null;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getMember();
    }

    public Member getRequiredMember() {
        if(isUnauthorized())
            throw new BusinessException(ErrorCode.LOGIN_REQUIRED);
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getMember();
    }

    public Long getMemberId() {
        if(isUnauthorized())
            return null;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    public Long getRequiredMemberId() {
        if(isUnauthorized())
            throw new BusinessException(ErrorCode.LOGIN_REQUIRED);
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }


    public String getLoginId() {
        if(isUnauthorized())
            return null;
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getRequiredLoginId() {
        if(isUnauthorized())
            throw new BusinessException(ErrorCode.LOGIN_REQUIRED);
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private boolean isUnauthorized() {
        return SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken;
    }
}
