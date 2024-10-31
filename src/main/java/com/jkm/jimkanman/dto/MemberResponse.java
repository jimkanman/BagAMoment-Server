package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.Member;
import lombok.*;

public class MemberResponse {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class fullMemberDto {
        private String id;
        private String nickname;
        private String username;
        private String email;
        private String phoneNumber;

        fullMemberDto(Member member){

        }
    }

    public static class simpleMemberDto {
        private String nickname;
        private String username;
    }


}
