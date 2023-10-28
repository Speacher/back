package gdsc.speacher.controller;

import com.amazonaws.HttpMethod;
import gdsc.speacher.dto.video.VideoDto;
import gdsc.speacher.entity.Member;
import gdsc.speacher.entity.Video;
import gdsc.speacher.login.SessionConst;
import gdsc.speacher.service.VideoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class VideoController {
    private final VideoService videoService;
    private final HttpSession session;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    //비디오 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String extension, @RequestParam String title){
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        log.info("{} member", member.getEmail());
        return ResponseEntity.ok(
                videoService.generatePreSignUrl(UUID.randomUUID()+"."+ extension,bucketName, HttpMethod.PUT, title, member.getId()));
    }

    //비디오 분석
    //@PostMapping("/analyze")

    //비디오 리스트 조회
    @GetMapping
    public List<VideoDto> videoList() {
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        List<Video> videos = videoService.findAll(member.getId());
        List<VideoDto> videoDtos = new ArrayList<>();
        for (Video video : videos) {
            VideoDto videoDto = new VideoDto(video);
            videoDtos.add(videoDto);
        }
        return videoDtos;
    }

    //특정 비디오 조회
    @GetMapping("/{videoId}")
    public VideoDto video(@PathVariable Long videoId) {
        Video findVideo = videoService.findById(videoId);
        VideoDto videoDto = new VideoDto(findVideo);
        return videoDto;
    }
}
