package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.sheet.annatation.Mapping;
import org.swiftboot.util.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder to build {@link SheetMeta}
 * <pre>
 * By annotated class type:
 * use method  {@code fromAnnotatedClass()} or {@code fromAnnotatedObject()}to
 * build items from an annotated class or it's instance object.
 *
 * By API dynamically:
 * use method {@code itemBuilder()} create a new {@link MetaItemBuilder} to build items.
 * use method {@code sheet()} to indicate a sheet by name or index.
 * use method {@code items()} to collect all items from {@link MetaItemBuilder} to current sheet in builder.
 * </pre>
 *
 * @author swiftech
 * @see MetaItemBuilder
 * @see SheetMeta
 * @see Mapping
 */
public class SheetMetaBuilder {

    /**
     * current sheet id
     */
    private SheetId sheetId;

    /**
     * mapping of sheet id to items.
     */
    private final MetaMap metaMap = new MetaMap();

    /**
     * Class fields of annotated class.
     */
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
            sheetId = SheetId.DEFAULT_SHEET; // be the first sheet (names 'Sheet 1') if not provides
        }
        for (MetaItem item : items) {
            if (item.getArea().getSheetId() == null) {
                item.getArea().setSheetId(sheetId);
            }
            metaMap.addItem(item.getArea().getSheetId(), item);
        }
        return this;
    }

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
     * Use method newItem() to start build an item.
     * Use method key() to add a key to identify the data value and area in sheet.
     * Use method from(), to() or parse() to define area in sheet.
     * Use method onCell() to do directly handling the POI cell object.
     */
    public static class MetaItemBuilder {
        // Translator to translate expression to actual positions in sheet.
        private static final Translator translator = new Translator();

        private final List<MetaItem> items = new ArrayList<>();
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

        public MetaItemBuilder onCell(CellHandler<? extends CellInfo> cellHandler) {
            item.setCellHandler(cellHandler);
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

        /**
         * Merge all cells in area, merge all values into multi-line text and center the text in the merged cell.
         *
         * @return
         */
        public MetaItemBuilder merge(){
            item.setMerge(true);
            return this;
        }

        public List<MetaItem> build() {
            return items;
        }

    }

}
