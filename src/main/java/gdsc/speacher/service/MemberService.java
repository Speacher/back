package gdsc.speacher.service;

import gdsc.speacher.entity.Member;
import gdsc.speacher.exception.ErrorCode;
import gdsc.speacher.exception.MemberException;
import gdsc.speacher.repository.MemberRepository;
import jakarta.transaction.Transactional;
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


    @Transactional
    public void save(String name, String email, String password) {
        memberRepository.findByEmail(email).ifPresent(value -> {
            log.info("중복 이메일 회원가입 시도");
            throw new MemberException(DUPLICATED_MEMBER_EMAIL);
        });

        memberRepository.save(new Member(name, email, password));
        log.info("{] member 저장", email);
    }

    @Transactional
    public void update(Long id, String name, String email, String password) {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isEmpty()) {
            log.info("없는 회원 수정 시도");
            throw new MemberException(INVALID_ID);
        }
        log.info("{] member 수정", email);
        byId.get().update(name, email, password);
    }
}
