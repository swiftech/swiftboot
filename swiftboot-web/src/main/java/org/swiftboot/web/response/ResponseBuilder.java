package org.swiftboot.web.response;

import org.apache.commons.lang3.StringUtils;

/**
 * @author swiftech
 * @since 2.2
 */
public class ResponseBuilder<T> {

    /**
     * 业务错误代码，长度4个字节的数字
     */
    protected String code = ResponseCode.CODE_OK;

    /**
     * 错误代码对应的错误信息
     */
    protected String message = null;

    /**
     * 错误资源中的参数值
     */
    protected String[] messageArgs;

    /**
     * 返回的对象
     */
    protected T data;

    public ResponseBuilder<T> ok() {
        this.code = ResponseCode.CODE_OK;
        this.message = "OK";
        return this;
    }

    public ResponseBuilder<T> code(String code) {
        this.code = code;
        return this;
    }

    public ResponseBuilder<T> message(String msg) {
        this.message = msg;
        return this;
    }

    public ResponseBuilder<T> messageArgs(String... messageArgs) {
        this.messageArgs = messageArgs;
        return this;
    }

    public ResponseBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    public Response<T> build() {
        Response<T> response = new Response<>();
        response.setCode(code);
        if (StringUtils.isNotBlank(message)) {
            response.setMessage(message);
        }
        response.setMessageArgs(messageArgs);
        response.setData(data);
        return response;
    }
}
