package com.jkm.jimkanman.service;


import com.jkm.jimkanman.dto.MemberRequest;
import com.jkm.jimkanman.dto.MemberResponse;

public interface MemberService {

    MemberResponse.FullMemberDto save(MemberRequest.SignupDto signupDto);

    MemberResponse.FullMemberDto findById(Long memberId);

    Boolean existsById(String memberId);

    Boolean existsByLoginId(String loginId);

    Boolean existsByNickname(String nickname);

    MemberResponse.FullMemberDto updateById(String memberId, MemberRequest.UpdateDto updateDto);

    MemberResponse.FullMemberDto deleteById(String memberId);

    Object checkDuplicate(MemberRequest.DuplicateCheckDto duplicateCheckDto);

    MemberResponse.FullMemberDto findByLoginId(String loginId);
}
