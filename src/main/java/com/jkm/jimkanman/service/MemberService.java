package com.jkm.jimkanman.service;


import com.jkm.jimkanman.dto.MemberRequest;
import com.jkm.jimkanman.dto.MemberResponse;

public interface MemberService {

    MemberResponse.fullMemberDto saveMember(MemberRequest.SignupDto signupDto);

    MemberResponse.fullMemberDto findById(String memberId);

    Boolean existsById(String memberId);

    Boolean existsByNickname(String nickname);

    MemberResponse.fullMemberDto updateById(String memberId, MemberRequest.UpdateDto updateDto);

    MemberResponse.fullMemberDto deleteById(String memberId);
}
