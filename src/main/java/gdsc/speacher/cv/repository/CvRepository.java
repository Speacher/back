package gdsc.speacher.cv.repository;

import gdsc.speacher.domain.CV;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CvRepository extends JpaRepository<CV, Long> {
    Optional<CV> findByVideoId(Long videoId);
}
