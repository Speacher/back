package gdsc.speacher.repository;

import gdsc.speacher.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LoginMemberRepository {
    @PersistenceContext
    private EntityManager em;

    public Optional<Member> findByLoginId(String loginEmail) {
        return findAll().stream()
                .filter(m -> m.getEmail().equals(loginEmail))
                .findFirst();
    }

    public List<Member> findAll() {
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        return members;
    }
}
