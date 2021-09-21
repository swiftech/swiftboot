package org.swiftboot.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.web.Info;
import org.swiftboot.web.R;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 错误代码定义，所有错误代码的变量名必须以 "CODE_" 开头
 * 成功代码以"2"开头，例如"2000"
 * 标准错误代码以"3"开头，例如"3005"
 * 自定义错误代码从"4"开始，例如"4001"
 * 在应用启动的时候必须调用 initErrorCode() 方法对错误信息进行初始化
 *
 * @author swiftech
 **/
public abstract class ErrorCodeSupport {

    private final Logger log = LoggerFactory.getLogger(ErrorCodeSupport.class);

    public static final String CODE_OK = "2000";
    public static final String CODE_OK_WITH_CONTENT = "2002";

    /**
     * 常规错误代码 30xx
     **/
    public static final String CODE_SYS_ERR = "3000"; //未知系统错误
    public static final String CODE_SYS_DB_ERROR = "3001"; //数据库错误
    public static final String CODE_PARAMS_ERROR = "3002"; // 输入参数错误
    public static final String CODE_NO_PERMISSION = "3003"; // 没有权限进行操作
    public static final String CODE_ILLEGAL_API_ACCESS = "3005"; // 非法的 API 调用
    public static final String CODE_APP_VERSION_EXPIRED = "3006"; // 客户端版本低，请升级至新版本
    public static final String CODE_SYS_TIME_DIFF = "3007"; // 客户端时间和服务器端不一致
    public static final String CODE_FILE_UPLOAD_FAIL = "3008"; // 上传文件失败
    public static final String CODE_FILE_DOWNLOAD_FAIL = "3009"; // 下载文件失败
    public static final String CODE_FILE_NOT_EXIST = "3010"; // 文件不存在
    public static final String CODE_TX_VERSION_ERROR = "3012"; // 他人已做操作，请刷新当前页面或数据


    /**
     * 用户错误代码定义 31xx
     */
    // 登录
    public static final String CODE_NO_REG = "3100";// 用户未注册
    public static final String CODE_NO_SIGNIN = "3101";// 用户未登录
    public static final String CODE_SIGNIN_FAIL = "3102";// 账号或密码错误
    public static final String CODE_SIGNIN_WRONG_PWD = "3103";// 密码错误
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


    // code -> message
    private static HashMap<String, String> errorCodeMap = new HashMap<>();

    @Resource
    private MessageSource messageSource;

    /**
     * 设置错误代码对应的错误消息
     *
     * @param code
     * @param msg
     */
    public static void putErrorCodeAndMessage(String code, String msg) {
        errorCodeMap.put(code, msg);
    }


    /**
     * 获取参数化的错误消息
     *
     * @param code
     * @param args
     * @return
     */
    public static String getErrorMessage(String code, String... args) {
        String template = getErrorMessage(code);
        if (code.equals(template)) {
            return code;
        }
        String[] argp = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            argp[i] = String.format("{%d}", i);
        }
        return StringUtils.replaceEach(template, argp, args);
    }

    /**
     * 获取错误代码对于的错误消息
     *
     * @param code
     * @return
     */
    public static String getErrorMessage(String code) {
        String msg;
        try {
            msg = errorCodeMap.get(code);
            if (StringUtils.isBlank(msg)) {
                System.out.printf("WARN: message not found for code '%s'%n", code);
            }
        } catch (Exception e) {
            System.out.println(Info.get(ErrorCodeSupport.class, R.NO_MSG_FOR_CODE1, code));
            msg = code; // 没有则直接返回 CODE
        }
        return msg;
    }

    /**
     * 验证是否存在重复的错误代码
     */
    public static void validate() {
        Map<String, Object> cache = new HashMap<>();
        Field[] fields = ErrorCodeSupport.class.getDeclaredFields();
        if (fields.length == 0) {
            throw new RuntimeException(Info.get(ErrorCodeSupport.class, R.NO_PRE_DEFINED_MSG));
        }
        try {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(String.class)) {
                    if (cache.containsKey(field.get(null))) {
                        throw new RuntimeException(Info.get(ErrorCodeSupport.class, R.REPEAT_MSG1, field.get(null)));
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
    protected void initErrorCode() {
        log.info(StringUtils.join(Info.sources));
        log.info(Info.get(ErrorCodeSupport.class, R.I18N_INIT_START));
        try {
            validate();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            log.info(Info.get(ErrorCodeSupport.class, R.INIT_PRE_DEFINED_MSG1, ErrorCodeSupport.class.getName()));
            List<Field> standard = BeanUtils.getStaticFieldsByType(ErrorCodeSupport.class, String.class);
            log.info(Info.get(ErrorCodeSupport.class, R.TOTAL_MSG_COUNT1, standard.size()));
            for (Field field : standard) {
                if (field.getName().startsWith("CODE_")) {
                    String fieldValue = field.get(null).toString();
                    log.debug(String.format("  %s(%s)", field.getName(), fieldValue));
                    String message = null;
                    try {
                        message = messageSource.getMessage(field.getName(), null, Locale.CHINESE); // TODO
                    } catch (NoSuchMessageException e) {
                        log.info(Info.get(ErrorCodeSupport.class, R.IGNORE_MSG1, field.getName()));
                        continue;
                    }
                    if (StringUtils.isBlank(message)) {
                        log.info(Info.get(ErrorCodeSupport.class, R.NOT_FOUND_MSG1, field.getName()));
                    }
                    else {
                        putErrorCodeAndMessage(field.get(null).toString(), message);
                    }
                }
            }

            log.info(Info.get(ErrorCodeSupport.class, R.INIT_USER_DEFINED_MSG1, this.getClass().getName()));
            List<Field> fields = BeanUtils.getStaticFieldsByType(this.getClass(), String.class);
            for (Field field : fields) {
                if (field.getName().startsWith("CODE_")) {
                    String fieldValue = field.get(null).toString();
                    log.debug(String.format("  %s(%s)", field.getName(), fieldValue));
                    if (StringUtils.isNotBlank(getErrorMessage(fieldValue))) {
                        log.warn(Info.get(ErrorCodeSupport.class, R.CODE_EXIST2, field.getName(), fieldValue));
                        continue;
                    }
                    String message = null;
                    try {
                        message = messageSource.getMessage(field.getName(), null, Locale.CHINESE);
                    } catch (NoSuchMessageException e) {
                        log.info(Info.get(ErrorCodeSupport.class, R.IGNORE_MSG1, field.getName()));
                        continue;
                    }
                    if (StringUtils.isBlank(message)) {
                        log.info(Info.get(ErrorCodeSupport.class, R.NOT_FOUND_MSG1, field.getName()));
                    }
                    else {
                        putErrorCodeAndMessage(field.get(null).toString(), message);
                    }
                }
            }

            if (errorCodeMap == null || errorCodeMap.isEmpty()) {
                log.warn(Info.get(ErrorCodeSupport.class, R.INIT_FAIL));
                return;
            }
            else {
                log.info(Info.get(ErrorCodeSupport.class, R.INIT_COUNT1, errorCodeMap.size()));
            }

            String argumentedMsg = getErrorMessage(CODE_OK_WITH_CONTENT, "this is a param of message");
            log.debug(Info.get(ErrorCodeSupport.class, R.VALIDATE_INI1T, argumentedMsg));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(Info.get(ErrorCodeSupport.class, R.I18N_INIT_DONE));
    }

    @PostConstruct
    public abstract void init();
}
