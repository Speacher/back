package gdsc.speacher.controller;

import gdsc.speacher.dto.feedback.FeedbackDto;

import gdsc.speacher.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity findFeedbackByFeedbackId(@PathVariable Long videoId, @PathVariable Long feedbackId) {
        FeedbackDto byFeedbackId = feedbackService.findByFeedbackId(feedbackId);
        return ResponseEntity.ok().body(byFeedbackId);
    }

    // 특정 비디오 피드백 모두 조회
    @GetMapping("/{videoId}")
    public ResponseEntity findFeedbacksByVideoId(@PathVariable Long videoId) {
        List<FeedbackDto> byVideoId = feedbackService.findByVideoId(videoId);
        return ResponseEntity.ok().body(byVideoId);
    }
}
