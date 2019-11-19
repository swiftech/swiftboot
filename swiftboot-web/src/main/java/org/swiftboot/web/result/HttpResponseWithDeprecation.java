package org.swiftboot.web.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import java.io.Serializable;

/**
 * 正确的返回值，但是提示接口已经废弃
 *
 * @author swiftech
 **/
@ApiModel("接口废弃的返回值，标识接口已废弃")
public class HttpResponseWithDeprecation<T extends Object> extends HttpResponse<T> {

    public HttpResponseWithDeprecation() {
        super.msg = "This API is deprecated";
    }

    public HttpResponseWithDeprecation(T result) {
        super(result);
        super.msg = "This API is deprecated";
    }

    public HttpResponseWithDeprecation(ErrMessageException errMsgException) {
        super(errMsgException);
        super.msg = "This API is deprecated";
    }
}
