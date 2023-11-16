package gdsc.speacher.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CV extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cv_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    private Integer crossing_arms_count;
    private Integer hands_in_pockets_count;
    private Integer walking_actions;
    private Integer hand_to_face_actions;
    private Integer hands_behind_back_actions;

    public CV(Video video, Integer crossing_arms_count, Integer hands_in_pockets_count, Integer walking_actions, Integer hand_to_face_actions, Integer hands_behind_back_actions) {
        this.video = video;
        this.crossing_arms_count = crossing_arms_count;
        this.hands_in_pockets_count = hands_in_pockets_count;
        this.walking_actions = walking_actions;
        this.hand_to_face_actions = hand_to_face_actions;
        this.hands_behind_back_actions = hands_behind_back_actions;
    }
}
