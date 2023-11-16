package gdsc.speacher.nlp.repository;

import gdsc.speacher.domain.CV;
import gdsc.speacher.domain.NLP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NLPRepository extends JpaRepository<NLP, Long> {

    Optional<NLP> findByVideoId(Long videoId);
}
