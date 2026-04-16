package org.swiftboot.web.config;

/**
 * @author swiftech
 **/
public class ValidationResultConfigBean {

    /**
     * 是否以 JSON 格式返回验证结果，默认为 true
     */
    private boolean resultInJson = true;

    /**
     * 是否在 Swagger 注解中启用多语言（只对 Validation 有效）
     */
    private boolean i18n = false;

    public boolean isResultInJson() {
        return resultInJson;
    }

    public void setResultInJson(boolean resultInJson) {
        this.resultInJson = resultInJson;
    }

    public boolean isI18n() {
        return i18n;
    }

    public void setI18n(boolean i18n) {
        this.i18n = i18n;
    }
}
