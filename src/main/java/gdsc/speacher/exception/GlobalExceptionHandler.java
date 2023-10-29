package gdsc.speacher.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> speacherExceptionHandler(MemberException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
    @ExceptionHandler(FeedbackException.class)
    public ResponseEntity<ErrorResponse> speacherExceptionHandler(FeedbackException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
