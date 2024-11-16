package com.jkm.jimkanman.service;


import com.jkm.jimkanman.dto.MemberRequest;
import com.jkm.jimkanman.dto.MemberResponse;

public interface MemberService {

    MemberResponse.MemberDto save(MemberRequest.SignupDto signupDto);

    MemberResponse.MemberDto findById(Long memberId);

    Boolean existsById(String memberId);

    Boolean existsByLoginId(String loginId);

    Boolean existsByNickname(String nickname);

    MemberResponse.MemberDto updateById(String memberId, MemberRequest.UpdateDto updateDto);

    MemberResponse.MemberDto deleteById(String memberId);

    Object checkDuplicate(MemberRequest.DuplicateCheckDto duplicateCheckDto);

    MemberResponse.MemberDto findByLoginId(String loginId);
}
