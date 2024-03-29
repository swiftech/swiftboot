package org.swiftboot.sheet.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.sheet.meta.MetaItem;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.sheet.meta.SheetMetaBuilder;
import org.swiftboot.util.BeanUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author swiftech
 */
public abstract class BaseImporter implements Importer {

    protected Logger log = LoggerFactory.getLogger(BaseImporter.class);

    private String fileType;

    public BaseImporter(String fileType) {
        this.fileType = fileType;
    }

    protected <T> T createResult(Class<T> resultClass) {
        try {
            return resultClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public <T> T importFromStream(InputStream templateFileStream, Class<T> resultClass) throws IOException {
        T ret = this.createResult(resultClass);
        SheetMetaBuilder builder = new SheetMetaBuilder();
        SheetMeta meta = builder.fromAnnotatedClass(resultClass).build();
        Map<String, Object> result = this.importFromStream(templateFileStream, meta);
        for (Field field : builder.getFields()) {
            Object value = result.get(field.getName());
            if (value != null) {
                BeanUtils.forceSetProperty(ret, field, value);
            }
        }
        return ret;
    }

    /**
     * Shrink a matrix which is from import file to its inner data type.
     *
     * @param matrix
     * @param rowCount
     * @param colCount
     * @return
     */
    public static Object shrinkMatrix(List<List<Object>> matrix, Integer rowCount, Integer colCount) {
        if (matrix == null || matrix.isEmpty() || matrix.get(0) == null || matrix.get(0).isEmpty()) {
            return null;
        }
        boolean isHorizontal = rowCount != null && rowCount == 1;
        boolean isVertical = colCount != null && colCount == 1;
        // System.out.println(StringUtils.join(matrix));
        if (isHorizontal && isVertical) {
            return matrix.get(0).get(0);
        }
        else if (isHorizontal) {
            return matrix.get(0);
        }
        else if (isVertical) {
            return matrix.stream().map(rows -> rows.get(0)).collect(Collectors.toList());
        }
        else {
            return matrix;
        }
    }

    protected boolean isStaticWay(MetaItem metaItem) {
        return metaItem.getPredicate() == null;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
