package org.swiftboot.web.model.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.web.SwiftBootConfigBean;
import org.swiftboot.web.model.entity.BaseIdEntity;
import org.swiftboot.web.model.id.IdGenerator;

import javax.annotation.Resource;

/**
 * 拦截 Spring Data JPA 的保存动作，设置实体类的 ID
 * 如果实体类 ID 已经存在，则不做处理
 * @author swiftech
 **/
@Aspect
public class EntityIdAspect {

    private Logger log = LoggerFactory.getLogger(EntityIdAspect.class);

    @Resource
    SwiftBootConfigBean swiftBootConfigBean;

    @Resource
    private IdGenerator<BaseIdEntity> idGenerator;


    @Pointcut(value = "execution(public * org.springframework.data.repository.CrudRepository+.save*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {
        log.debug(this.getClass().getSimpleName() + " before()");
        // 检测前置条件
        if (!swiftBootConfigBean.getModel().isAutoGenerateId()) {
            return null;
        }
        if (idGenerator == null) {
            return null;
        }
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }

        for (Object arg : args) {
            if (arg instanceof BaseIdEntity) {
                BaseIdEntity idEntity = (BaseIdEntity) arg;
                this.tryToSetID(idEntity);
            }
            else if (arg instanceof Iterable) {
                for (Object idEntity : ((Iterable) arg)) {
                    if (idEntity instanceof BaseIdEntity) {
                        this.tryToSetID((BaseIdEntity) idEntity);
                    }
                }
            }
            else {
                log.debug("略过不处理: " + arg);
            }
        }
        return null;
    }

    private void tryToSetID(BaseIdEntity idEntity) {
        // ID不存在才生成，
        if (StringUtils.isBlank(idEntity.getId())) {
            String id = idGenerator.generate(idEntity);
            idEntity.setId(id);
        }
    }
}