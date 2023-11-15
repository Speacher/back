package gdsc.speacher.video.dto;

import gdsc.speacher.domain.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {

    private Long videoId;
    private String title;
    private String videoUrl;
    private String createdAt;
    public VideoDto(Video video) {
        this.videoId = video.getId();
        this.title = video.getTitle();
        this.videoUrl = video.getVideoUrl();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createdAt = video.getLastModifiedDate().format(formatter);
    }
}
