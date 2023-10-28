package gdsc.speacher.service;

import gdsc.speacher.entity.Member;
import gdsc.speacher.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        Optional<Member> byEmail = memberRepository.findByEmail(member.getEmail());
        if (byEmail.isEmpty()) { // 중복 아이디 예외처리
            memberRepository.save(member);
            return memberRepository.findByEmail(member.getEmail()).get();
        } else {
            return null;
        }
    }

    public Optional<Member> login(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    public Member update(Long id, Member member) {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().update(member);
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
