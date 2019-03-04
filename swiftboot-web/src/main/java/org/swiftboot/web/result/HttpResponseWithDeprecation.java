package org.swiftboot.web.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.exception.ErrorCodeSupport;

import java.io.Serializable;

/**
 * @author swiftech
 **/
@ApiModel("接口废弃的返回值，标识接口已废弃")
public class HttpResponseWithDeprecation<T extends Object> implements Serializable {

    @ApiModelProperty(value = "业务代码，固定值为 1001", required = true, example = "1001")
    protected String code = ErrorCodeSupport.CODE_OK_WITH_DEPRECATION;

    @ApiModelProperty(value = "废弃的错误信息", example = "该接口已废弃")
    protected String msg = ErrorCodeSupport.getErrorMessage(ErrorCodeSupport.CODE_OK_WITH_DEPRECATION);

    @ApiModelProperty(value = "返回的对象")
    protected T result;

    public HttpResponseWithDeprecation(T result) {
        this.result = result;
    }

    public HttpResponseWithDeprecation(String msg, T result) {
        this.msg = msg;
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
