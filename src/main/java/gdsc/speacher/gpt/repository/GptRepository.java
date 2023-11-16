package gdsc.speacher.gpt.repository;

import gdsc.speacher.domain.CV;
import gdsc.speacher.domain.GPT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GptRepository extends JpaRepository<GPT, Long> {

    Optional<GPT> findByVideoId(Long videoId);
}
