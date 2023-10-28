package gdsc.speacher.service;

import gdsc.speacher.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private FeedbackRepository feedbackRepository;
}
