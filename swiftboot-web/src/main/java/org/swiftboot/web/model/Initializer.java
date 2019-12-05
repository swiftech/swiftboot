package org.swiftboot.web.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.web.SwiftBootConfigBean;
import org.swiftboot.web.model.entity.BaseIdEntity;
import org.swiftboot.web.reader.CsvReader;
import org.swiftboot.web.reader.CsvReaderHandler;
import org.swiftboot.web.util.SpringPackageUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 从 CSV 文件中初始化数据库，文件名对应实体类名，CSV 中的字段对应实体类的属性变量名
 * 初始化执行过程中会对主键或者关键字段（必须是 unique 字段）做重复性检查
 *
 * @author swiftech
 * @since 1.1
 */
@Transactional
public class Initializer implements ApplicationContextAware {

    @Resource
    private SwiftBootConfigBean swiftBootConfigBean;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        System.out.println("Init data from files");
        System.out.println(swiftBootConfigBean.getModel().getInitBaseDir());
        System.out.println(StringUtils.join(swiftBootConfigBean.getModel().getInitBaseEntityPackages(), ","));

        File baseDir = new File(swiftBootConfigBean.getModel().getInitBaseDir());
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.out.println("not exists or not a directory");
        }
        else {
            File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".csv"));

            if (files == null || files.length == 0) {
                System.out.println("No files found to init data");
                return;
            }

            Set<Class<?>> entityClasses
                    = SpringPackageUtils.scanClasses(
                    swiftBootConfigBean.getModel().getInitBaseEntityPackages(), BaseIdEntity.class);

            if (entityClasses.isEmpty()) {
                System.out.println("No entity classes found to create instance");
                return;
            }

            Map<String, ? extends Class<?>> classDic = entityClasses.stream().collect(
                    Collectors.toMap(clazz -> clazz.getSimpleName(), clazz -> clazz));

            for (File file : files) {
                String rawFileName = StringUtils.substringBeforeLast(file.getName(), ".csv");
                System.out.println("File: " + rawFileName);
                Class<?> aClass = classDic.get(rawFileName);
                System.out.println("Entity class: " + aClass);

                if (aClass != null) {

                    try {
                        // try to get dao
                        String baseName = StringUtils.substringBeforeLast(aClass.getName(), ".entity.");
                        String daoName = String.format("%s.dao.%sDao", baseName, StringUtils.substringBeforeLast(rawFileName, "Entity"));
                        Class<?> daoClass = Class.forName(daoName);
                        CrudRepository<BaseIdEntity, String> dao = (CrudRepository<BaseIdEntity, String>) this.applicationContext.getBean(daoClass);

                        new CsvReader().readCsv(new FileInputStream(file), new CsvReaderHandler() {
                            BaseIdEntity entity;

                            @Override
                            public void onTitle(List<String> titles) {

                            }

                            @Override
                            public void onRow(int rowNum, List<String> columns) {
                                System.out.println("handle line " + rowNum);
                                try {
                                    entity = (BaseIdEntity) aClass.getDeclaredConstructor().newInstance();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCell(int colNum, String columnName, String cellValue) {
                                System.out.printf("handle data: %s=%s%n", columnName, cellValue);
                                try {
                                    BeanUtils.forceSetPropertyFromString(entity, columnName, cellValue);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onRowFinished(int rowNow) {
                                System.out.printf("row finished: %d%n", rowNow);
                                dao.save(entity);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.printf("the class %s must has a non-arg constructor%n", aClass.getName());
                    }
                }
            }
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
