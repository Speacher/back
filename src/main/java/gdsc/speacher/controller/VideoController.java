package gdsc.speacher.controller;

import com.amazonaws.HttpMethod;
import gdsc.speacher.dto.video.VideoDto;
import gdsc.speacher.entity.Video;
import gdsc.speacher.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
@CrossOrigin
public class VideoController {
    private final VideoService videoService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    //비디오 업로드
    @PostMapping("/upload/{id}")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String extension, @RequestParam String title, @PathVariable Long id){
        return ResponseEntity.ok(
                videoService.generatePreSignUrl(UUID.randomUUID()+"."+ extension,bucketName, HttpMethod.PUT, title, id));
    }

    //비디오 분석
    //@PostMapping("/analyze")

    //비디오 리스트 조회
    @GetMapping("/{id}")
    public List<VideoDto> videoList(@PathVariable Long id) {
        List<Video> videos = videoService.findAll(id);
        List<VideoDto> videoDtos = new ArrayList<>();
        for (Video video : videos) {
            VideoDto videoDto = new VideoDto(video);
            videoDtos.add(videoDto);
        }
        return videoDtos;
    }

    //특정 비디오 조회
    @GetMapping("/{videoId}/{id}")
    public VideoDto video(@PathVariable Long videoId, @PathVariable Long id) {
        Video findVideo = videoService.findById(videoId);
        VideoDto videoDto = new VideoDto(findVideo);
        return videoDto;
    }
}
