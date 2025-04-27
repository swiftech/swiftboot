package org.swiftboot.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.exception.ErrMessageException;

/**
 * 正确的返回值，但是提示接口已经废弃
 *
 * @author swiftech
 **/
@Schema(name = "HTTP response with deprecation", description = "接口废弃的返回值，标识接口已废弃")
public class ResponseWithDeprecation<T extends Object> extends Response<T> {

    public ResponseWithDeprecation() {
        super.msg = "This API works for now, but will be deprecated in the future.";
    }

    public ResponseWithDeprecation(T result) {
        super(result);
        super.msg = "This API works for now, but will be deprecated in the future.";
    }

    public ResponseWithDeprecation(ErrMessageException errMsgException) {
        super(errMsgException);
        super.msg = "This API works for now, but will be deprecated in the future.";
    }
}
