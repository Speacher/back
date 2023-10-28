package gdsc.speacher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Video extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "video_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "video_url")
    private String videoUrl;

    private String title;

    @OneToMany(mappedBy = "video")
    private List<Feedback> feedbacks = new ArrayList<>();

}
