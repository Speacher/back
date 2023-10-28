package gdsc.speacher.repository;

import gdsc.speacher.entity.Feedback;
import gdsc.speacher.entity.feedback.CV;
import gdsc.speacher.entity.feedback.VR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VRRepository extends JpaRepository<VR, Long> {
    Optional<VR> findByFeedback(Feedback feedback);
}
