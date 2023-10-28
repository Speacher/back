package gdsc.speacher.service;

import gdsc.speacher.entity.Member;
import gdsc.speacher.exception.ErrorCode;
import gdsc.speacher.exception.MemberException;
import gdsc.speacher.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static gdsc.speacher.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(String name, String email, String password) {
        Member member = new Member(name, email, password);
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if (byEmail.isEmpty()) { // 중복 아이디 예외처리
            log.info("{] member 저장", member.getEmail());
            memberRepository.save(member);
            return memberRepository.findByEmail(member.getEmail()).get();
        } else {
            throw new MemberException(INVALID_ID_PASSWORD);
        }
    }

    public Optional<Member> login(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    public Member update(Long id, String name, String email, String password) {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().update(name,email,password);
            return memberRepository.findById(id).get();
        } else {
            // 없는 회원
            return null;
        }
    }

    public Optional<Member> findById(Long id) {
        Optional<Member> byId = memberRepository.findById(id);
        return byId;
    }
}
