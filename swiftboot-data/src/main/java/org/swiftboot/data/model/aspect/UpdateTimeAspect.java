package org.swiftboot.data.model.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.data.Info;
import org.swiftboot.data.R;
import org.swiftboot.data.config.SwiftBootDataConfigBean;
import org.swiftboot.data.model.entity.TimePersistable;
import org.swiftboot.util.GenericUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Date;

import static org.swiftboot.data.constant.AutoUpdateTimeStrategy.AUTO_UPDATE_TIME_ON_CHANGE;
import static org.swiftboot.data.constant.AutoUpdateTimeStrategy.AUTO_UPDATE_TIME_NOT_SET;

/**
 * 持久化实体类之前设置更新时间（updateTime）。由于 Hibernate 的 Interceptor 在数据没改变的情况下不能拦截，
 * 所以只有在 swiftboot.data.model.autoUpdateTimeStrategy 设置为 always 情况下才有效，并且不处理子实体。
 *
 * @author swiftech
 * @see org.swiftboot.data.model.interceptor.TimeInterceptor
 **/
@Aspect
public class UpdateTimeAspect {

    private final Logger log = LoggerFactory.getLogger(UpdateTimeAspect.class);

    @Resource
    private EntityManager entityManager;

    @Resource
    private SwiftBootDataConfigBean configBean;

    @Pointcut(value = "execution(public * org.springframework.data.repository.CrudRepository+.save*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {
        String strategy = configBean.getModel().getAutoUpdateTimeStrategy();
        if (AUTO_UPDATE_TIME_NOT_SET.equals(strategy)
                || AUTO_UPDATE_TIME_ON_CHANGE.equals(strategy)) {
            return null;
        }
        log.debug(this.getClass().getSimpleName() + " executed before save()");
        // 检测前置条件
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }

        for (Object arg : args) {
            if (arg instanceof TimePersistable) {
                TimePersistable<?> entity = (TimePersistable<?>) arg;
                this.tryToSetUpdateTime(entity);
            }
            else if (arg instanceof Iterable) {
                for (Object entity : ((Iterable) arg)) {
                    if (entity instanceof TimePersistable) {
                        this.tryToSetUpdateTime((TimePersistable<?>) entity);
                    }
                }
            }
            else {
                log.debug(Info.get(UpdateTimeAspect.class, R.PARAM_NOT_EXTEND_CLASS2, TimePersistable.class.getName(), arg));
            }
        }
        return null;
    }

    private void tryToSetUpdateTime(TimePersistable entity) {
        // Only persisted entity will be set updateTime;
        if (StringUtils.isNotBlank(entity.getId()) && entityManager.contains(entity)) {
            Class<? extends TimePersistable<?>> clazz = (Class<? extends TimePersistable<?>>) entity.getClass();
            entity.setUpdateTime(this.nowByParameterizedType(clazz));
        }
    }

    Object nowByParameterizedType(Class<? extends TimePersistable<?>> clazz) {
        ParameterizedType parameterizedType = GenericUtils.firstParameterizedType(clazz, TimePersistable.class);
        if (parameterizedType.getActualTypeArguments().length == 0) {
            return null;
        }
        Type type = parameterizedType.getActualTypeArguments()[0];
        log.trace(String.valueOf(type));
        if (type == Long.class) {
            return Long.valueOf(System.currentTimeMillis());
        }
        else if (type == Date.class) {
            return new Date();
        }
        else if (type == LocalDateTime.class) {
            return LocalDateTime.now();
        }
        return null;
    }

}
