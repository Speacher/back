package gdsc.speacher.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Member
    DUPLICATED_MEMBER_EMAIL(CONFLICT, "DUPLICATE EMAIL"),
    INVALID_EMAIL_OR_PASSWORD(UNAUTHORIZED, "INVALID EMAIL OR PASSWORD"),
    INVALID_ID(UNAUTHORIZED, "INVALID ID"),

    // Feedback
    INVALID_VIDEO_ID(UNAUTHORIZED, "INVALID VIDEO ID"),
    INVALID_FEEDBACK_ID(UNAUTHORIZED, "INVALID FEEDBACK ID");

    private final HttpStatus httpStatus;
    private final String detail;
}