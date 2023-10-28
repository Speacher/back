package gdsc.speacher.controller;

import gdsc.speacher.dto.login.LoginDtoRequest;
import gdsc.speacher.entity.Member;
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

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
@CrossOrigin
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    // 회원가입
    @PostMapping
    public ResponseEntity createMember(@RequestBody Member member) {
        Member save = memberService.save(member);
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
    @PatchMapping("/{id}")
    public ResponseEntity updateMember(@PathVariable Long id, @RequestBody Member member) {
        Member update = memberService.update(id, member);
        if (update == null) {
            return ResponseEntity.badRequest().body("수정 실패");
        }
        return ResponseEntity.ok().body(member);
    }

    // 회원정보 조회
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        Optional<Member> member = memberService.findById(id);
        if (member.isPresent()) {
            log.info("{} 조회 성공", member.get().getName());
            return ResponseEntity.ok().body(member.get());
        } else {
            log.info("조회 실패");
            return ResponseEntity.badRequest().body("조회 실패");
        }
    }
}
