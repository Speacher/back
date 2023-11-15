package gdsc.speacher.nlp.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NlpDto {

    private String script;

    private Double time;

    private Double speed;

    private String fillerWord;
}