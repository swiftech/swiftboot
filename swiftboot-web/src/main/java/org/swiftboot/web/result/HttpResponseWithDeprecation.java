package org.swiftboot.web.result;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.swiftboot.web.exception.ErrMessageException;

/**
 * 正确的返回值，但是提示接口已经废弃
 *
 * @author swiftech
 **/
@Schema(name = "HTTP response with deprecation", description = "接口废弃的返回值，标识接口已废弃")
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
