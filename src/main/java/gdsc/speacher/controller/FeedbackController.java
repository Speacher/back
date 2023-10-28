package gdsc.speacher.controller;

import gdsc.speacher.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FeedbackController {

    private FeedbackService feedbackService;
}
