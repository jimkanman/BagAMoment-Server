package com.jkm.jimkanman.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

public class MemberRequest {
    @Getter
    @AllArgsConstructor
    @ToString
    public static class SignupDto {
        @NotBlank
        @Size(max = 20, message = "ID는 15자 이내여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "id는 영문과 숫자만 포함할 수 있습니다.")
        private String id;

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
                message = "비밀번호는 영문과 숫자, 특수문자만 가능합니다.")
        private String password;

        private String nickname;

        private String username;

        @NotBlank
        @Pattern(regexp = "^\\w+@\\w+.[a-zA-Z]{2,}$",
                 message = "유효한 이메일 주소를 입력하세요.")
        private String email;

        @Pattern(regexp = "^([0-9]{3}-[0-9]{4}-[0-9]{4})|$",
                 message = "잘못된 전화번호 형식입니다.")
        private String phoneNumber;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDto {
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "id는 영문과 숫자만 포함할 수 있습니다.")
        private String id;

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
                message = "비밀번호는 영문과 숫자, 특수문자만 가능합니다.")
        private String password;
    }

    public static class UpdateDto {
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class DuplicateCheckDto {
        @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "id는 영문과 숫자만 포함할 수 있습니다.")
        private String loginId;
        private String nickName;
    }
}
