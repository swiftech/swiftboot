package org.swiftboot.data.model.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.data.SwiftBootDataConfigBean;
import org.swiftboot.data.model.entity.TimePersistable;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static org.swiftboot.data.constant.AutoUpdateTimeStrategy.*;

/**
 * 自动设置实体的创建时间（createTime）和更新时间（updateTime）。
 * 对于子集合来说，新创建的实体类会调用 {@code onSave} 方法， merge 实体类会调用 {@code onFlushDirty} 方法。
 * 对于直接持久化的实体来说，如果数据没有改变但是配置为强制设置更新时间的情况下，需要通过 {@code UpdateTimeAspect} 来实现。
 *
 * @author swiftech
 * @see org.swiftboot.data.model.aspect.UpdateTimeAspect
 * @since 2.0.0
 */
public class TimeInterceptor extends EmptyInterceptor {

    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_TIME = "updateTime";

    private final Logger log = LoggerFactory.getLogger(TimeInterceptor.class);

    @Resource
    private SwiftBootDataConfigBean configBean;

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
//        log.debug(this.getClass().getSimpleName() + ".onSave()");
//        log.debug(String.format(" %s - %s%n", entity, id));
//        System.out.println(StringUtils.join(propertyNames, ", "));
//        System.out.println(StringUtils.join(state, ", "));
//        System.out.println(StringUtils.join(types, ", "));
        boolean changed = false;
        if (entity instanceof TimePersistable) {
            for (int i = 0; i < propertyNames.length; i++) {
                String pname = propertyNames[i];
                if (CREATE_TIME.equals(pname)) {
                    state[i] = nowByType(types[i]);
                    changed = true;
                }
            }
        }
        return changed;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
//        log.debug(this.getClass().getSimpleName() + ".onFlushDirty()");
//        log.debug(String.format(" %s - %s%n", entity, id));
//        System.out.println(StringUtils.join(propertyNames, ", "));
//        System.out.println(StringUtils.join(previousState, ", "));
//        System.out.println(StringUtils.join(currentState, ", "));
//        System.out.println(StringUtils.join(types, ", "));
        boolean changed = false;
        String flag = configBean.getModel().getAutoUpdateTimeStrategy();
        if (AUTO_UPDATE_TIME_NOT_SET.equals(flag)) {
            return changed;
        }

        if (entity instanceof TimePersistable) {
            // find if data changed (except createTime and updateTime)
            boolean dataChanged = false;
            for (int i = 0; i < propertyNames.length; i++) {
                String propertyName = propertyNames[i];
                if (CREATE_TIME.equals(propertyName) || UPDATE_TIME.equals(propertyName))
                    continue; // createTime and updateTime are not handled by client.
                if (types[i] instanceof CollectionType)
                    continue; // sub-collection will be handled after parent entity's onSave(), which means sub-collection will make parent dirty.
                if (!Objects.equals(previousState[i], currentState[i])) {
                    dataChanged = true;
                    break;
                }
            }

            for (int i = 0; i < propertyNames.length; i++) {
                String propertyName = propertyNames[i];
                if (CREATE_TIME.equals(propertyName)) {
                    currentState[i] = previousState[i]; // restore createTime for merged entities
                }
                else if (UPDATE_TIME.equals(propertyName)) {
                    if (AUTO_UPDATE_TIME_ALWAYS.equals(flag) ||
                            (AUTO_UPDATE_TIME_ON_CHANGE.equals(flag)) && dataChanged) { // force or really changed entities will be updated updateTime.
                        currentState[i] = nowByType(types[i]);
                        changed = true; // mark as changed
                    }
                    else {
                        currentState[i] = previousState[i]; // restore updateTime for merged entities if no need to update.
                    }
                }
            }
        }
        else {
//            log.debug("Entity without updateTime: " + entity);
        }
        return changed;
    }

    private Object nowByType(Type type) {
        if (type instanceof LongType) {
            return System.currentTimeMillis();
        }
        else if (type instanceof LocalDateTimeType) {
            return LocalDateTime.now();
        }
        else if (type instanceof TimestampType) {
            return new Date();
        }
        else {
            throw new RuntimeException("Type of value is not supported: " + type);
        }
    }
}
