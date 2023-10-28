package gdsc.speacher.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberException extends RuntimeException {
    private final ErrorCode errorCode;
}