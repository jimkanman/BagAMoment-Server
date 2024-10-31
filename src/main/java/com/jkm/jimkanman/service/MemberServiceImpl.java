package com.jkm.jimkanman.service;

import com.jkm.jimkanman.domain.Member;
import com.jkm.jimkanman.domain.enums.MemberStatus;
import com.jkm.jimkanman.dto.MemberRequest;
import com.jkm.jimkanman.dto.MemberResponse;
import com.jkm.jimkanman.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public MemberResponse.fullMemberDto saveMember(MemberRequest.SignupDto signupDto) {
        if(existsById(signupDto.getId())) throw new RuntimeException("이미 존재하는 아이디입니다.");
        if(existsByNickname(signupDto.getNickname())) throw new RuntimeException("이미 존재하는 닉네임입니다.");

        Member newMember = Member.builder()
                .loginId(signupDto.getId())
                .password(signupDto.getPassword())
                .nickname(signupDto.getNickname())
                .username(signupDto.getUsername())
                .phoneNumber(signupDto.getPhoneNumber())
                .roles(RoleProvider.getUserRoles())
                .status(MemberStatus.ACTIVE)
                .email(signupDto.getEmail())
                .build();

        return null;
    }

    @Override
    public MemberResponse.fullMemberDto findById(String memberId) {
        return null;
    }

    @Override
    public Boolean existsById(String memberId) {
        return null;
    }

    @Override
    public Boolean existsByNickname(String nickname) {
        return null;
    }

    @Override
    public MemberResponse.fullMemberDto updateById(String memberId, MemberRequest.UpdateDto updateDto) {
        return null;
    }

    @Override
    public MemberResponse.fullMemberDto deleteById(String memberId) {
        return null;
    }
}
