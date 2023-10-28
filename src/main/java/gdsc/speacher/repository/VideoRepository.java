package gdsc.speacher.repository;


import gdsc.speacher.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findAllByMemberIdOrderByCreateDateDesc(@Param("memberId") Long memberId);
}
