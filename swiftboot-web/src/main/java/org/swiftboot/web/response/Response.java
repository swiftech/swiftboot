package org.swiftboot.web.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.exception.ErrMessageException;

import java.io.IOException;
import java.io.Serializable;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 通用的 HTTP(S) 请求的返回对象
 *
 * @param <T> 内容对象的类型
 * @author swiftech
 */
@Schema(name = "Response Body")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Response<T> implements Serializable {

    /**
     * Create response builder with inner data type.
     *
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> ResponseBuilder<T> builder(Class<T> clazz) {
        return new ResponseBuilder<T>();
    }

    /**
     * 业务响应代码，长度4个字节的数字
     */
    @Schema(description = "Response code", requiredMode = REQUIRED, example = "1000")
    protected String code = ResponseCode.CODE_OK;

    /**
     * 响应代码对应的响应信息
     */
    @Schema(description = "Response message, default is OK", requiredMode = NOT_REQUIRED, example = "OK")
    protected String message = "OK";

    /**
     * 响应资源中的参数值
     */
    @JsonIgnore
    protected String[] messageArgs;

    /**
     * 返回的对象
     */
    @Schema(description = "Result data object")
    protected T data;

    public Response() {
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(String code) {
        this.code = code;
    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(ErrMessageException errMsgException) {
        this.code = errMsgException.getErrorCode();
        this.message = errMsgException.getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(String[] messageArgs) {
        this.messageArgs = messageArgs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return String.format("{\"error\":\"%s\"}", e.getLocalizedMessage());
        }
    }
}
