package org.swiftboot.auth.service;

/**
 * 自定义的简易 Session 服务, 可用于 App 会话控制, 也可以用户 Web 会话控制, 支持分布式
 * 如果会话控制比较复杂的情况可以考虑使用 Shiro 等其他框架
 *
 * @author swiftech
 */
public interface SessionService {

    /**
     * 添加一个 session，如果 Session 指定了 group，那么这个 session 存储在这个分组下，
     * 否则存储在配置文件中 swiftboot.auth.session.group 指定的分组下，
     * 如果没有配置，那么就存储在直接存储在默认的分组下
     *
     * @param token
     * @param session
     */
    void addSession(String token, Session session);

    /**
     * 通过令牌 token 获取 Session 信息，
     * 分组为配置文件中 swiftboot.auth.session.group，
     * 如果要获取不同的分组下的 session， 调用 {@code getSession(String group, String token)} 方法，
     * 超时的会话也会返回，除非调用 {@code verifySession()} 清除了
     *
     * @param token
     * @return
     */
    Session getSession(String token);

    /**
     * 通过分组 group 和令牌 token 获取 Session 信息
	 * 如果 group 为空，则分组为配置文件中 swiftboot.auth.session.group，
	 * 如果没有配置，那么就从默认的分组下查找，
     * 超时的会话也会返回，除非调用 {@code verifySession()} 清除了
	 *
     * @param group
     * @param token
     * @return
     */
    Session getSession(String group, String token);

    /**
     * 移除一个 Session，
	 * 分组为配置文件中 swiftboot.auth.session.group，
	 * 如果没有配置，那么就从默认的分组下查找
     *
     * @param token
     */
    void removeSession(String token);

    /**
     * 移除一个 Session
	 * 如果分组 group 为空，则分组为配置文件中 swiftboot.auth.session.group，
	 * 如果没有配置，那么就从默认的分组下查找
     *
     * @param group
     * @param token
     */
    void removeSession(String group, String token);

    /**
     * 验证会话，验证失败抛出异常，
	 * 分组为配置文件中 swiftboot.auth.session.group，
	 * 如果没有配置，那么就从默认的分组下查找，
	 * 超时的会话会被自动删除
     *
     * @param token
     * @return 返回有效的会话
     */
    Session verifySession(String token);

    /**
     * 验证会话，验证失败抛出异常，
	 * 如果 group 为空，则分组为配置文件中 swiftboot.auth.session.group，
	 * 如果没有配置，那么就从默认的分组下查找，
     * 超时的会话会被自动删除
	 *
     * @param group
     * @param token
     * @return 返回有效的会话
     */
    Session verifySession(String group, String token);

}
