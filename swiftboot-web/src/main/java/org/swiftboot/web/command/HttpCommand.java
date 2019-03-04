package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 所有 HTTP(S) 请求命令都继承自 HttpCommand
 * 继承实现一个 Command 至少要做以下几步：
 * 1. 实现一个无参数的构造函数
 * 2. 覆盖 init() 方法并在方法中设置 Http 请求返回参数的类型，以及其他必要的初始化步骤
 * 3. 添加需要的属性并加上 @JsonProperty 注解。
 *
 * @author swiftech
 */
@ApiModel
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HttpCommand implements Serializable {

    /**
     * HTTP(S) 请求访问的URL，HttpCommand 不关心请求是 GET 还是 POST，
     */
    @JsonIgnore
    private String url;

    /**
     * HTTP(S) 请求头
     */
    @ApiModelProperty("HTTP(S) 请求头")
    @JsonIgnore
    private Map<String, String> headers;

    /**
     * HTTP(S) 请求的返回值类型，与 responseTypeReference 只能有一个生效
     */
    @JsonIgnore
    private Class<? extends Serializable> responseClassType;

    /**
     * HTTP(S) 请求的返回值类型，与 responseClassType 只能有一个能够生效
     */
    @JsonIgnore
    private TypeReference responseTypeReference;

    /**
     * 一旦设置为 true，则接口只返回缓存内容，不对服务器做 HTTP 请求
     */
    @JsonIgnore
    private boolean cacheOnly = false;

    public HttpCommand() {
        // for serialization only
        init();
    }

    public HttpCommand(String url) {
        this.url = url;
        init();
    }

    public void init() {
        // 继承实现初始化动作
        // TODO 考虑用注解来实现
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Class<? extends Serializable> getResponseClassType() {
        return responseClassType;
    }

    public void setResponseClassType(Class<? extends Serializable> responseClassType) {
        this.responseClassType = responseClassType;
        this.responseTypeReference = null;
    }

    public TypeReference getResponseTypeReference() {
        return responseTypeReference;
    }

    public void setResponseTypeReference(TypeReference responseTypeReference) {
        this.responseTypeReference = responseTypeReference;
        this.responseClassType = null;
    }

    @JsonIgnore
    public String getResponseTypeName() {
        return String.valueOf(getResponseClassType() != null ? getResponseClassType().getName() : getResponseTypeReference().getType());
    }

    @JsonIgnore
    public String getHeaderString() {
        StringBuffer bufHeaderStr = new StringBuffer();
        if (headers != null) {
            for (String header : headers.keySet()) {
                bufHeaderStr.append(header).append(headers.get(header));
            }
        }
        return bufHeaderStr.toString();
    }

    public boolean isCacheOnly() {
        return cacheOnly;
    }

    public void setCacheOnly(boolean cacheOnly) {
        this.cacheOnly = cacheOnly;
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
