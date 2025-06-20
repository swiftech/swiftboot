package org.swiftboot.demo.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.swiftboot.data.model.Initializer;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.data.model.id.EntityIdGenerator;
import org.swiftboot.data.model.id.IdGenerator;

@AutoConfiguration
public class AppDataConfig {

    @Bean
    public IdGenerator<IdPersistable> idGenerator() {
        return new EntityIdGenerator();
    }

    @Bean
    public Initializer initializer() {
        Initializer initializer = new Initializer();
//        initializer.forEntities(GoodsEntity.class, GoodsDetailEntity.class);
        return initializer;
    }
}
