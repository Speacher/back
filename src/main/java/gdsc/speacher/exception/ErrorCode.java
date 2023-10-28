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
    INVALID_ID_PASSWORD(UNAUTHORIZED, "INVALID ID OR PASSWORD"),
    UNAUTHORIZED_USER(UNAUTHORIZED, "NOT APPROVED MEMBER");

    private final HttpStatus httpStatus;
    private final String detail;
}