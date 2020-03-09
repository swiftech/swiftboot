package org.swiftboot.web.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.util.ClasspathResourceUtils;
import org.swiftboot.util.IdUtils;
import org.swiftboot.util.WordUtils;
import org.swiftboot.web.SwiftBootConfigBean;
import org.swiftboot.web.model.entity.BaseIdEntity;
import org.swiftboot.web.reader.CsvReader;
import org.swiftboot.web.reader.CsvReaderHandler;
import org.swiftboot.web.util.SpringPackageUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

/**
 * [Experimental]
 * 从 CSV 文件中初始化数据库，文件名对应实体类名，CSV 中的字段对应实体类的属性变量名
 * 初始化执行过程中会对主键或者关键字段（必须是 unique 字段）做重复性检查
 * 要求：
 * <ul>
 *     <li>导入的数据文件必须和实体类名称相同（不包含'Entity'）的 csv 文件</li>
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
 *
 * @author swiftech
 * @since 1.1
 */
@Component
public class Initializer{// implements ApplicationContextAware {

    private Logger log = LoggerFactory.getLogger(Initializer.class);

    @Resource
    private SwiftBootConfigBean swiftBootConfigBean;

    private ApplicationContext applicationContext;

    /**
     * 在数据文件中预生成ID， 运行带有一个参数：读取和写入 CSV 文件的目录路径
     *
     * @param args 数据文件所在路径
     */
    public static void main(String[] args) {
        if (args == null || args.length < 1 || StringUtils.isBlank(args[0])) {
            System.out.println("Folder path of data files not provided");
            return;
        }
        String dataFolderPath = args[0];
        Initializer initializer = new Initializer();
        initializer.preAssignIdToAllDataFiles(dataFolderPath);
    }

//    @PostConstruct
    public void init() {
        initFromFiles();
    }

    /**
     * Pre-assign IDs to all data rows before they are imported.
     * Run in command line mode, not the web mode
     *
     * @param baseDirPath
     */
    public void preAssignIdToAllDataFiles(String baseDirPath) {
        System.out.println("Try to pre-assign id to all csv files under: " + baseDirPath);
        File baseDir = new File(baseDirPath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.out.println(String.format("Not exists or not a directory: %s", baseDir.getName()));
        }
        else {
            File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".csv"));
            if (files == null || files.length == 0) {
                System.out.println("No files found to pre-assign ID");
                return;
            }
            for (File file : files) {
                System.out.println("Pre-assign ID to file: " + file.getName());
                try {
                    TextStringBuilder outBuffer = new TextStringBuilder();
                    new CsvReader().readCsv(new FileInputStream(file), new CsvReaderHandler() {
                        String code = null;
                        int columnCount = 0;

                        @Override
                        public void onTitle(List<String> titles) {
                            columnCount = titles.size();
                            if (!titles.contains("id")) {
                                outBuffer.append("\"id\",");
                                columnCount = titles.size() + 1;
                            }

                            outBuffer.append(WordUtils.joinWordsWithPad(titles, ",", "\""));
                            String rawFileName = substringBeforeLast(file.getName(), ".csv");
                            String[] words = splitByCharacterTypeCamelCase(rawFileName);
                            code = WordUtils.joinWords(words, 8);
                        }

                        @Override
                        public void onRow(int rowNum, List<String> columns) {
                            outBuffer.appendNewLine();
                            System.out.printf("handle line %d%n", rowNum);
                            if (columns.size() < columnCount) {
                                outBuffer.append("\"").append(IdUtils.makeID(code)).append("\"").append(",");
                            }
                            else if (StringUtils.isBlank(columns.get(0))) {
                                columns.set(0, IdUtils.makeID(code));
                            }
                            String dataRow = WordUtils.joinWordsWithPad(columns, ",", "\"");
                            System.out.println(dataRow);
                            outBuffer.append(dataRow);
                        }

                        @Override
                        public void onCell(int colNum, String columnName, String cellValue) {
                            System.out.printf("handle data: %s=%s%n", columnName, cellValue);
                        }

                        @Override
                        public void onRowFinished(int rowNow) {
                            System.out.printf("row finished: %d%n", rowNow);
                        }
                    });
                    System.out.println(outBuffer.toString());
                    FileOutputStream writer = new FileOutputStream(file);
                    writer.write(outBuffer.toString().getBytes());
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getLocalizedMessage());
                }
            }
            log.info(String.format("Initialize data from %d files is completed", files.length));
        }
    }

    public void initFromFiles() {
        log.info("Init data from files: ");
        log.info("Scan entity classes from packages: ");
        String[] pkgs = swiftBootConfigBean.getModel().getInitBaseEntityPackages();
        for (String pkg : pkgs) {
            log.info(pkg);
        }

        File[] files = ClasspathResourceUtils.getResourceFiles(swiftBootConfigBean.getModel().getInitBaseDir(), ".csv");

        if (files == null || files.length == 0) {
            log.warn("No files found to init data");
            return;
        }

        Set<Class<?>> entityClasses
                = SpringPackageUtils.scanClasses(
                swiftBootConfigBean.getModel().getInitBaseEntityPackages(), BaseIdEntity.class);

        if (entityClasses.isEmpty()) {
            log.warn("No entity classes found to create instance");
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
                    log.warn(String.format("Make sure the class %s doesn't have a non-arg constructor%n", aClass.getName()));
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
        if (daoClass == null) {
            log.warn(String.format("Dao class not exist: %s", daoName));
            return;
        }
        else {
            log.info("Dao class: " + daoClass);
        }

        Object daoBean = this.applicationContext.getBean(daoClass);

        CrudRepository<BaseIdEntity, String> dao =
                (CrudRepository<BaseIdEntity, String>) daoBean;
        log.info(String.format("Use DAO %s to create entities", dao));

        new CsvReader().readCsv(new FileInputStream(file), new CsvReaderHandler() {
            BaseIdEntity entity;

            @Override
            public void onTitle(List<String> titles) {

            }

            @Override
            public void onRow(int rowNum, List<String> columns) {
                try {
                    entity = (BaseIdEntity) aClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCell(int colNum, String columnName, String cellValue) {
                try {
                    BeanUtils.forceSetPropertyFromString(entity, columnName, cellValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRowFinished(int rowNow) {
                dao.save(entity);
            }
        });
        log.info("Initialize data done.");
    }

//    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("### Application Context in Initializer" + applicationContext);
        this.applicationContext = applicationContext;
    }

}
