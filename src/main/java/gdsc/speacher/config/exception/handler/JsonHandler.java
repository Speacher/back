package gdsc.speacher.config.exception.handler;

import gdsc.speacher.config.code.BaseErrorCode;
import gdsc.speacher.config.exception.GeneralException;

public class JsonHandler extends GeneralException {
    public JsonHandler(BaseErrorCode code) {
        super(code);
    }
}
