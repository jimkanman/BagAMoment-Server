package com.jkm.jimkanman.domain.enums;

public enum MemberStatus {
    ACTIVE,
    INACTIVE, // 비활성화 - 로그인 실패 등
    SUSPENDED, // 정지 - 제재 등
    BANNED, // 영구 정지
}
