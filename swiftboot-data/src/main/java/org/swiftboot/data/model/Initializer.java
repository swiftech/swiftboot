package org.swiftboot.data.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.swiftboot.data.SwiftBootDataConfigBean;
import org.swiftboot.data.constant.InitDataConstants;
import org.swiftboot.data.model.entity.BaseIdEntity;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.data.reader.CsvReader;
import org.swiftboot.data.reader.CsvReaderHandler;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.util.ClasspathResourceUtils;
import org.swiftboot.util.IdUtils;
import org.swiftboot.util.WordUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

/**
 * 从 CSV 文件中初始化数据库，文件名对应实体类名，CSV 中的字段对应实体类的属性变量名。
 * 游离态的实体会创建新的表记录，非游离态的实体会更新表记录非主键字段。
 * 要求：
 * <ul>
 *     <li>创建 bean 的时候要调用 {@code forEntities()} 或者 {@code forEntity()} 方法添加需要初始化数据的实体类</li>
 *     <li>实体类必须继承 {@link IdPersistable}，并且以 "Entity" 为后缀命名</li>
 *     <li>导入的数据文件必须和实体类名称相同（不包含'Entity'后缀）的 csv 文件</li>
 *     <li>代码包结构必须是：
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
public class Initializer implements ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(Initializer.class);

    @Resource
    private SwiftBootDataConfigBean swiftBootDataConfigBean;

    private ApplicationContext applicationContext;

    @Resource
    protected PlatformTransactionManager txManager;

    @Resource
    private EntityManager entityManager;

    /**
     * Class of entities which need to be initialized.
     * Because we can't load CSV data files in Jar, so we don't know which entities should be initialized,
     * so customize a list of entity class to indicate that.
     * This list should be populated when defining this bean.
     */
    private final List<Class<? extends IdPersistable>> initEntities = new ArrayList<>();

    private final AtomicInteger createdCount = new AtomicInteger(0);
    private final AtomicInteger updatedCount = new AtomicInteger(0);

    @Value("${spring.profiles.active:}")
    private String env;

    /**
     * 在数据文件中预生成ID， 运行带有一个参数：读取和写入 CSV 文件的目录路径
     *
     * @param args 数据文件所在绝对路径
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

    @PostConstruct
    public void init() {
        if (!swiftBootDataConfigBean.getModel().getInitData().isEnabled()) {
            return;
        }
        if (initEntities.isEmpty()) {
            log.warn("init data is enabled, but target entities is not given, please configure them.");
            return;
        }
        initFromFiles();
    }

    /**
     * Run in command line mode, not the web mode
     *
     * @param baseDirPath absolut path.
     */
    public void preAssignIdToAllDataFiles(String baseDirPath) {
        log.info(String.format("Try to pre-assign id to all csv files under: %s", baseDirPath));
        File baseDir = new File(baseDirPath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            log.info(String.format("Not exists or not a directory: %s", baseDir.getName()));
        }
        else {
            File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".csv"));
            if (files == null || files.length == 0) {
                log.info("No files found to pre-assign ID");
                return;
            }
            for (File file : files) {
                log.info("Pre-assign ID to file: " + file.getName());
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
                            log.info(String.format("handle line %d", rowNum));
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
                            log.info(String.format("handle data: %s=%s", columnName, cellValue));
                        }

                        @Override
                        public void onRowFinished(int rowNow) {
                            log.info(String.format("row finished: %d", rowNow));
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
        log.info("Init data from CSV files: ");
        Map<Class<? extends IdPersistable>, InputStream> entityAndFileMapping;
        try {
            entityAndFileMapping = this.loadCsvDataFilesAsStreams();
            if (entityAndFileMapping.isEmpty()) return;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Load CSV data files to init failed.");
            return;
        }
        for (Class<? extends IdPersistable> entityClass : entityAndFileMapping.keySet()) {
            InputStream fileIns = entityAndFileMapping.get(entityClass);
            log.info("Init data for entity: " + entityClass);
            try {
                initOne(fileIns, entityClass);
            } catch (Exception e) {
                e.printStackTrace();
                log.warn(String.format("Init one record failed, make sure the entity class %s does have a non-arg constructor.", entityClass.getName()));
                break;
            }
        }
        log.info(String.format("Initialize data done with %d objects created and %d objects updated", createdCount.get(), updatedCount.get()));
    }

    private Map<Class<? extends IdPersistable>, InputStream> loadCsvDataFilesAsStreams() throws IOException {
        Map<Class<? extends IdPersistable>, InputStream> ret = new HashMap<>();
        for (Class<? extends IdPersistable> initEntityClass : initEntities) {
            String fileName = StringUtils.substringBefore(initEntityClass.getSimpleName(), "Entity");
            InputStream inputStream;
            if (StringUtils.isBlank(env)) {
                inputStream = loadCsvDataFileAsStream(InitDataConstants.DEFAULT_FALLBACK_DIR_NAME, fileName);
            }
            else {
                inputStream = this.loadCsvDataFileAsStream(env, fileName);
            }
            if (inputStream != null) {
                ret.put(initEntityClass, inputStream);
            }
        }
        return ret;
    }

    private InputStream loadCsvDataFileAsStream(String profile, String fileName) throws IOException {
        String dataFileUri = String.format("%s%s/%s.csv", swiftBootDataConfigBean.getModel().getInitData().getBaseDir(),
                profile, fileName);
        log.info("Try to load CSV data file: " + dataFileUri);
        InputStream inputStream = ClasspathResourceUtils.openResourceStream(dataFileUri);
        if (inputStream != null) {
            return inputStream;
        }
        else {
            log.info("No CSV file found to init data");
            if (!InitDataConstants.DEFAULT_FALLBACK_DIR_NAME.equals(profile)
                    && swiftBootDataConfigBean.getModel().getInitData().isFallback()) {
                log.info("fallback to default.");
                return loadCsvDataFileAsStream(InitDataConstants.DEFAULT_FALLBACK_DIR_NAME, fileName);
            }
            return null;
        }
    }

    @Transactional
    void initOne(InputStream fileIns, Class<?> entityClass) throws Exception {
        // try to get dao bean
        String baseName = substringBeforeLast(entityClass.getName(), ".entity.");
        String entityName = StringUtils.substringBefore(entityClass.getSimpleName(), "Entity");
        String daoName = String.format("%s.dao.%sDao", baseName, entityName);

        Class<?> daoClass = Class.forName(daoName);
        log.info("Dao class: " + daoClass);
        Object daoBean = this.applicationContext.getBean(daoClass);
        CrudRepository<? extends IdPersistable, String> dao =
                (CrudRepository<? extends IdPersistable, String>) daoBean;

        TransactionTemplate transactionTemplate = new TransactionTemplate(txManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    AtomicInteger idColIdx = new AtomicInteger(0); // Index of the ID column
                    new CsvReader().readCsv(fileIns, new CsvReaderHandler() {
                        private IdPersistable entity;

                        @Override
                        public void onTitle(List<String> titles) {
                            for (int i = 0; i < titles.size(); i++) {
                                if ("id".equalsIgnoreCase(titles.get(i))) idColIdx.set(i);
                            }
                        }

                        @Override
                        public void onRow(int rowNum, List<String> columns) {
                            String id = columns.get(idColIdx.get());
                            Optional<IdPersistable> byId = (Optional<IdPersistable>) dao.findById(id);
                            if (byId.isPresent()) {
                                entity = byId.get();
                            }
                            else {
                                try {
                                    entity = (BaseIdEntity) entityClass.getDeclaredConstructor().newInstance();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onCell(int colNum, String columnName, String cellValue) {
                            if (StringUtils.isBlank(cellValue)) {
                                return;
                            }
                            try {
                                BeanUtils.forceSetPropertyFromString(entity, columnName, cellValue);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onRowFinished(int rowNow) {
                            // dao.save(entity); directly call doesn't work, use EntityManager instead.
                            if (entityManager.contains(entity)) {
                                updatedCount.incrementAndGet();
                            }
                            else {
                                createdCount.incrementAndGet();
                            }
                            entityManager.persist(entity);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        log.info("Initialize data of one table done.");
    }


    /**
     * Which entity to init data for.
     *
     * @param entity
     */
    public void forEntity(Class<? extends IdPersistable> entity) {
        initEntities.add(entity);
    }

    /**
     * Which entities to init data for.
     *
     * @param entities
     */
    @SafeVarargs
    public final void forEntities(Class<? extends IdPersistable>... entities) {
        Collections.addAll(initEntities, entities);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
