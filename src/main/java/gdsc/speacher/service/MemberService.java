package gdsc.speacher.service;

import gdsc.speacher.config.exception.handler.MemberHandler;
import gdsc.speacher.dto.member.MemberEditForm;
import gdsc.speacher.dto.member.MemberSaveForm;
import gdsc.speacher.entity.Member;
import gdsc.speacher.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static gdsc.speacher.config.code.status.ErrorStatus.DUPLICATED_MEMBER_EMAIL;
import static gdsc.speacher.config.code.status.ErrorStatus.INVALID_ID;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public Long save(MemberSaveForm form) {
        memberRepository.findByEmail(form.getEmail()).ifPresent(value -> {
            log.info("중복 이메일 회원가입 시도");
            throw new MemberHandler(DUPLICATED_MEMBER_EMAIL);
        });
        Member member = new Member(form.getName(), form.getEmail(), form.getPassword());
        memberRepository.save(member);
        log.info("{} member 저장", form.getEmail());
        return member.getId();
    }

    @Transactional
    public void update(Long id ,MemberEditForm form) {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isEmpty()) {
            log.info("없는 회원 수정 시도");
            throw new MemberHandler(INVALID_ID);
        }
        log.info("{} member 수정", form.getEmail());
        byId.get().update(form.getName(), form.getEmail(), form.getPassword());
    }
}
