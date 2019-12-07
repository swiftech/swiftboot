package org.swiftboot.web.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.util.IdUtils;
import org.swiftboot.util.WordUtils;
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
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * 从 CSV 文件中初始化数据库，文件名对应实体类名，CSV 中的字段对应实体类的属性变量名
 * 初始化执行过程中会对主键或者关键字段（必须是 unique 字段）做重复性检查
 * 要求：
 * <ul>
 *     <li>实体类必须继承 BaseIdEntity，并且以 "Entity" 为后缀命名</li>
 *     <li>包结构必须是
 *     <pre>
 *     model
 *       |- dao
 *       |- entity
 *     </pre>
 *     </li>
 * </ul>
 * 限制：
 * <ul>
 *     <li>不能处理外键关系</li>
 * </ul>
 * @author swiftech
 * @since 1.1
 */
public class Initializer implements ApplicationContextAware {

    private Logger log = LoggerFactory.getLogger(Initializer.class);

    @Resource
    private SwiftBootConfigBean swiftBootConfigBean;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        preAssignIdToAllDataFiles();
        initFromFiles();
    }

    public void preAssignIdToAllDataFiles() {
        File baseDir = new File(swiftBootConfigBean.getModel().getInitBaseDir());
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.out.println("Not exists or not a directory");
            log.warn(String.format("Not exists or not a directory: %s", baseDir.getName()));
        }
        else {
            File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".csv"));
            if (files == null || files.length == 0) {
                System.out.println("No files found to pre-assign ID");
                return;
            }
            for (File file : files) {
                log.info("Pre-assign ID to file: " + file.getName());
                try {
                    StringBuilder outputBuffer = new StringBuilder();
                    new CsvReader().readCsv(new FileInputStream(file), new CsvReaderHandler() {

                        @Override
                        public void onTitle(List<String> titles) {
                            if (titles.contains("id")) {
                                throw new RuntimeException(String.format("The data file %s has already contains id", file.getName()));
                            }
                            outputBuffer.append("id,").append(join(titles, ",")).append(System.getProperty("line.separator"));
                        }

                        @Override
                        public void onRow(int rowNum, List<String> columns) {
                            System.out.printf("handle line %d%n", rowNum);
                            String rawFileName = substringBeforeLast(file.getName(), ".csv");
                            String[] words = splitByCharacterTypeCamelCase(rawFileName);
                            String code = WordUtils.joinWords(words, 8);
                            outputBuffer.append(IdUtils.makeID(code));
                        }

                        @Override
                        public void onCell(int colNum, String columnName, String cellValue) {
                            System.out.printf("handle data: %s=%s%n", columnName, cellValue);
                            outputBuffer.append(",").append(cellValue);
                        }

                        @Override
                        public void onRowFinished(int rowNow) {
                            System.out.printf("row finished: %d%n", rowNow);
                            outputBuffer.append(System.getProperty("line.separator"));
                        }
                    });
                    System.out.println(outputBuffer.toString());
                    FileOutputStream writer = new FileOutputStream(file);
                    writer.write(outputBuffer.toString().getBytes());
                    writer.close();
                } catch (Exception e) {
                    log.info(e.getLocalizedMessage());
                }
            }
        }
    }

    public void initFromFiles() {
        log.info("Init data from files: ");
        File baseDir = new File(swiftBootConfigBean.getModel().getInitBaseDir());
        log.info(baseDir.getPath());
        log.info("Scan entity classes from packages: ");
        String[] pkgs = swiftBootConfigBean.getModel().getInitBaseEntityPackages();
        for (String pkg : pkgs) {
            log.info(pkg);
        }

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
                log.info(String.format("Load data from file: %s", file.getName()));
                String rawFileName = substringBeforeLast(file.getName(), ".csv");
                Class<?> aClass = classDic.get(rawFileName + "Entity");
                log.info("  for entity class: " + aClass);

                if (aClass == null) {
                    log.warn(String.format("Entity class not exist: %sEntity", rawFileName));
                }
                else {
                    try {
                        initOne(file, rawFileName, aClass);

                    } catch (Exception e) {
                        e.printStackTrace();
                        log.warn(String.format("the class %s must has a non-arg constructor%n", aClass.getName()));
                    }
                }
            }
        }
    }

    @Transactional
    void initOne(File file, String rawFileName, Class<?> aClass) throws Exception {
        // try to get dao
        String baseName = substringBeforeLast(aClass.getName(), ".entity.");
        String daoName = String.format("%s.dao.%sDao", baseName, rawFileName);
        Class<?> daoClass = Class.forName(daoName);
        if (daoClass==null){
            log.warn(String.format("Dao class not exist: %s", daoName));
        }
        CrudRepository<BaseIdEntity, String> dao =
                (CrudRepository<BaseIdEntity, String>) this.applicationContext.getBean(daoClass);

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
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
