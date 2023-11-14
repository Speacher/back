package gdsc.speacher.feedback.controller;

import gdsc.speacher.config.BaseResponse;
import gdsc.speacher.feedback.dto.FeedbackDto;

import gdsc.speacher.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
@Slf4j
@CrossOrigin
public class FeedbackController {

    private final FeedbackService feedbackService;

    // 특정 피드백 조회
    @GetMapping("/{videoId}/{feedbackId}")
    public BaseResponse<FeedbackDto> findFeedbackByFeedbackId(@PathVariable Long videoId, @PathVariable Long feedbackId) {
        FeedbackDto byFeedbackId = feedbackService.findByFeedbackId(feedbackId);
        return BaseResponse.onSuccess(byFeedbackId);
    }

    // 특정 비디오 피드백 모두 조회
    @GetMapping("/{videoId}")
    public BaseResponse<List<FeedbackDto>> findFeedbacksByVideoId(@PathVariable Long videoId) {
        List<FeedbackDto> byVideoId = feedbackService.findByVideoId(videoId);
        return BaseResponse.onSuccess(byVideoId);
    }
}
