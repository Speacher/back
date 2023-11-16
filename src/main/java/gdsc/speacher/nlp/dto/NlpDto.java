package gdsc.speacher.nlp.dto;

import gdsc.speacher.domain.NLP;
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

    public NlpDto(NLP nlp) {
        this.script = nlp.getScript();
        this.time = nlp.getTime();
        this.speed = nlp.getSpeed();
        this.fillerWord = nlp.getFillerWord();
    }
}