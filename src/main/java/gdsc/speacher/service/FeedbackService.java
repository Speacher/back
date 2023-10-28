package gdsc.speacher.service;

import gdsc.speacher.entity.Feedback;
import gdsc.speacher.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public Optional<Feedback> findByFeedbackId(Long id) {
        return feedbackRepository.findById(id);
    }

    public Optional<List<Feedback>> findByVideoId(Long id) {
        return feedbackRepository.findAllByVideoIdOrderByCreateDateDesc(id);
    }
}
