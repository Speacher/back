package gdsc.speacher.entity;

import gdsc.speacher.entity.feedback.CV;
import gdsc.speacher.entity.feedback.NLP;
import gdsc.speacher.entity.feedback.VR;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "feedback_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @OneToOne(mappedBy = "feedback")
    private CV cv;

    @OneToOne(mappedBy = "feedback")
    private NLP nlp;

    @OneToOne(mappedBy = "feedback")
    private VR vr;

    private String title;
}
