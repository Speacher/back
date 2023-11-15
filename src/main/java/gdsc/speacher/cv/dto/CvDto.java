package gdsc.speacher.cv.dto;

import gdsc.speacher.domain.CV;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CvDto {

    private Integer crossing_arms_count;
    private Integer hands_in_pockets_count;
    private Integer walking_actions;
    private Integer hand_to_face_actions;
    private Integer hands_behind_back_actions;

    public CvDto(CV cv) {
        this.crossing_arms_count = cv.getCrossing_arms_count() ;
        this.hands_in_pockets_count = cv.getHands_in_pockets_count();
        this.walking_actions = cv.getWalking_actions();
        this.hand_to_face_actions = cv.getHand_to_face_actions();
        this.hands_behind_back_actions = cv.getHands_behind_back_actions();
    }

    public Integer getCrossing_arms_count() {
        return crossing_arms_count;
    }

    public Integer getHands_in_pockets_count() {
        return hands_in_pockets_count;
    }

    public Integer getWalking_actions() {
        return walking_actions;
    }

    public Integer getHand_to_face_actions() {
        return hand_to_face_actions;
    }

    public Integer getHands_behind_back_actions() {
        return hands_behind_back_actions;
    }
}

