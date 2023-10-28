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
                                     HttpMethod httpMethod, String title) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); // 10분의 유효 기간

        String generatedUrl = amazonS3.generatePresignedUrl(bucket, filePath, calendar.getTime(), httpMethod).toString();

        // 영상 URL을 데이터베이스에 저장
        Video video = new Video(generatedUrl, title);
        log.info("{} 비디오 생성 후 디비 저장 {}", title, generatedUrl);
        videoRepository.save(video);
        return generatedUrl;
    }
}
