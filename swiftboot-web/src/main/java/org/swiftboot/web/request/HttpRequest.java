package org.swiftboot.web.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 所有 HTTP(S) 请求都继承自 HttpRequest
 *
 * @author swiftech
 */
@Schema(name="REST request")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HttpRequest implements Serializable {

    /**
     * HTTP(S) 请求头
     */
    @Schema(description="HTTP(S) headers")
    @JsonIgnore
    private Map<String, String> headers;

    public HttpRequest() {
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

    public String getHeader(String k) {
        if (this.headers != null) {
            return this.headers.get(k);
        }
        return null;
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
