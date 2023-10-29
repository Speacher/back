package gdsc.speacher.service;

import gdsc.speacher.dto.feedback.FeedbackDto;
import gdsc.speacher.dto.feedback.VideoDto;
import gdsc.speacher.entity.Feedback;
import gdsc.speacher.entity.Video;
import gdsc.speacher.exception.FeedbackException;
import gdsc.speacher.exception.MemberException;
import gdsc.speacher.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static gdsc.speacher.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final VideoRepository videoRepository;

    public FeedbackDto findByFeedbackId(Long id) {
        Optional<Feedback> byId = feedbackRepository.findById(id);
        if(!byId.isPresent()){
            throw new FeedbackException(INVALID_FEEDBACK_ID);
        }
        Feedback feedback = byId.get();
        FeedbackDto feedbackDto = new FeedbackDto(
                feedback.getVideo(), feedback.getCv(), feedback.getNlp(),
                feedback.getVr(), feedback.getTitle());

        return feedbackDto;
    }

    public List<FeedbackDto> findByVideoId(Long id) {
        Optional<Video> byId = videoRepository.findById(id);
        if(!byId.isPresent()){
            throw new FeedbackException(INVALID_VIDEO_ID);
        }
        Optional<List<Feedback>> all = feedbackRepository.findAllByVideoIdOrderByCreateDateDesc(id);
        if(!all.isPresent()){
            throw new FeedbackException(INVALID_VIDEO_ID);
        }
        List<Feedback> feedbacks = all.get();
        List<FeedbackDto> feedbackDtos = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            FeedbackDto feedbackDto = new FeedbackDto(
                    feedback.getVideo(), feedback.getCv(), feedback.getNlp(),
                    feedback.getVr(), feedback.getTitle());
            feedbackDtos.add(feedbackDto);
        }
        return feedbackDtos;
    }
}
