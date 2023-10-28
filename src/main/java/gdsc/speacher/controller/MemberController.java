package gdsc.speacher.controller;

import gdsc.speacher.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private MemberService memberService;
}
