package gdsc.speacher.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoRes {

    private Long videoId;
    private String generatePresignedUrl;




}
