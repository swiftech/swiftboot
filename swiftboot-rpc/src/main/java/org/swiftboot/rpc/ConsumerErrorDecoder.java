package org.swiftboot.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;
import org.swiftboot.util.IoUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 转换服务提供者因异常返回的错误信息为通用的异常，下一步由 ExceptionProcessor 处理转换后的异常。
 * 依赖于 hibernate-validator 框架
 *
 * @author Allen 2019-02-14
 * @see org.swiftboot.web.exception.ExceptionProcessor
 **/
@Component
public class ConsumerErrorDecoder implements ErrorDecoder {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        System.out.printf("调用服务：%s 发生异常： HTTP %s%n", methodKey, response.status());
        try {
            String body = IoUtils.readAllToString(response.body().asInputStream());
            System.out.println("  异常内容" + body);
            Map mapResp = objectMapper.readValue(body.getBytes(), Map.class);
            StringBuilder msgBuf = new StringBuilder();
            if (mapResp != null) {
                List<Map> errors = (List<Map>) mapResp.get("errors"); // 验证错误
                if (errors == null || errors.isEmpty()) {
                    String message = (String) mapResp.get("message");
                    String error = (String) mapResp.get("error");
                    String path = (String) mapResp.get("path");
                    System.out.printf("%s - %s %s%n", path, error, message);
                    msgBuf.append(message);
                }
                else {
                    for (Map error : errors) {
                        String errField = (String) error.get("field");
                        String errMsg = (String) error.get("defaultMessage");
                        msgBuf.append(" ").append(errField).append(errMsg);
                    }
                }
            }
            return new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR, msgBuf.toString());
        } catch (IOException e) {
            return new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR, e.getMessage());
        }
    }
}
