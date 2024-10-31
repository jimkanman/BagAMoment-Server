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
    public MemberResponse.FullMemberDto saveMember(MemberRequest.SignupDto signupDto) {
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

        return new MemberResponse.FullMemberDto(
                memberRepository.save(newMember)
        );
    }

    @Override
    public MemberResponse.FullMemberDto findById(String memberId) {
        return new MemberResponse.FullMemberDto(
                memberRepository.findById(Long.getLong(memberId)).orElseThrow()
        );
    }

    @Override
    public Boolean existsById(String memberId) {
        return memberRepository.existsById(Long.getLong(memberId));
    }

    @Override
    public Boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public MemberResponse.FullMemberDto updateById(String memberId, MemberRequest.UpdateDto updateDto) {
        return null;
    }

    @Override
    public MemberResponse.FullMemberDto deleteById(String memberId) {
        Long id = Long.getLong(memberId);
        Member target = memberRepository.findById(id).orElseThrow();
        memberRepository.deleteById(id);
        return new MemberResponse.FullMemberDto(target);
    }
}
