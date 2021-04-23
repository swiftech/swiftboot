package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.sheet.annatation.Mapping;
import org.swiftboot.util.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.swiftboot.sheet.meta.SheetMeta.MetaMap;

/**
 * Builder to build {@link SheetMeta}
 *
 * @author allen
 */
public class SheetMetaBuilder {

    private SheetId sheetId;

    private MetaMap metaMap = new MetaMap();

    private List<Field> fields;

    public SheetMetaBuilder sheet(int index) {
        sheetId = new SheetId();
        sheetId.setSheetIndex(index);
        return this;
    }

    public SheetMetaBuilder sheet(String name) {
        sheetId = new SheetId();
        sheetId.setSheetName(name);
        return this;
    }

    public SheetMetaBuilder sheet(int index, String name) {
        sheetId = new SheetId();
        sheetId.setSheetIndex(index);
        sheetId.setSheetName(name);
        return this;
    }

    public MetaItemBuilder itemBuilder() {
        return new MetaItemBuilder();
    }

    public SheetMetaBuilder items(MetaItemBuilder itemBuilder) {
        List<MetaItem> items = itemBuilder.build();
        if (sheetId == null) {
            sheetId = SheetId.DEFAULT_SHEET;
        }
        for (MetaItem item : items) {
            item.getArea().setSheetId(sheetId);
            metaMap.addItem(this.sheetId, item);
        }
        return this;
    }

//    public SheetMetaBuilder item(MetaItemBuilder itemBuilder) {
//        MetaItem item = itemBuilder.build();
//        if (sheetId == null) {
//            sheetId = SheetId.DEFAULT_SHEET;
//        }
//        item.getArea().setSheetId(sheetId);
//        metaMap.addItem(this.sheetId, item);
//        return this;
//    }


    /**
     * Load meta items from an object which is has fields with mapping annotation.
     * Should be used for export.
     *
     * @param object
     * @return
     */
    public SheetMetaBuilder fromAnnotatedObject(Object object) {
        List<Field> fields = BeanUtils.getDeclaredFieldsByAnnotation(object.getClass(), Mapping.class);
        MetaItemBuilder itemBuilder = itemBuilder();
        fields.forEach(field -> {
            Mapping annotation = field.getAnnotation(Mapping.class);
            String expression = annotation.value();
            if (StringUtils.isBlank(expression)) {
                return; // Ignore
            }
            Object value = BeanUtils.forceGetProperty(object, field);
            itemBuilder.newItem().key(field.getName()).parse(expression).value(value);
        });
        this.items(itemBuilder);
        return this;
    }

    /**
     * Init meta items from class which is has fields with mapping annotation.
     * Should be used for import.
     *
     * @param clazz
     * @return
     */
    public SheetMetaBuilder fromAnnotatedClass(Class<?> clazz) {
        List<Field> fields = BeanUtils.getDeclaredFieldsByAnnotation(clazz, Mapping.class);
        MetaItemBuilder itemBuilder = itemBuilder();
        fields.forEach(field -> {
            Mapping annotation = field.getAnnotation(Mapping.class);
            String expression = annotation.value();
            if (StringUtils.isBlank(expression)) {
                return;// Ignore
            }
            itemBuilder.newItem().key(field.getName()).parse(expression);
        });
        this.items(itemBuilder);
        this.fields = fields;
        return this;
    }

    public SheetMeta build() {
        return new SheetMeta(this.metaMap);
    }

    public List<Field> getFields() {
        return fields;
    }

    /**
     * Builder to build a meta item.
     */
    public static class MetaItemBuilder {
        // Translator to translate expression to actual positions in sheet.
        private static final Translator translator = new Translator();

        private List<MetaItem> items = new ArrayList<>();
        private MetaItem item;

        public MetaItemBuilder newItem() {
            item = new MetaItem();
            items.add(item);
            return this;
        }

        public MetaItemBuilder key(String key) {
            item.setKey(key);
            return this;
        }

        public MetaItemBuilder value(Object value) {
            item.setValue(value);
            return this;
        }

        public MetaItemBuilder parse(String expression) {
            Area area = translator.toArea(expression);
            item.setArea(area);
            return this;
        }

        public MetaItemBuilder from(Position startPosition) {
            if (item.getArea() == null) {
                item.setArea(new Area(startPosition));
            }
            item.getArea().setStartPosition(startPosition);
            return this;
        }

        public MetaItemBuilder from(Integer rowIdx, Integer columnIdx) {
            if (item.getArea() == null) {
                item.setArea(new Area(new Position(rowIdx, columnIdx)));
            }
            item.getArea().setStartPosition(new Position(rowIdx, columnIdx));
            return this;
        }

        public MetaItemBuilder to(Position endPosition) {
            if (item.getArea() == null || item.getArea().getStartPosition() == null) {
                return this;
            }
            item.getArea().setEndPosition(endPosition);
            return this;
        }

        public MetaItemBuilder to(Integer rowIdx, Integer columnIdx) {
            if (item.getArea() == null || item.getArea().getStartPosition() == null) {
                return this;
            }
            item.getArea().setEndPosition(new Position(rowIdx, columnIdx));
            return this;
        }

        public List<MetaItem> build() {
            return items;
        }

    }

}
