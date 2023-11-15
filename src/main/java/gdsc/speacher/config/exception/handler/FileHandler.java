package gdsc.speacher.config.exception.handler;

import gdsc.speacher.config.code.BaseErrorCode;
import gdsc.speacher.config.exception.GeneralException;

public class FileHandler extends GeneralException {
    public FileHandler(BaseErrorCode code) {
        super(code);
    }
}
