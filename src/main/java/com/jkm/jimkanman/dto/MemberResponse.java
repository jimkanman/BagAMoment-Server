package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.Member;
import lombok.*;

public class MemberResponse {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberDto {
        private Long id;
        private String loginId;
        private String nickname;
        private String username;
        private String email;
        private String phoneNumber;

        public MemberDto(Member member){
            id = member.getId();
            loginId = member.getLoginId();
            nickname = member.getNickname();
            username = member.getUsername();
            email = member.getEmail();
            phoneNumber = member.getPhoneNumber();
        }
    }

    public static class SimpleMemberDto {
        private String nickname;
        private String username;
    }

    public static class TokenDto {
        private Long id;
        private String loginToken;
    }

    public static class DuplicateStatusDto {
        private Boolean id;
        private Boolean nickname;
    }

}
