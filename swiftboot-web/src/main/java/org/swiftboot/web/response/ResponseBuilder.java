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
    protected String msg = null;

    /**
     * 错误资源中的参数值
     */
    protected String[] msgParams;

    /**
     * 返回的对象
     */
    protected T result;

    public ResponseBuilder<T> code(String errCode) {
        this.code = errCode;
        return this;
    }

    public ResponseBuilder<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResponseBuilder<T> msgParams(String... msgParams) {
        this.msgParams = msgParams;
        return this;
    }

    public ResponseBuilder<T> result(T result) {
        this.result = result;
        return this;
    }

    public Response<T> build() {
        Response<T> response = new Response<>();
        response.setCode(code);
        if (StringUtils.isNotBlank(msg)) {
            response.setMessage(msg);
        }
        response.setMsgParams(msgParams);
        response.setData(result);
        return response;
    }
}
