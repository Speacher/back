package gdsc.speacher.cv.repository;

import gdsc.speacher.domain.CV;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CvRepository extends JpaRepository<CV, Long> {
}
