package gdsc.speacher.video.controller;

import com.amazonaws.HttpMethod;
import gdsc.speacher.config.BaseResponse;
import gdsc.speacher.video.dto.VideoDto;
import gdsc.speacher.entity.Member;
import gdsc.speacher.entity.Video;
import gdsc.speacher.login.config.SessionConst;
import gdsc.speacher.video.service.VideoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @PostMapping
    public BaseResponse<String> generatePresignedUrl(@RequestParam String extension, @RequestParam String title){
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        log.info("{} member", member.getEmail());
        String generatedUrl = videoService.generatePreSignUrl(UUID.randomUUID() + "." + extension, bucketName, HttpMethod.PUT, title, member.getId());
        return BaseResponse.onSuccess(generatedUrl);
    }

    //비디오 분석
    @PostMapping("/analyze")
    public BaseResponse<byte[]> analyze(@RequestPart("file") MultipartFile file) throws IOException {
        byte[] analyze = videoService.analyze(file);
        return BaseResponse.onSuccess(analyze);
    }
    //비디오 리스트 조회
    @GetMapping
    public BaseResponse<List<VideoDto>> videoList() {
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        List<Video> videos = videoService.findAll(member.getId());
        List<VideoDto> videoDtos = new ArrayList<>();
        for (Video video : videos) {
            VideoDto videoDto = new VideoDto(video);
            videoDtos.add(videoDto);
        }
        return BaseResponse.onSuccess(videoDtos);
    }
    //특정 비디오 조회
    @GetMapping("/{videoId}")
    public BaseResponse<VideoDto> video(@PathVariable Long videoId) {
        Video findVideo = videoService.findById(videoId);
        VideoDto videoDto = new VideoDto(findVideo);
        return BaseResponse.onSuccess(videoDto);
    }
}
