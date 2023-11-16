package gdsc.speacher.video.controller;

import com.amazonaws.HttpMethod;
import gdsc.speacher.config.BaseResponse;
import gdsc.speacher.cv.dto.CvDto;
import gdsc.speacher.domain.*;
import gdsc.speacher.nlp.dto.NlpDto;
import gdsc.speacher.video.dto.FeedbackDto;
import gdsc.speacher.video.dto.VideoDto;
import gdsc.speacher.login.config.SessionConst;
import gdsc.speacher.video.dto.VideoRes;
import gdsc.speacher.video.service.VideoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public BaseResponse<VideoRes> generatePresignedUrl(@RequestParam String extension, @RequestParam String title){
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        // log.info("{} member", member.getEmail());
        VideoRes videoRes = videoService.generatePreSignUrl(UUID.randomUUID() + "." + extension, bucketName, HttpMethod.PUT, title, 1L);
        return BaseResponse.onSuccess(videoRes);
    }

    //비디오 cv분석
    @CrossOrigin
    @PostMapping("/{videoId}/analyze-cv")
    public BaseResponse<CvDto> analyzeCv( @PathVariable Long videoId)  {
        CvDto analyze = videoService.analyzeCv(videoId);
        return BaseResponse.onSuccess(analyze);
    }
    //비디오 nlp분석
    @PostMapping("/{videoId}/analyze-nlp")
    public BaseResponse<NlpDto> analyzeNlp(@PathVariable Long videoId)  {
        NlpDto analyze = videoService.analyzeNlp(videoId);
        return BaseResponse.onSuccess(analyze);
    }

    //비디오 리스트 조회
    @GetMapping
    public BaseResponse<List<VideoDto>> videoList() {
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        System.out.println("member = " + member);
        List<Video> videos = videoService.findAll(1L);
        List<VideoDto> videoDtos = new ArrayList<>();
        for (Video video : videos) {
            VideoDto videoDto = new VideoDto(video);
            videoDtos.add(videoDto);
        }
        return BaseResponse.onSuccess(videoDtos);
    }
    //특정 비디오 조회
    @GetMapping("/{videoId}")
    public BaseResponse<FeedbackDto> video(@PathVariable Long videoId) {
        FeedbackDto video = videoService.findVideo(videoId);
        return BaseResponse.onSuccess(video);
    }

    //특정 비디오 CV 피드백 조회
    @GetMapping("/{videoId}/cv")
    public BaseResponse<CvDto> cvFeedback(@PathVariable Long videoId) {
        Video findVideo = videoService.findById(videoId);
        CV cv = findVideo.getCv();
        CvDto cvDto = new CvDto(cv);
        return BaseResponse.onSuccess(cvDto);
    }
    //특정 비디오 CV 피드백 조회
    @GetMapping("/{videoId}/nlp")
    public BaseResponse<NlpDto> nlpFeedback(@PathVariable Long videoId) {
        Video findVideo = videoService.findById(videoId);
        NLP nlp = findVideo.getNlp();
        NlpDto nlpDto = new NlpDto(nlp.getScript(),nlp.getTime(),nlp.getSpeed(),nlp.getFillerWord());
        return BaseResponse.onSuccess(nlpDto);
    }


}
