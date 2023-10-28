package gdsc.speacher.controller;

import com.amazonaws.HttpMethod;
import gdsc.speacher.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {
    private final VideoService videoService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @PostMapping("/generate-presigned-url")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String extension){
        return ResponseEntity.ok(
                videoService.generatePreSignUrl(UUID.randomUUID()+"."+ extension,bucketName, HttpMethod.PUT));

    }
}
