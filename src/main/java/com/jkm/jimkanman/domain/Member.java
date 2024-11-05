package com.jkm.jimkanman.domain;

import com.jkm.jimkanman.domain.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "member")
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;
    private String loginId; // 로그인용 id
    private String nickname; // 닉네임
    private String username; // 이름
    private String email;
    private String phoneNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles; // 권한 리스트

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    private MemberStatus status; // 계정 상태

    // 소셜 로그인 관련 필드
    private String provider; // 소셜 제공자 (예: google, kakao)
    private String providerId; // 소셜 제공자에서 발급한 사용자 ID

}