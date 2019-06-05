package org.swiftboot.web.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * 服务层使用的异常
 *
 * @author swiftech
 */
public class ErrMessageException extends RuntimeException {

    protected String errorCode;

    public ErrMessageException() {
    }

    public ErrMessageException(ErrMessageException exception) {
        super(ErrorCodeSupport.getErrorMessage(exception.getErrorCode()));
        this.errorCode = exception.getErrorCode();
    }

    public ErrMessageException(String errorCode) {
        super(ErrorCodeSupport.getErrorMessage(errorCode));
        this.errorCode = errorCode;
        if (StringUtils.isBlank(ErrorCodeSupport.getErrorMessage(errorCode))) {
            System.out.println("找不到错误代码" + errorCode + "的资源");
        }
    }

    public ErrMessageException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrMessageException(String errorCode, String[] args) {
        super(ErrorCodeSupport.getErrorMessage(errorCode, args));
        this.errorCode = errorCode;
        if (StringUtils.isBlank(super.getMessage())) {
            System.out.println("找不到错误代码" + errorCode + "的资源");
        }
    }

    public ErrMessageException(String errorCode, Throwable cause) {
        super(ErrorCodeSupport.getErrorMessage(errorCode), cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        //  输出错误信息
        return "ErrMessageException{" +
                "errorCode='" + errorCode + "\'," + getMessage() +
                '}';
    }


}
