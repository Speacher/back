package gdsc.speacher;

import gdsc.speacher.domain.Member;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitData {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }
    @Component
    static class InitService {

        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            Member member1 = Member.builder()
                    .name("관리자")
                    .email("gyu@naver.com")
                    .password("password1")
                    .build();

            Member member2 = Member.builder()
                    .name("테스터1")
                    .email("test1@naver.com")
                    .password("test1")
                    .build();

            Member member3 = Member.builder()
                    .name("테스터2")
                    .email("test2@naver.com")
                    .password("test2")
                    .build();


            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

        }
    }
}