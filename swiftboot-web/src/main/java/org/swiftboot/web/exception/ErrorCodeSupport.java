package org.swiftboot.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.swiftboot.util.BeanUtils;

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
 *
 * @author swiftech
 **/
public abstract class ErrorCodeSupport {

    private Logger log = LoggerFactory.getLogger(ErrorCodeSupport.class);

    public static final String CODE_OK = "1000";
    public static final String CODE_OK_WITH_DEPRECATION = "1001"; // 该接口已废弃
    public static final String CODE_OK_WITH_CONTENT = "1002";

    /**
     * 常规错误代码 20xx
     **/
    public static final String CODE_SYS_ERR = "2000";//未知系统错误
    public static final String CODE_VALIDATION_FAIL = "2001"; // 表单验证错误
    public static final String CODE_PARAMS_ERROR = "2002"; // 输入参数错误
    public static final String CODE_NO_PERMISSION = "2003"; // 没有权限进行操作
    public static final String CODE_SESSION_TIMEOUT = "2004"; // 会话超时，请重新登录
    public static final String CODE_ILLEGAL_API_ACESS = "2005"; // 非法的 API 调用
    public static final String CODE_APP_VERSION_EXPIRED = "2006"; // App版本过期，请升级至新版本
    public static final String CODE_SYS_TIME_DIFF = "2007"; // 您的手机系统时间不正确，请先校准！
    public static final String CODE_FILE_UPLOAD_FAIL = "2008"; // 上传文件失败
    public static final String CODE_FILE_DOWNLOAD_FAIL = "2009"; // 上传文件失败
    public static final String CODE_UNDER_DEVELOPMENT = "2010"; // 正在开发中，敬请期待
    public static final String CODE_TEXT_TOO_LONG = "2011"; // 文字长度过长
    public static final String CODE_TXVERSION_ERROR = "2012"; // 他人已做操作，请刷新当前页面或数据
    public static final String CODE_NEED_LOGIN = "2013"; // 需要登录
    public static final String CODE_GET_CURRENT_SYSUSER_FAILED = "2014"; // 获取当前登录的系统用户失败
    public static final String CODE_PERSISTENT_ERROR = "2015"; // 数据库操作失败
    public static final String CODE_FILE_NOT_EXIST= "2016"; // 文件不存在

    /**
     * 用户业务错误代码定义 21xx
     */
    // Signin
    public static final String CODE_NO_REG = "2100";// 用户未注册
    public static final String CODE_NO_SIGNIN = "2101"; // 用户未登录
    public static final String CODE_SIGNIN_FAIL = "2102"; //账号或密码错误
    public static final String CODE_SIGNIN_WRONG_PWD = "2103"; //密码错误
    public static final String CODE_USER_FROZEN = "2105";//用户已被冻结
    public static final String CODE_USER_ACCOUNT_EMPTY = "2106";//请输入账号
    public static final String CODE_USER_PASSWORD_EMPTY = "2107";//请输入密码
    public static final String CODE_VALIDATION_SESSION_NOT_EXIST = "2108"; // 登录会话不存在

    // CAPTCHA
    public static final String CODE_CAPTCHA_NO_EXIST = "2104";// 没有找到验证码
    public static final String CODE_CAPTCHA_EMPTY = "2110";//请输入验证码
    public static final String CODE_CAPTCHA_NO_REPEAT = " 2111";// 不能重复重复获取，请稍后再试
    public static final String CODE_CAPTCHA_SEND_FAIL = "2112";// 发送验证码失败
    public static final String CODE_CAPTCHA_TOOMANY = "2113";// 超过验证码最多条数限制
    public static final String CODE_CAPTCHA_WRONG = "2114"; // 验证码错误
    public static final String CODE_CAPTCHA_RETRY = "2116";// 请稍后再试
    public static final String CODE_CAPTCHA_EXPIRED = "2117";//验证码已失效，请重新获取
    public static final String CODE_SMS_CAPTCHA_WRONG = "2115";// 短信验证码错误
    public static final String CODE_SMS_CAPTCHA_LACK_SEND_TO = "2119";// 缺少手机号码
    public static final String CODE_SMS_CAPTCHA_LACK_DEVICEID = "2118";// 缺少设备ID

    // Register (TODO 部分验证错误代码会被统一验证框架取代）
    public static final String CODE_REG_USER_EXISTS = "2131";// 用户已注册
    public static final String CODE_PWD_ERR = "2132";// 用户密码验证失败
    public static final String CODE_REG_FAIL = "2133";// 用户注册失败
    public static final String CODE_USER_UPDATE_INFO_FAILED = "2134";// 更新个人信息失败
    public static final String CODE_USER_NICK_NAME_TOO_LONG = "2135";// 昵称长度太长
    public static final String CODE_USER_NAME_TOO_LONG = "2136";// 姓名长度太长
    public static final String CODE_VALIDATION_PASSWORD_BLANK = "2137"; // 密码不能为空
    public static final String CODE_VALIDATION_CAPTCHA_BLANK = "2018"; // 请填写验证码
    public static final String CODE_VALIDATION_PASSWORD_TOO_SHORT = "2023"; // 密码长度过短
    public static final String CODE_VALIDATION_PASSWORD_TOO_LONG = "2024"; // 密码长度过长
    // Others
    public static final String CODE_CHANGE_PWD_FAILD = "2141";// 修改密码失败

    // Corporation
    public static final String CODE_CORP_SAVE_FAILED = "2151";// 企业信息录入失败
    public static final String CODE_CORP_CONTACT_NAME_TOO_LONG = "2153";// 企业联系人姓名太长
    public static final String CODE_CORP_NAME_EMPTY = "2154";// 企业名称不能为空
    public static final String CODE_CORP_DISTRICT_EMPTY = "2155";// 企业地区不能为空
    public static final String CODE_CORP_CONTACT_NAME_EMPTY = "2156";// 企业联系人不能为空
    public static final String CODE_CORP_EMAIL_EMPTY = "2157";// 企业联系人电子邮件不能为空
    public static final String CODE_CORP_PHONE_EMPTY = "2158";// 企业联系电话不能为空
    public static final String CODE_CORP_NAME_TOO_LONG = "2159";// 企业名称太长

    /**
     * 表单验证错误信息
     */
    public static final String CODE_VALID_REQUIRED = "3100"; // 必须输入
    public static final String CODE_VALID_LOGIN_NAME_FORMAT = "3101"; //登录名格式错误，只能使用英文字母和数字
    public static final String CODE_VALID_LOGIN_NAME_LENGTH = "3102"; //登录名长度不能低于3位，不能超过32位
    public static final String CODE_VALID_LOGIN_PWD_FORMAT = "3103"; //密码长度不能低于8位，不能超过32位并且必须包含英文字母和数字
    public static final String CODE_VALID_LOGIN_PWD_LENGTH = "3104"; //密码长度不能低于8位，不能超过32位
    public static final String CODE_VALID_IDCARDNO_WRONG = "3105"; //身份证号码格式错误
    public static final String CODE_VALID_PHONENO_WRONG = "3106"; //手机号码格式错误
    public static final String CODE_VALID_EMAIL_WRONG = "3107"; //邮箱格式错误

    /**
     * 其他
     */
    public static final String CODE_LOAD_EMAIL_TEMPLATE_ERROR = "3201"; // 加载邮件模板失败

    // code -> message
    private static HashMap<String, String> errorCodeMap = new HashMap<>();

    @Resource
    MessageSource messageSource;

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
            System.out.printf("WARN: 没有找到错误代码对应的错误消息(%s)%n", code);
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
        try {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(String.class)) {
                    if (cache.containsKey(field.get(null))) {
                        throw new RuntimeException("存在重复的错误代码: " + field.get(null));
                    }
                    cache.put(String.valueOf(field.get(null)), field);
                }
            }

            String argumentedMsg = getErrorMessage(CODE_OK_WITH_CONTENT, "参数化错误信息");
            System.out.println("检测参数化信息：" + argumentedMsg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从i18n资源初始化错误代码对应的错误信息
     */
    protected void initErrorCode() {
        log.info("初始化i18n资源");
        try {
            validate();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            log.info("Init basic error messages");
            List<Field> standard = BeanUtils.getStaticFieldsByType(ErrorCodeSupport.class, String.class);
            log.info(String.format("%d code in total", standard.size()));
            for (Field field : standard) {
                if (field.getName().startsWith("CODE_")) {
                    String fieldValue = field.get(null).toString();
                    log.debug(String.format("  %s(%s)", field.getName(), fieldValue));
                    String message = null;
                    try {
                        message = messageSource.getMessage(field.getName(), null, Locale.CHINESE);
                    } catch (NoSuchMessageException e) {
                        log.info(String.format("Message %s ignored", field.getName()));
                        continue;
                    }
                    putErrorCodeAndMessage(field.get(null).toString(), message);
                }
            }

            log.info("Init custom error messages from: " + this.getClass().getName());
            List<Field> fields = BeanUtils.getStaticFieldsByType(this.getClass(), String.class);
            for (Field field : fields) {
                if (field.getName().startsWith("CODE_")) {
                    String fieldValue = field.get(null).toString();
                    log.debug(String.format("  %s(%s)", field.getName(), fieldValue));
                    if (StringUtils.isNotBlank(getErrorMessage(fieldValue))) {
                        log.warn(String.format("Error code already exist: %s(%s)", field.getName(), fieldValue));
                        continue;
                    }
                    String message = null;
                    try {
                        message = messageSource.getMessage(field.getName(), null, Locale.CHINESE);
                    } catch (NoSuchMessageException e) {
                        log.info(String.format("Message %s ignored", field.getName()));
                        continue;
                    }
                    putErrorCodeAndMessage(field.get(null).toString(), message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("i18n初始化完成");
    }

    @PostConstruct
    public abstract void init();
}
