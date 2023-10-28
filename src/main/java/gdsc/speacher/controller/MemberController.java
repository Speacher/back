package gdsc.speacher.controller;

import gdsc.speacher.entity.Member;
import gdsc.speacher.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
@CrossOrigin
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/new")
    public ResponseEntity createMember(@RequestBody Member member) {
        Member save = memberService.save(member);
        if (save == null) {
            return ResponseEntity.badRequest().body("가입 실패");
        }
        return ResponseEntity.ok().body(save);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Member member) {
        Optional<Member> login = memberService.login(member.getEmail(), member.getPassword());
        if (login.isPresent()) {
            return ResponseEntity.ok(login.get());
        }
        return ResponseEntity.badRequest().body("로그인 실패");
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
