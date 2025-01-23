package ai.maum.beframework.vo;

import ai.maum.beframework.codemessage.CodeMessage;
import ai.maum.beframework.codemessage.SystemCodeMsg;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 기본 응답
 * @author baekgol@maum.ai
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    @Schema(title = "결과", description = "API 처리 결과 유무", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean result;
    @Schema(title = "코드", description = "API 처리 결과 코드", requiredMode = Schema.RequiredMode.REQUIRED, example = "F000")
    private String code;
    @Schema(title = "데이터", description = "API 처리 결과 데이터", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private T data;
    @Schema(title = "메시지", description = "API 처리 결과 메시지", requiredMode = Schema.RequiredMode.REQUIRED, example = "성공적으로 수행하였습니다.")
    private String message;

    protected BaseResponse() {}

    public static BaseResponse<Void> success() {
        BaseResponse<Void> res = new BaseResponse<>();
        res.result = true;
        res.code = SystemCodeMsg.SUCCESS.getCode();
        res.message = SystemCodeMsg.SUCCESS.getMessage();
        return res;
    }

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> res = new BaseResponse<>();
        res.result = true;
        res.code = SystemCodeMsg.SUCCESS.getCode();
        res.data = data;
        res.message = SystemCodeMsg.SUCCESS.getMessage();
        return res;
    }

    public static <T> BaseResponse<T> failure() {
        BaseResponse<T> res = new BaseResponse<>();
        res.result = false;
        res.message = SystemCodeMsg.FAILURE.getMessage();
        return res;
    }

    public static <T> BaseResponse<T> failure(String message) {
        BaseResponse<T> res = new BaseResponse<>();
        res.result = false;
        res.message = message;
        return res;
    }

    public static <T> BaseResponse<T> failure(CodeMessage codeMsg) {
        BaseResponse<T> res = new BaseResponse<>();
        res.result = false;
        res.code = codeMsg.getCode();
        res.message = codeMsg.getMessage();
        return res;
    }
}
