package gdsc.speacher.repository;

import gdsc.speacher.entity.Feedback;
import gdsc.speacher.entity.feedback.CV;
import gdsc.speacher.entity.feedback.NLP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NLPRepository extends JpaRepository<NLP, Long> {
    Optional<NLP> findByFeedback(Feedback feedback);
}
