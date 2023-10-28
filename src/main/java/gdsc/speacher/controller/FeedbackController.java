package gdsc.speacher.controller;

import gdsc.speacher.entity.Feedback;
import gdsc.speacher.entity.Member;
import gdsc.speacher.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
@Slf4j
@CrossOrigin
public class FeedbackController {

    private final FeedbackService feedbackService;

    // 특정 피드백 조회
    @GetMapping("/feedbackId/{feedbackId}")
    public ResponseEntity findFeedbackByFeedbackId(@PathVariable Long feedbackId) {
        Optional<Feedback> byFeedbackId = feedbackService.findByFeedbackId(feedbackId);
        if (byFeedbackId.isPresent()) {
            return ResponseEntity.ok().body(byFeedbackId.get());
        }
        return ResponseEntity.badRequest().body("조회 실패");
    }

    // 특정 비디오 피드백 모두 조회
    @GetMapping("/videoId/{videoId}")
    public ResponseEntity findFeedbacksByVideoId(@PathVariable Long videoId) {
        Optional<List<Feedback>> byVideoId = feedbackService.findByVideoId(videoId);
        if (byVideoId.isPresent()){
            return ResponseEntity.ok().body(byVideoId.get());
        }
        return ResponseEntity.badRequest().body("조회 실패");
    }
}
