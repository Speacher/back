package gdsc.speacher.video.dto;

import gdsc.speacher.cv.dto.CvDto;
import gdsc.speacher.gpt.dto.GptDto;
import gdsc.speacher.nlp.dto.NlpDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {

    private Long videoId;
    private String title;
    private String videoUrl;
    private String createdAt;
    private Integer crossing_arms_count;
    private Integer hands_in_pockets_count;
    private Integer walking_actions;
    private Integer hand_to_face_actions;
    private Integer hands_behind_back_actions;
    private String script;
    private Double time;
    private Double speed;
    private String fillerWord;
    private String gpt;

    public FeedbackDto(VideoDto videoDto, CvDto cvDto, NlpDto nlpDto, GptDto gptDto) {
        this.videoId = videoDto.getVideoId();
        this.title = videoDto.getTitle();
        this.videoUrl = videoDto.getVideoUrl();
        this.createdAt = videoDto.getCreatedAt();
        this.crossing_arms_count = cvDto.getCrossing_arms_count();
        this.hands_in_pockets_count = cvDto.getHands_in_pockets_count();
        this.walking_actions = cvDto.getWalking_actions();
        this.hand_to_face_actions = cvDto.getHand_to_face_actions();
        this.hands_behind_back_actions = cvDto.getHands_behind_back_actions();
        this.script = nlpDto.getScript();
        this.time = nlpDto.getTime();
        this.speed = nlpDto.getSpeed();
        this.fillerWord = nlpDto.getFillerWord();
        this.gpt = gptDto.getGpt();
    }
}
