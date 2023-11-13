package gdsc.speacher.config.exception.handler;


import gdsc.speacher.config.code.BaseErrorCode;
import gdsc.speacher.config.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
