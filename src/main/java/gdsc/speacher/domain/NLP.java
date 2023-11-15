package gdsc.speacher.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NLP extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nlp_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String script;

    @Column(nullable = false)
    private Double time;

    @Column(nullable = false)
    private Double speed;

    @Lob
    @Column(nullable = false)
    private String fillerWord;

    @OneToOne
    @JoinColumn(name = "video_id")
    private Video video;

    public NLP(String script, Double time, Double speed, String fillerWord, Video video) {
        this.script = script;
        this.time = time;
        this.speed = speed;
        this.fillerWord = fillerWord;
        this.video = video;
    }
}
