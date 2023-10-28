package gdsc.speacher.controller;

import com.amazonaws.HttpMethod;
import gdsc.speacher.dto.video.VideoDto;
import gdsc.speacher.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {
    private final VideoService videoService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    //비디오 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String extension){
        return ResponseEntity.ok(
                videoService.generatePreSignUrl(UUID.randomUUID()+"."+ extension,bucketName, HttpMethod.PUT));
    }

    //비디오 분석
    //@PostMapping("/analyze")

    //비디오 리스트 조회
    /*@GetMapping
    public List<VideoDto> videoList() {

    }*/
}
