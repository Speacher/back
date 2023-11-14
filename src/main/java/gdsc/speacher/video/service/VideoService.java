package gdsc.speacher.video.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import gdsc.speacher.domain.Member;
import gdsc.speacher.domain.Video;
import gdsc.speacher.member.repository.MemberRepository;
import gdsc.speacher.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class VideoService {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Transactional
    public String generatePreSignUrl(String filePath,
                                     String bucketName,
                                     HttpMethod httpMethod, String title, Long id) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); // 10분의 유효 기간

        String generatedUrl = amazonS3.generatePresignedUrl(bucket, filePath, calendar.getTime(), httpMethod).toString();

        Member member = memberRepository.findById(id).get();
        Video video = new Video(member, generatedUrl, title);
        log.info("비디오 생성 후 디비 저장 {}", generatedUrl);
        videoRepository.save(video);
        return generatedUrl;
    }

    @Transactional
    public byte[] analyze(MultipartFile file) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        log.info("flask 서버 API 호출");
        // Flask 서버 API 호출
        ResponseEntity<byte[]> response = restTemplate.exchange(
                "http://127.0.0.1:5000/api/predict", org.springframework.http.HttpMethod.POST, requestEntity, byte[].class);
        log.info("분석 완료 - 분석 결과 : {}", response.getBody());
        return response.getBody();
    }

    public List<Video> findAll(Long memberId) {
        log.info("모든 영상 최신순으로 모두 조회");
        return videoRepository.findAllByMemberIdOrderByCreateDateDesc(memberId);
    }

    public Video findById(Long videoId) {
        Video findVideo = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("영상 조회 시 오류 발생"));
        log.info("{} 비디오 영상 조회", findVideo.getTitle());
        return findVideo;
    }
}
