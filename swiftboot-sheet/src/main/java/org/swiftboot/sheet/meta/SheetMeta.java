package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.StringUtils;
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
    private Map<MetaItem, Object> metaItems = new HashMap<>();

    private final Translator translator = new Translator();

    private boolean isAllowFreeSize = false;

    public SheetMeta() {
    }

    public SheetMeta(Map<MetaItem, Object> metaItems) {
        this.metaItems = metaItems;
    }

    public void fromExpression(String key, String expression) {
        this.metaItems.put(new MetaItem(key, translator.toArea(expression)), null);
    }

    /**
     * Add new meta item to store value to export to the position which is located by expression.
     *
     * @param key
     * @param expression
     * @param value      null if no value data
     */
    public void fromExpression(String key, String expression, Object value) {
        this.metaItems.put(new MetaItem(key, translator.toArea(expression)), value);
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
            String expression = annotation.value();
            if (StringUtils.isBlank(expression)) {
                return;// Ignore
            }
            Object value = BeanUtils.forceGetProperty(object, field);
            fromExpression(field.getName(), expression, value);
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
            String expression = annotation.value();
            if (StringUtils.isBlank(expression)) {
                return;// Ignore
            }
            fromExpression(field.getName(), expression, null);
        });
        return fields; // TODO to be refactored
    }

    /**
     * Get value by key
     *
     * @param key
     * @return
     */
    public Object getValue(String key) {
        for (MetaItem metaItem : metaItems.keySet()) {
            if (metaItem.getKey().equals(key)) {
                return metaItems.get(metaItem);
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
        if (this.metaItems == null || this.metaItems.isEmpty()) {
            throw new RuntimeException("Setup meta first");
        }
        for (MetaItem meta : this.metaItems.keySet()) {
            Area area = meta.getArea();
            Position startingPos = area.getStartPosition();
            System.out.printf("'%s' -> %s%n", meta.getKey(), meta.getArea());
            if (area.isSingleCell()) {
                visitor.visitMetaItem(meta.getKey(), startingPos, 1, 1);
            }
            else {
                Integer rowCount = area.rowCount();
                Integer columnCount = area.columnCount();
                System.out.printf("row count %d, column count %d%n", rowCount, columnCount);
                if (!isAllowFreeSize && (rowCount == null || columnCount == null)) {
                    throw new RuntimeException("Free size expression is not allowed");
                }
                else if (area.isLine()) {
                    // Only one row is horizontal
                    if (rowCount != null && rowCount == 1) {
                        visitor.visitMetaItem(meta.getKey(), startingPos, 1, columnCount);
                    }
                    else if (columnCount != null && columnCount == 1) { // Only one column is vertical
                        visitor.visitMetaItem(meta.getKey(), startingPos, rowCount, 1);
                    }
                }
                else {
                    visitor.visitMetaItem(meta.getKey(), startingPos, rowCount, columnCount);
                }
            }
        }
    }

    /**
     * Find max (end) position from all areas.
     *
     * @return not null
     */
    public Position findMaxPosition() {
        Area overlayedArea = new Area(0, 0, 0, 0);
        for (MetaItem metaItem : metaItems.keySet()) {
            overlayedArea = overlayedArea.overlay(metaItem.getArea());
        }
        System.out.println("Overlayed area: " + overlayedArea);
        return overlayedArea.getEndPosition();
    }

    public boolean isAllowFreeSize() {
        return isAllowFreeSize;
    }

    public void setAllowFreeSize(boolean allowFreeSize) {
        isAllowFreeSize = allowFreeSize;
    }
}
