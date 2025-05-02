package org.swiftboot.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.exception.ErrMessageException;

/**
 * 正确的返回值，但是提示接口已经废弃
 *
 * @author swiftech
 **/
@Schema(description = "HTTP response with deprecation")
public class ResponseWithDeprecation<T> extends Response<T> {

    public ResponseWithDeprecation() {
        super.message = "This API works for now, but will be deprecated in the future.";
    }

    public ResponseWithDeprecation(T dto) {
        super(dto);
        super.message = "This API works for now, but will be deprecated in the future.";
    }

    public ResponseWithDeprecation(ErrMessageException errMsgException) {
        super(errMsgException);
        super.message = "This API works for now, but will be deprecated in the future.";
    }
}
