package gdsc.speacher.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import gdsc.speacher.cv.dto.CvDto;

import java.io.IOException;

public class JsonToCvDtoConverter {

    private ObjectMapper objectMapper;

    public JsonToCvDtoConverter() {
        this.objectMapper = new ObjectMapper();
    }

    public CvDto convert(String json) throws IOException {
        return objectMapper.readValue(json, CvDto.class);
    }
}
