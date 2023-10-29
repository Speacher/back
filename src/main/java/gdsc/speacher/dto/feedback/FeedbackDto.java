package gdsc.speacher.dto.feedback;

import gdsc.speacher.entity.Video;
import gdsc.speacher.entity.feedback.CV;
import gdsc.speacher.entity.feedback.NLP;
import gdsc.speacher.entity.feedback.VR;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private VideoDto videoDto;
    private CvDto cvDto;
    private NlpDto nlpDto;
    private VrDto vrDto;

    private String title;

    public FeedbackDto(Video video, CV cv, NLP nlp, VR vr, String title) {
        this.videoDto = new VideoDto(video);
        this.cvDto = new CvDto(cv.getVal());
        this.nlpDto = new NlpDto(nlp.getVal());
        this.vrDto = new VrDto(vr.getVal());
        this.title = title;
    }
}
