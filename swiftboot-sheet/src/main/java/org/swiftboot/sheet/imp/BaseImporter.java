package org.swiftboot.sheet.imp;

import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.util.BeanUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author allen
 */
public abstract class BaseImporter implements Importer {

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
        SheetMeta meta = new SheetMeta();
        List<Field> fields = meta.fromAnnotatedClass(resultClass);
        Map<String, Object> result = this.importFromStream(templateFileStream, meta);
        for (Field field : fields) {
            BeanUtils.forceSetProperty(ret, field, result.get(field.getName()));
        }
        return ret;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
