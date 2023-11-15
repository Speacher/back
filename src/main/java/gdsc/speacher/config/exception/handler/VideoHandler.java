package gdsc.speacher.config.exception.handler;

import gdsc.speacher.config.code.BaseErrorCode;
import gdsc.speacher.config.exception.GeneralException;

public class VideoHandler extends GeneralException {
    public VideoHandler(BaseErrorCode code) {
        super(code);
    }
}
