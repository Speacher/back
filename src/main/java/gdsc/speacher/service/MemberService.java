package gdsc.speacher.service;

import gdsc.speacher.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;

}
