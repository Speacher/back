package gdsc.speacher.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import gdsc.speacher.entity.Video;
import gdsc.speacher.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class VideoService {

    private final VideoRepository videoRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Transactional
    public String generatePreSignUrl(String filePath,
                                     String bucketName,
                                     HttpMethod httpMethod) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); // 10분의 유효 기간

        String generatedUrl = amazonS3.generatePresignedUrl(bucket, filePath, calendar.getTime(), httpMethod).toString();

        Video video = new Video(generatedUrl);
        log.info("비디오 생성 후 디비 저장 {}", generatedUrl);
        videoRepository.save(video);
        return generatedUrl;
    }

    public List<Video> findAll(Long memberId) {
        log.info("모든 영상 최신순으로 모두 조회");
        return videoRepository.findAllByMemberIdOrderByCreateDateDesc(memberId);
    }

}
