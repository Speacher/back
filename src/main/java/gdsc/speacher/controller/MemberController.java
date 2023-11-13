package gdsc.speacher.controller;

import gdsc.speacher.config.BaseResponse;
import gdsc.speacher.config.exception.handler.MemberHandler;
import gdsc.speacher.dto.member.LoginDtoRequest;
import gdsc.speacher.dto.member.MemberDto;
import gdsc.speacher.dto.member.MemberEditForm;
import gdsc.speacher.dto.member.MemberSaveForm;
import gdsc.speacher.entity.Member;
import gdsc.speacher.service.LoginService;
import gdsc.speacher.login.SessionConst;
import gdsc.speacher.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static gdsc.speacher.config.code.status.ErrorStatus.INVALID_EMAIL_OR_PASSWORD;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
@CrossOrigin
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final HttpSession session;

    // 회원가입
    @PostMapping
    public BaseResponse<Long> createMember(@RequestBody MemberSaveForm form) {
        return BaseResponse.onSuccess(memberService.save(form));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity login(@Validated @RequestBody LoginDtoRequest request, BindingResult bindingResult,
                                HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            log.info("아이디 또는 비밀번호 형식 오류");
            throw new MemberHandler(INVALID_EMAIL_OR_PASSWORD);
        }
        Member loginMember = loginService.login(request.getEmail(), request.getPassword());
        if (loginMember == null) {
            log.info("아이디 또는 비밀번호 오류");
            throw new MemberHandler(INVALID_EMAIL_OR_PASSWORD);
        }
        HttpSession session = httpServletRequest.getSession();
        // request.getSession() -> 세션이 있으면 세션 반환, 없으면 신규 세션을 생성
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        // 세션에 로그인 회원 정보를 보관
        log.info("{} 로그인 성공!", loginMember.getEmail());
        return new ResponseEntity(HttpStatus.OK);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원정보 수정
    @PatchMapping
    public ResponseEntity updateMember(@RequestBody MemberEditForm form) {
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        memberService.update(loginMember.getId(), form.getName(), form.getEmail(), form.getPassword());
        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원정보 조회
    @GetMapping
    public ResponseEntity findById() {
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        MemberDto memberDto = new MemberDto(loginMember);
        log.info("{} 회원 조회", loginMember.getEmail());
        return ResponseEntity.ok().body(memberDto);
    }
}
