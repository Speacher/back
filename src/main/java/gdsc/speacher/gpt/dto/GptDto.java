package gdsc.speacher.gpt.dto;

import gdsc.speacher.domain.GPT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptDto {

    String gpt;

    public GptDto(GPT gpt) {
        this.gpt = gpt.getGpt();
    }
}
