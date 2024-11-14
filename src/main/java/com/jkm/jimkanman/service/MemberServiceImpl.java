package com.jkm.jimkanman.service;

import com.jkm.jimkanman.domain.Member;
import com.jkm.jimkanman.domain.enums.MemberStatus;
import com.jkm.jimkanman.dto.MemberRequest;
import com.jkm.jimkanman.dto.MemberResponse;
import com.jkm.jimkanman.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResponse.MemberDto save(MemberRequest.SignupDto signupDto) {
        if(existsByLoginId(signupDto.getLoginId())) throw new RuntimeException("이미 존재하는 아이디입니다.");
        if(existsByNickname(signupDto.getNickname())) throw new RuntimeException("이미 존재하는 닉네임입니다.");

        Member newMember = Member.builder()
                .loginId(signupDto.getLoginId())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .nickname(signupDto.getNickname())
                .username(signupDto.getUsername())
                .phoneNumber(signupDto.getPhoneNumber())
                .roles(RoleProvider.getUserRoles())
                .status(MemberStatus.ACTIVE)
                .email(signupDto.getEmail())
                .build();

        return new MemberResponse.MemberDto(
                memberRepository.save(newMember)
        );
    }

    @Override
    public MemberResponse.MemberDto findById(Long memberId) {
        return new MemberResponse.MemberDto(
                memberRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."))
        );
    }

    @Override
    public Boolean existsById(String memberId) {
        if(!StringUtils.isNumeric(memberId)) throw new RuntimeException("id 형식이 잘못되었습니다.");
        return memberRepository.existsById(Long.parseLong(memberId));
    }

    @Override
    public Boolean existsByLoginId(String loginId) {
        if(Strings.isBlank(loginId)) throw new RuntimeException("id가 비어있습니다.");
        return memberRepository.existsByLoginId(loginId);
    }

    @Override
    public Boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public MemberResponse.MemberDto updateById(String memberId, MemberRequest.UpdateDto updateDto) {
        Long id = Long.parseLong(memberId);
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // TODO: updateDto로 member의 필드 업데이트 -> 노션 고민사항 확인

        // 추가 필드 업데이트 ...

        return new MemberResponse.MemberDto(memberRepository.save(member));
    }

    @Override
    public MemberResponse.MemberDto deleteById(String memberId) {
        if(!StringUtils.isNumeric(memberId)) throw new RuntimeException("id 형식이 잘못되었습니다.");
        Long id = Long.parseLong(memberId);
        Member target = memberRepository.findById(id).orElseThrow();
        memberRepository.deleteById(id);
        return new MemberResponse.MemberDto(target);
    }

    @Override
    /* DuplicateCheckDto 필드 중 null이 아닌 것의 중복 여부를 Map에 담아서 반환 */
    public Map<String, Boolean> checkDuplicate(MemberRequest.DuplicateCheckDto checkDto) {
        Map<String ,Boolean> duplicateStatus = new HashMap<>();
        if(!Strings.isBlank(checkDto.getLoginId())) duplicateStatus.put("id", existsById(checkDto.getLoginId()));
        if(!Strings.isBlank(checkDto.getNickName())) duplicateStatus.put("nickname", existsByNickname(checkDto.getNickName()));
        return duplicateStatus;
    }

    @Override
    public MemberResponse.MemberDto findByLoginId(String loginId) {
        return new MemberResponse.MemberDto(
                memberRepository.findByLoginId(loginId).orElseThrow(() -> new NoSuchElementException("해당 id의 회원 정보를 찾을 수 없습니다."))
        );
    }
}
