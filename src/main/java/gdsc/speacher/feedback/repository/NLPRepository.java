package gdsc.speacher.feedback.repository;

import gdsc.speacher.domain.Feedback;
import gdsc.speacher.domain.feedback.NLP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NLPRepository extends JpaRepository<NLP, Long> {
    Optional<NLP> findByFeedback(Feedback feedback);
}
