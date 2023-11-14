package gdsc.speacher.feedback.repository;

import gdsc.speacher.domain.Feedback;
import gdsc.speacher.domain.feedback.VR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VRRepository extends JpaRepository<VR, Long> {
    Optional<VR> findByFeedback(Feedback feedback);
}
