package gdsc.speacher.config.exception.handler;

import gdsc.speacher.config.code.BaseErrorCode;
import gdsc.speacher.config.exception.GeneralException;

public class FeedbackHandler extends GeneralException {
    public FeedbackHandler(BaseErrorCode code) {
        super(code);
    }
}
