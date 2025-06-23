package org.swiftboot.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.web.Info;
import org.swiftboot.web.R;
import org.swiftboot.web.response.ResponseCode;

import java.util.Arrays;
import java.util.List;

/**
 * 服务层使用的异常
 *
 * @author swiftech
 */
public class ErrMessageException extends RuntimeException {

    protected String errorCode;

    protected List<String> messageArgs;

    public ErrMessageException() {
    }

    public ErrMessageException(ErrMessageException exception) {
        this.errorCode = exception.getErrorCode();
    }

    /**
     * 通过错误码自动查找在资源文件中定义的错误消息
     *
     * @param errorCode
     */
    public ErrMessageException(String errorCode) {
        this.errorCode = errorCode;
        if (StringUtils.isBlank(ResponseCode.getErrorMessage(errorCode))) {
            System.out.println(Info.get(ErrMessageException.class, R.NO_RESOURCE_FOR_ERR_CODE1, errorCode));
        }
    }

    /**
     * 自定义错误消息
     *
     * @param errorCode
     * @param message
     */
    public ErrMessageException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrMessageException(String errorCode, String[] args) {
        this.errorCode = errorCode;
        this.messageArgs = Arrays.asList(args);
        if (StringUtils.isBlank(super.getMessage())) {
            System.out.println(Info.get(ErrMessageException.class, R.NO_RESOURCE_FOR_ERR_CODE1, errorCode));
        }
    }

    public ErrMessageException(String errorCode, List<String> args) {
        this.errorCode = errorCode;
        this.messageArgs = args;
        if (StringUtils.isBlank(super.getMessage())) {
            System.out.println(Info.get(ErrMessageException.class, R.NO_RESOURCE_FOR_ERR_CODE1, errorCode));
        }
    }

    public ErrMessageException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(List<String> messageArgs) {
        this.messageArgs = messageArgs;
    }

    @Override
    public String toString() {
        //  输出错误信息
        return "ErrMessageException{" +
                "errorCode='" + errorCode + "'," + getMessage() +
                '}';
    }


}
