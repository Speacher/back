package gdsc.speacher.controller;

import gdsc.speacher.dto.member.LoginDtoRequest;
import gdsc.speacher.dto.member.MemberDto;
import gdsc.speacher.dto.member.MemberEditForm;
import gdsc.speacher.dto.member.MemberSaveForm;
import gdsc.speacher.entity.Member;
import gdsc.speacher.exception.MemberException;
import gdsc.speacher.login.LoginService;
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


import static gdsc.speacher.exception.ErrorCode.*;

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
    public ResponseEntity createMember(@RequestBody MemberSaveForm form) {
        Member save = memberService.save(form.getName(), form.getEmail(), form.getPassword());
        if (save == null) {
            return ResponseEntity.badRequest().body("가입 실패");
        }
        return ResponseEntity.ok().body(save);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity login (@Validated @RequestBody LoginDtoRequest request, BindingResult bindingResult,
                                 HttpServletRequest httpServletRequest) {

        if(bindingResult.hasErrors()) {
            log.info("로그인 오류");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
            // 로그인 페이지로 리다이렉트
        }

        Member loginMember = loginService.login(request.getEmail(), request.getPassword());

        if(loginMember == null) {
            bindingResult.reject("loginFail");
            return new ResponseEntity(HttpStatus.BAD_REQUEST); // 여기도 로그인 실패로 리다이렉트
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
    public ResponseEntity<?> logout (HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity(HttpStatus.OK); // 홈 페이지로 리다이렉트
    }

    // 회원정보 수정
    @PatchMapping
    public ResponseEntity updateMember(@RequestBody MemberEditForm form) {
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Member update = memberService.update(loginMember.getId(), form.getName(), form.getEmail(), form.getPassword());
        if (update == null) {
            throw new MemberException(INVALID_ID_PASSWORD);
        }
        return ResponseEntity.ok().body(update);
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
