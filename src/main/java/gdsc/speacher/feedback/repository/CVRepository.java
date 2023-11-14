package gdsc.speacher.feedback.repository;

import gdsc.speacher.entity.Feedback;
import gdsc.speacher.entity.feedback.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CVRepository extends JpaRepository<CV, Long> {
    Optional<CV> findByFeedback(Feedback feedback);
}
