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

    private String title;
    private String videoUrl;
    private String createdAt;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public VideoDto(Video video) {
        this.title = video.getTitle();
        this.videoUrl = video.getVideoUrl();
        this.createdAt = video.getLastModifiedDate().format(formatter);
    }
}
