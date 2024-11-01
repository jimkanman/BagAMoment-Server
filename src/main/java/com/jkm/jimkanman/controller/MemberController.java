package com.jkm.jimkanman.controller;

import com.jkm.jimkanman.dto.MemberRequest;
import com.jkm.jimkanman.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저 관련 API입니다")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "로그인", description = "사용자의 로그인 요청을 처리함")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/login")
    public Object login(@Valid @RequestBody MemberRequest.LoginDto loginDto){
        // 로그인 기능은 LoginFilter에서 작동하므로 swagger 틀만 작성
        return ResponseEntity.ok();
    }

    @Operation(summary = "회원가입", description = "새로운 사용자를 회원으로 등록함")
    @PostMapping("/signup")
    public Object signup(@Valid @RequestBody  MemberRequest.SignupDto signupDto){
        return new ResponseEntity<>(memberService.saveMember(signupDto), HttpStatus.OK);
    }

    @Operation(summary = "회원 조회", description = "해당 ID를 가진 사용자의 정보를 조회함")
    @GetMapping("/users/{id}")
    public Object getMember(@PathVariable("id") String memberId){
        return new ResponseEntity<>(memberService.findById(memberId), HttpStatus.OK);
    }

    @Operation(summary = "회원 정보 수정", description = "해당 ID를 가진 사용자의 정보를 수정함")
    @PutMapping("/users/{id}")
    public Object updateUser(@Valid @PathVariable("id") String memberId, MemberRequest.UpdateDto updateDto){
        return new ResponseEntity<>(memberService.updateById(memberId, updateDto), HttpStatus.OK);
    }

    @Operation(summary = "회원 삭제", description = "해당 ID를 가진 사용자를 삭제함 (회원탈퇴)")
    @DeleteMapping("/users/{id}")
    public Object removeUser(@Valid @PathVariable("id") String memberId){
        return new ResponseEntity<>(memberService.deleteById(memberId), HttpStatus.OK);
    }

    /* url의 쿼리 파라미터로 전달받은 필드가 존재하는지 반환 */
    @Operation(summary = "중복 체크", description = "쿼리 파라미터로 전달받은 필드(id, 닉네임 등)가 중복되는지 확인")
    @GetMapping("/users/check-duplicate")
    public Object checkIdExists(
            @Parameter(description = "중복 체크할 필드 정보 (필수 X)")
            @ModelAttribute MemberRequest.DuplicateCheckDto duplicateCheckDto){
        return new ResponseEntity<>(memberService.checkDuplicate(duplicateCheckDto), HttpStatus.OK);
    }


}
