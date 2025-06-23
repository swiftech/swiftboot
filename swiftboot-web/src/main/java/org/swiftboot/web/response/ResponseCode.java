package org.swiftboot.web.response;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.web.Info;
import org.swiftboot.web.R;
import org.swiftboot.web.i18n.MessageHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 响应代码定义，所有代码的变量名必须以 "CODE_" 开头
 * 成功代码以"2"开头，例如"2000"
 * 标准错误代码以"3"开头，例如"3005"
 * 自定义错误代码从"4"开始，例如"4001"
 * 在应用启动的时候必须调用 initErrorCode() 方法对错误信息进行初始化
 *
 * @author swiftech
 **/
@Component
public class ResponseCode {

    private static final Logger log = LoggerFactory.getLogger(ResponseCode.class);

    public static final String CODE_OK = "2000";
    public static final String CODE_OK_WITH_CONTENT = "2002";

    /**
     * 常规错误代码 30xx
     **/
    public static final String CODE_SYS_ERR = "3000"; //未知系统错误
    public static final String CODE_SYS_DB_ERROR = "3001"; //数据库错误
    public static final String CODE_ARGUMENTS_ERROR = "3002"; // 输入参数错误
    public static final String CODE_NO_PERMISSION = "3003"; // 没有权限进行操作
    public static final String CODE_ILLEGAL_API_ACCESS = "3005"; // 非法的 API 调用
    public static final String CODE_APP_VERSION_EXPIRED = "3006"; // 客户端版本低，请升级至新版本
    public static final String CODE_SYS_TIME_DIFF = "3007"; // 客户端时间和服务器端不一致
    public static final String CODE_FILE_UPLOAD_FAIL = "3008"; // 上传文件失败
    public static final String CODE_FILE_DOWNLOAD_FAIL = "3009"; // 下载文件失败
    public static final String CODE_FILE_NOT_EXIST = "3010"; // 文件不存在
    public static final String CODE_TX_VERSION_ERROR = "3012"; // 他人已做操作，请刷新当前页面或数据
    public static final String CODE_ARGUMENTS_ERROR_PARAM = "3013"; // 输入参数错误 {0}

    /**
     * 用户错误代码定义 31xx
     */
    // 登录
    public static final String CODE_NO_REG = "3100";// 用户未注册
    public static final String CODE_NO_SIGNIN = "3101";// 用户未登录
    public static final String CODE_SIGNIN_FAIL = "3102";// 账号或密码错误
    public static final String CODE_SIGNIN_WRONG_PWD = "3103";// 密码错误
    public static final String CODE_USER_DOEST_NOT_EXIST = "3104";// 用户不存在
    public static final String CODE_USER_FROZEN = "3105";// 用户已被冻结
    public static final String CODE_USER_ACCOUNT_EMPTY = "3106";// 请输入账号
    public static final String CODE_USER_PASSWORD_EMPTY = "3107";// 请输入密码
    public static final String CODE_USER_SESSION_NOT_EXIST = "3108";// 用户登录会话不存在
    public static final String CODE_SESSION_TIMEOUT = "3109";// 会话超时，请重新登录
    public static final String CODE_USER_LACK_INFO = "3110";// 缺少用户信息，无法登录

    // 验证码
    public static final String CODE_CAPTCHA_EMPTY = "3111";// 请输入验证码
    public static final String CODE_CAPTCHA_TOO_MANY = " 3112";// 获取太频繁，请稍后再试
    public static final String CODE_CAPTCHA_SEND_FAIL = "3113";// 发送验证码失败
    public static final String CODE_CAPTCHA_TOOMANY = "3114";// 超过验证码发送上限
    public static final String CODE_CAPTCHA_WRONG = "3115"; // 验证码错误
    public static final String CODE_CAPTCHA_EXPIRED = "3117";// 验证码已失效，请重新获取
    public static final String CODE_CAPTCHA_NO_EXIST = "3118";// 没有找到验证码
    public static final String CODE_SMS_CAPTCHA_LACK_SEND_TO = "3119";// 请输入手机号码

    // 注册
    public static final String CODE_REG_USER_EXISTS = "3131";// 用户已注册
    public static final String CODE_REG_FAIL = "3133";// 用户注册失败
    // 其他
    public static final String CODE_CHANGE_PWD_FAILED = "3141";// 修改密码失败

    // mapping for fallback resource:  code -> resource key
    private static final HashMap<String, String> codeKeyMap = new HashMap<>();

    // mapping for locale bundled resource: code + locale -> resource key
    private static final MultiKeyMap<Object, String> codeLocaleKeyMap = MultiKeyMap.multiKeyMap(new LRUMap<>());

    @Resource
    private MessageSource messageSource;

    @Resource
    private MessageHelper messageHelper;

    /**
     * 设置错误代码对应的错误消息
     *
     * @param code
     * @param msg
     */
    public void putErrorCodeAndMessage(String code, Locale locale, String msg) {
        if (locale == null) {
            codeKeyMap.put(code, msg);
        }
        else {
            codeLocaleKeyMap.put(code, locale, msg);
        }
    }

    public String getMessage(String code) {
        return getMessage(code, null);
    }

    public String getMessage(String code, String... args) {
        String msg;
        try {
            Locale locale = LocaleContextHolder.getLocale();
            String key;
            if (StringUtils.isBlank(locale.getLanguage())) {
                key = codeKeyMap.get(code);
            }
            else {
                key = codeLocaleKeyMap.get(code, locale);
                // still need to fall back if not found
                if (StringUtils.isBlank(key)) {
                    key = codeKeyMap.get(code);
                }
            }
            msg = messageSource.getMessage(key, args, locale);
            if (StringUtils.isBlank(msg)) {
                System.out.printf("WARN: message not found for code '%s'-'%s'%n", code, locale);
            }
        } catch (Exception e) {
            System.out.println(Info.get(ResponseCode.class, R.NO_MSG_FOR_CODE1, code));
            msg = code; // 没有则直接返回 CODE
        }
        return msg;
    }

    /**
     * 获取错误代码对应的错误消息
     *
     * @param code
     * @return
     */
    public static String getErrorMessage(String code) {
        String msg;
        try {
            String key;
            if (StringUtils.isBlank(LocaleContextHolder.getLocale().getLanguage())) {
                key = codeKeyMap.get(code);
            }
            else {
                key = codeLocaleKeyMap.get(code, LocaleContextHolder.getLocale());
                // still need to fall back if not found
                if (StringUtils.isBlank(key)) {
                    key = codeKeyMap.get(code);
                }
            }
            if (StringUtils.isBlank(key)) {
                System.out.printf("WARN: message not found for code '%s'-'%s'%n", code, LocaleContextHolder.getLocale());
            }
            msg = code;
        } catch (Exception e) {
            System.out.println(Info.get(ResponseCode.class, R.NO_MSG_FOR_CODE1, code));
            msg = code; // 没有则直接返回 CODE
        }
        return msg;
    }

    /**
     * 验证是否存在重复的错误代码
     */
    public void validate(Class<?> errCodeClass) {
        Map<String, Object> cache = new HashMap<>();
        Field[] fields = errCodeClass.getDeclaredFields();
        if (fields.length == 0) {
            throw new RuntimeException(Info.get(ResponseCode.class, R.NO_PRE_DEFINED_MSG));
        }
        try {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(String.class)) {
                    if (cache.containsKey(field.get(null))) {
                        throw new RuntimeException(Info.get(ResponseCode.class, R.REPEAT_MSG1, field.get(null)));
                    }
                    cache.put(String.valueOf(field.get(null)), field);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从i18n资源初始化错误代码对应的错误信息
     */
    public void initErrorCode() {
        log.info(StringUtils.join(Info.sources));
        log.info(Info.get(this.getClass(), R.I18N_INIT_START));
        try {
            this.validate(this.getClass());
            log.info(Info.get(ResponseCode.class, R.INIT_PRE_DEFINED_MSG1, this.getClass()));
            this.loadFromClass(ResponseCode.class);
//            log.info(Info.get(ErrorCodeSupport.class, R.INIT_USER_DEFINED_MSG1, this.getClass()));
            if (codeLocaleKeyMap.isEmpty()) {
                log.warn(Info.get(ResponseCode.class, R.INIT_FAIL));
                return;
            }
            else {
                log.info(Info.get(ResponseCode.class, R.INIT_COUNT1, codeLocaleKeyMap.size()));
            }
            // validate immediately
            if (log.isDebugEnabled()) {
                String argumentedMsg = getMessage(CODE_OK_WITH_CONTENT, "this is a param of message");
                log.debug(Info.get(ResponseCode.class, R.VALIDATE_INI1T, argumentedMsg));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        log.info(Info.get(ResponseCode.class, R.I18N_INIT_DONE));
    }

    /**
     * @param errCodeClass
     * @throws IllegalAccessException
     */
    public void loadFromClass(Class<?> errCodeClass) throws IllegalAccessException, IOException {
        List<Field> fields = BeanUtils.getStaticFieldsByType(errCodeClass, String.class);

        // load fallback messages
        this.loadMessagesForLocale(fields, null);
        // load messages for all supported locales
        List<Locale> supportedLocales = messageHelper.getSupportedLocales("message");
        for (Locale supportedLocale : supportedLocales) {
            log.info("Load messages for %s".formatted(supportedLocale));
            this.loadMessagesForLocale(fields, supportedLocale);
        }
    }

    private void loadMessagesForLocale(List<Field> fields, Locale locale) throws IllegalAccessException {
        log.info("Load messages for %s".formatted(locale));
        for (Field field : fields) {
            if (field.getName().startsWith("CODE_")) {
                String codeNum = field.get(null).toString();
                log.debug(String.format("  %s(%s)-%s", field.getName(), codeNum, locale));
                boolean alreadyExist = locale == null ? StringUtils.isNotBlank(codeKeyMap.get(codeNum))
                        : StringUtils.isNotBlank(codeLocaleKeyMap.get(codeNum, locale));
                if (alreadyExist) {
                    // ignore if already loaded
                    log.warn(Info.get(ResponseCode.class, R.CODE_EXIST2, field.getName(), codeNum));
                    continue;
                }
                this.putErrorCodeAndMessage(codeNum, locale, field.getName());
            }
        }
    }

    @PostConstruct
    public void init() {
        initErrorCode();
    }
}
