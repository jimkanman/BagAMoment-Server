package com.jkm.jimkanman.controller;

import com.jkm.jimkanman.dto.MemberRequest;
import com.jkm.jimkanman.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "유저 API", description = "유저 관련 API입니다")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    public Object login(@Valid @RequestBody MemberRequest.LoginDto loginDto){
        // 로그인 기능은 LoginFilter에서 작동하므로 swagger 틀만 작성
        return ResponseEntity.ok();
    }

    public Object signup(@Valid @RequestBody  MemberRequest.SignupDto signupDto){
        return new ResponseEntity<>(memberService.saveMember(signupDto), HttpStatus.OK);
    }

    public Object getMember(@PathVariable("id") String memberId){
        return new ResponseEntity<>(memberService.findById(memberId), HttpStatus.OK);
    }

    public Object updateUser(@Valid @PathVariable("id") String memberId, MemberRequest.UpdateDto updateDto){
        return new ResponseEntity<>(memberService.updateById(memberId, updateDto), HttpStatus.OK);
    }

    public Object removeUser(@Valid @PathVariable("id") String memberId){
        return new ResponseEntity<>(memberService.deleteById(memberId), HttpStatus.OK);
    }

    public Object checkIdExists(@RequestParam("id") String memberId){
        return memberService.existsById(memberId);
    }

    public Object checkNicknameExists(@RequestParam("nickname") String nickname){
        return memberService.existsByNickname(nickname);
    }

}
