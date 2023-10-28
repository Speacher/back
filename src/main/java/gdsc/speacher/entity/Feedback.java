package gdsc.speacher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "feedback_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    private String title;
    private String script;

    @Column(name = "error_count")
    private double errorCount;

    //그래프??

}
