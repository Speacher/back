package gdsc.speacher.config.code.status;

import gdsc.speacher.config.code.BaseErrorCode;
import gdsc.speacher.config.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // 멤버 관려 에러
    // Member
    MEMBER_NOT_FOUND(BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),
    DUPLICATED_MEMBER_EMAIL(BAD_REQUEST, "MEMBER4003", "이메일이 중복되었습니다"),
    INVALID_EMAIL_OR_PASSWORD(BAD_REQUEST,  "MEMBER4004","이메일 또는 비밀번호가 잘못되었습니다"),
    INVALID_ID(BAD_REQUEST, "MEMBER4005","유효하지 않은 아이디입니다"),

    //video
    VIDEO_INQUIRY_ERROR(INTERNAL_SERVER_ERROR, "VIDEO5001", "영상 조회 시 오류 발생하였습니다"),
    // Feedback
    INVALID_VIDEO_ID(BAD_REQUEST, "VIDEO4001","유효하지 않은 영상입니다"),
    INVALID_FEEDBACK_ID(BAD_REQUEST, "VIDEO4002", "유효하지 않은 피드백입니다"),

    //file error
    FILE_NOT_FOUND(BAD_REQUEST, "FILE4001", "파일이 존재하지 않습니다"),
    INVALID_FILE_FORMAT(BAD_REQUEST, "FILE4002", "mp4 또는 mp3 형식이 아닙니다"),
    FILE_IO_EXCEPTION(INTERNAL_SERVER_ERROR, "FILE5001", "파일 입출력 예외가 발생했습니다."),
    MP4_TO_MP3_ERROR(INTERNAL_SERVER_ERROR, "FILE5002", "mp4에서 mp3 변환 시 에러가 발생하였습니다."),


    //json error
    JSON_TO_STRING_ERROR(INTERNAL_SERVER_ERROR, "JSON_5001", "JSON 변환 시 에러가 발생했습니다"),

    // Ror test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
