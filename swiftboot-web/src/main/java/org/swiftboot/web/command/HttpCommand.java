package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 所有 HTTP(S) 请求命令都继承自 HttpCommand
 *
 * @author swiftech
 */
@ApiModel
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HttpCommand implements Serializable {

    /**
     * HTTP(S) 请求头
     */
    @ApiModelProperty("HTTP(S) headers")
    @JsonIgnore
    private Map<String, String> headers;

    public HttpCommand() {
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeader(String k, String v) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(k, v);
    }

    @JsonIgnore
    public String getHeaderString() {
        StringBuffer buf = new StringBuffer();
        if (headers != null) {
            for (String header : headers.keySet()) {
                buf.append(header).append(headers.get(header));
            }
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}
