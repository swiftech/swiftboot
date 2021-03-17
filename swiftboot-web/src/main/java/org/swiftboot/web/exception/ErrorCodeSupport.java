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
 * 成功代码以"1xxx"开头
 * 标准错误代码以"2xxx"开头
 * 自定义错误代码从"3000"开始
 * 在应用启动的时候必须调用 initErrorCode() 方法对错误信息进行初始化
 *
 * @author swiftech
 **/
public abstract class ErrorCodeSupport {

    private final Logger log = LoggerFactory.getLogger(ErrorCodeSupport.class);

    public static final String CODE_OK = "1000";
    public static final String CODE_OK_WITH_CONTENT = "1002";

    /**
     * 常规错误代码 20xx
     **/
    public static final String CODE_SYS_ERR = "2000"; //未知系统错误
    public static final String CODE_SYS_DB_ERROR = "2001"; //数据库错误
    public static final String CODE_PARAMS_ERROR = "2002"; // 输入参数错误
    public static final String CODE_NO_PERMISSION = "2003"; // 没有权限进行操作
    public static final String CODE_ILLEGAL_API_ACCESS = "2005"; // 非法的 API 调用
    public static final String CODE_APP_VERSION_EXPIRED = "2006"; // 客户端版本低，请升级至新版本
    public static final String CODE_SYS_TIME_DIFF = "2007"; // 客户端时间和服务器端不一致
    public static final String CODE_FILE_UPLOAD_FAIL = "2008"; // 上传文件失败
    public static final String CODE_FILE_DOWNLOAD_FAIL = "2009"; // 下载文件失败
    public static final String CODE_FILE_NOT_EXIST = "2010"; // 文件不存在
    public static final String CODE_TX_VERSION_ERROR = "2012"; // 他人已做操作，请刷新当前页面或数据


    /**
     * 用户错误代码定义 21xx
     */
    // 登录
    public static final String CODE_NO_REG = "2100";// 用户未注册
    public static final String CODE_NO_SIGNIN = "2101";// 用户未登录
    public static final String CODE_SIGNIN_FAIL = "2102";// 账号或密码错误
    public static final String CODE_SIGNIN_WRONG_PWD = "2103";// 密码错误
    public static final String CODE_USER_FROZEN = "2105";// 用户已被冻结
    public static final String CODE_USER_ACCOUNT_EMPTY = "2106";// 请输入账号
    public static final String CODE_USER_PASSWORD_EMPTY = "2107";// 请输入密码
    public static final String CODE_USER_SESSION_NOT_EXIST = "2108";// 用户登录会话不存在
    public static final String CODE_SESSION_TIMEOUT = "2109";// 会话超时，请重新登录
    public static final String CODE_USER_LACK_INFO = "2110";// 缺少用户信息，无法登录

    // 验证码
    public static final String CODE_CAPTCHA_EMPTY = "2111";// 请输入验证码
    public static final String CODE_CAPTCHA_TOO_MANY = " 2112";// 获取太频繁，请稍后再试
    public static final String CODE_CAPTCHA_SEND_FAIL = "2113";// 发送验证码失败
    public static final String CODE_CAPTCHA_TOOMANY = "2114";// 超过验证码发送上限
    public static final String CODE_CAPTCHA_WRONG = "2115"; // 验证码错误
    public static final String CODE_CAPTCHA_EXPIRED = "2117";// 验证码已失效，请重新获取
    public static final String CODE_CAPTCHA_NO_EXIST = "2118";// 没有找到验证码
    public static final String CODE_SMS_CAPTCHA_LACK_SEND_TO = "2119";// 请输入手机号码

    // 注册
    public static final String CODE_REG_USER_EXISTS = "2131";// 用户已注册
    public static final String CODE_REG_FAIL = "2133";// 用户注册失败
    // 其他
    public static final String CODE_CHANGE_PWD_FAILED = "2141";// 修改密码失败


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
