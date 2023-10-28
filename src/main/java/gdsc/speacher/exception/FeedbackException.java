package gdsc.speacher.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedbackException extends RuntimeException {
    private final ErrorCode errorCode;
}