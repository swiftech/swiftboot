package org.swiftboot.sheet.meta;

import org.swiftboot.sheet.annatation.Mapping;
import org.swiftboot.util.BeanUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Meta for accessing sheet data.
 *
 * @author allen
 */
public class SheetMeta {
    /**
     * meta item -> export value
     */
    private Map<MetaItem, Object> metas = new HashMap<>();

    private final Translator translator = new Translator();

    public SheetMeta() {
    }

    public SheetMeta(Map<MetaItem, Object> metas) {
        this.metas = metas;
    }

    public void fromExpression(String key, String expression) {
        this.metas.put(new MetaItem(key, translator.toArea(expression)), null);
    }

    /**
     * Add new meta item to store value to export to the position which is located by expression.
     *
     * @param key
     * @param expression
     * @param value      null if no value data
     */
    public void fromExpression(String key, String expression, Object value) {
        this.metas.put(new MetaItem(key, translator.toArea(expression)), value);
    }

    /**
     * Init meta items from object which is has fields with mapping annotation.
     *
     * @param object
     * @return
     */
    public List<Field> fromAnnotatedObject(Object object) {
        List<Field> fields = BeanUtils.getDeclaredFieldsByAnnotation(object.getClass(), Mapping.class);
        fields.forEach(field -> {
            Mapping annotation = field.getAnnotation(Mapping.class);
            Object value = BeanUtils.forceGetProperty(object, field);
            fromExpression(field.getName(), annotation.value(), value);
        });
        return fields; // TODO to be refactored
    }

    /**
     * Init meta items from class which is has fields with mapping annotation.
     *
     * @param clazz
     * @return
     */
    public List<Field> fromAnnotatedClass(Class<?> clazz) {
        List<Field> fields = BeanUtils.getDeclaredFieldsByAnnotation(clazz, Mapping.class);
        fields.forEach(field -> {
            Mapping annotation = field.getAnnotation(Mapping.class);
            fromExpression(field.getName(), annotation.value(), null);
        });
        return fields; // TODO to be refactored
    }

    public Object getValue(String key) {
        for (MetaItem metaItem : metas.keySet()) {
            if (metaItem.getKey().equals(key)) {
                return metas.get(metaItem);
            }
        }
        return null;
    }

    /**
     * Accept visitor to visit meta items.
     *
     * @param visitor
     */
    public void accept(MetaVisitor visitor) {
        if (this.metas == null || this.metas.isEmpty()) {
            throw new RuntimeException("Setup meta first");
        }
        for (MetaItem meta : this.metas.keySet()) {
            Area area = meta.getArea();
            Position startingPos = area.getStartPosition();
            if (area.isSingleCell()) {
                visitor.visitSingleCell(meta.getKey(), startingPos);
            }
            else {
                int rowCount = area.rowCount();
                int columnCount = area.columnCount();
                if (area.isLine()) {
                    // Only one row is horizontal
                    if (rowCount == 1) {
                        visitor.visitHorizontalLine(meta.getKey(), startingPos, columnCount);
                    }
                    else if (columnCount == 1) { // Only one column is vertical
                        visitor.visitVerticalLine(meta.getKey(), startingPos, rowCount);
                    }
                }
                else {
                    visitor.visitMatrix(meta.getKey(), startingPos, rowCount, columnCount);
                }
            }
        }
    }

}
