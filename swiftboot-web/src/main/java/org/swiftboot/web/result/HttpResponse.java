package org.swiftboot.web.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import java.io.IOException;
import java.io.Serializable;

/**
 * 通用的 HTTP(S) 请求的返回对象
 *
 * @param <T> 内容对象的类型
 * @author swiftech
 */
@ApiModel
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HttpResponse<T extends Object> implements Serializable {

    /**
     * 业务错误代码，长度4个字节的数字
     */
    @ApiModelProperty(value = "Error code", required = true, example = "1000")
    protected String code = ErrorCodeSupport.CODE_OK;

    /**
     * 错误代码对应的错误信息
     */
    @ApiModelProperty(value = "Error message", required = false, example = "OK")
    protected String msg = ErrorCodeSupport.getErrorMessage(ErrorCodeSupport.CODE_OK);

    /**
     * 返回的对象
     */
    @ApiModelProperty(value = "Result data object")
    protected T result;

    public HttpResponse() {
    }

    public HttpResponse(T result) {
        this.result = result;
    }

    public HttpResponse(String code) {
        this.code = code;
        this.msg = ErrorCodeSupport.getErrorMessage(code);
    }

    public HttpResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public HttpResponse(String code, T result) {
        this.code = code;
        this.msg = ErrorCodeSupport.getErrorMessage(code);
        this.result = result;
    }

    public HttpResponse(String code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public HttpResponse(ErrMessageException errMsgException) {
        this.code = errMsgException.getErrorCode();
        this.msg = errMsgException.getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        this.msg = ErrorCodeSupport.getErrorMessage(code);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
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
