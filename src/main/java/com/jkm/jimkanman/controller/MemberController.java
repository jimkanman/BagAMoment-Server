package com.jkm.jimkanman.controller;

import com.jkm.jimkanman.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Tag(name = "유저 API", description = "유저 관련 API입니다")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

}
