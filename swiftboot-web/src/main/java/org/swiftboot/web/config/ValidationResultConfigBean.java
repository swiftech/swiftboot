package org.swiftboot.web.config;

/**
 * @author swiftech
 **/
public class ValidationResultConfigBean {

    /**
     * 是否以 JSON 格式返回验证结果，默认为 true
     */
    boolean resultInJson = true;

    public boolean isResultInJson() {
        return resultInJson;
    }

    public void setResultInJson(boolean resultInJson) {
        this.resultInJson = resultInJson;
    }
}
