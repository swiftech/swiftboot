package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.sheet.annatation.Mapping;
import org.swiftboot.util.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Builder to build {@link SheetMeta}
 * <pre>
 * By annotated class type:
 * use method  {@code fromAnnotatedClass()} or {@code fromAnnotatedObject()} to
 * build items from an annotated (@{@link Mapping}) class or it's instance object.
 *
 * By API dynamically:
 * use method {@code itemBuilder()} create a new {@link MetaItemBuilder} to build items.
 * use method {@code sheet()} to indicate a sheet by name or index.
 * use method {@code items()} to collect all items from {@link MetaItemBuilder} to current sheet in builder.
 * use method {@code handler()} with a {@link SheetHandler} to access a POI sheet object directly.
 * </pre>
 *
 * @author swiftech
 * @see MetaItemBuilder
 * @see SheetMeta
 * @see Mapping
 */
public class SheetMetaBuilder {

    private final Logger log = LoggerFactory.getLogger(SheetMetaBuilder.class);

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

    /**
     * Identity a sheet by index.
     *
     * @param index
     * @return
     */
    public SheetMetaBuilder sheet(int index) {
        sheetId = new SheetId();
        sheetId.setSheetIndex(index);
        return this;
    }

    /**
     * Identity a sheet by name.
     *
     * @param name Should follow the Excel's sheet name rule, if not, it will be converted by force.
     * @return
     */
    public SheetMetaBuilder sheet(String name) {
        sheetId = new SheetId();
        sheetId.setSheetName(name);
        return this;
    }

    /**
     * Identity a sheet by index or name.
     * The index has higher priority than the name.
     *
     * @param index
     * @param name  Should follow the Excel's sheet name rule, if not, it will be converted by force.
     * @return
     */
    public SheetMetaBuilder sheet(int index, String name) {
        sheetId = new SheetId();
        sheetId.setSheetIndex(index);
        sheetId.setSheetName(name);
        return this;
    }

    /**
     * Specify a handler to access a sheet directly. Only works for Excel.
     *
     * @param sheetHandler
     * @return
     */
    public SheetMetaBuilder handler(SheetHandler<?> sheetHandler) {
        if (sheetId == null) {
            sheetId = SheetId.DEFAULT_SHEET; // be the first sheet (index is 0 and name is 'Sheet 1') if not provides
        }
        this.metaMap.setSheetHandler(this.sheetId, sheetHandler);
        return this;
    }

    /**
     * Create a new {@link MetaItemBuilder}.
     *
     * @return
     */
    public MetaItemBuilder itemBuilder() {
        return new MetaItemBuilder();
    }

    /**
     * Append valid meta items which is built from {@link MetaItemBuilder}.
     * If {@link SheetId} has not been provided, a default one with sheetIndex=0 and sheetName="Sheet 1" will be used.
     * The non-null {@link SheetId} in meta item overrides current {@link SheetId}.
     *
     * @param itemBuilder
     * @return
     */
    public SheetMetaBuilder items(MetaItemBuilder itemBuilder) {
        List<MetaItem> items = itemBuilder.build();
        if (sheetId == null) {
            sheetId = SheetId.DEFAULT_SHEET; // be the first sheet (names 'Sheet 1') if not provides
        }
        for (MetaItem item : items) {
            if (item == null) {
                log.warn("One meta item is null, ignored");
                continue;
            }
            if (item.getArea() == null) {
                log.warn(String.format("Meta item's area is null: %s, ignored", item.getKey()));
                continue;
            }
            if (item.getArea().getSheetId() == null) {
                item.getArea().setSheetId(sheetId);
            }
            metaMap.addItem(item.getArea().getSheetId(), item);
        }
        return this;
    }

    /**
     * Load meta items from an object which has fields with mapping annotation @{@link Mapping}.
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
     * Init meta items from class which has fields with mapping annotation @{@link Mapping}.
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

    /**
     * Enable reading images from Excel sheets.
     *
     * @return
     */
    public SheetMetaBuilder withImages() {
        this.metaMap.setWithImages(true);
        return this;
    }

    /**
     * Converters to convert image data form Excel sheets into any format you want.
     *
     * @param converter
     * @return
     */
    public SheetMetaBuilder imageConverter(Function<Picture, ?> converter) {
        if (sheetId == null) {
            sheetId = SheetId.DEFAULT_SHEET; // be the first sheet (index is 0 and name is 'Sheet 1') if not provides
        }
        this.metaMap.setImageConverter(this.sheetId, converter);
        return this;
    }

    /**
     * Build a sheet meta object for import or export.
     *
     * @return
     */
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
     *
     * @author swiftech
     * @see SheetMetaBuilder
     */
    public static class MetaItemBuilder {
        private final Logger log = LoggerFactory.getLogger(MetaItemBuilder.class);
        // Translator to translate expression to actual positions in sheet.
        private static final Translator translator = new Translator();

        private final List<MetaItem> items = new ArrayList<>();
        private MetaItem item;

        /**
         * Start to build a new item, this method must be called before setup anything.
         *
         * @return
         */
        public MetaItemBuilder newItem() {
            item = new MetaItem();
            items.add(item);
            return this;
        }

        /**
         * Identify the data value and sheet area, not required for export.
         *
         * @param key
         * @return
         */
        public MetaItemBuilder key(String key) {
            item.setKey(key);
            return this;
        }

        /**
         * Value object from import or to export,
         * it could be string, number, time, boolean, array, List or List with List elements ({@code List<List<Object>}).
         *
         * @param value A List is values for cells in line, A List with List elements is values for cells in matrix.
         * @return
         */
        public MetaItemBuilder value(Object value) {
            item.setValue(value);
            return this;
        }

        /**
         * Custom handler to access cells in the area. Only works for Excel.
         *
         * @param cellHandler
         * @return
         */
        public MetaItemBuilder onCell(CellHandler<? extends CellInfo> cellHandler) {
            item.setCellHandler(cellHandler);
            return this;
        }

//        /**
//         *
//         * @param predicate
//         * @param height
//         * @return
//         */
//        public MetaItemBuilder predict(Predicate<? extends CellInfo> predicate, Integer height) {
//            return this.predict(predicate, null, height);
//        }

        /**
         * Predict whether a cell is base one to start, Only works for Import.
         *
         * @param predicate
         * @param width
         * @param height
         * @return
         */
        public MetaItemBuilder predict(Predicate<? extends CellInfo> predicate, Integer width, Integer height) {
            if (item.getArea() != null
                    && (item.getArea().getStartPosition() != null || item.getArea().getEndPosition() != null)) {
                throw new RuntimeException("The predict function can't be used since the area position has been setup");
            }
            this.item.setPredicate(predicate);
            this.item.setArea(new Area(new Dimension(width, height)));
            return this;
        }

        private void checkPredictFunction() {
            if (item.getPredicate() != null) {
                throw new RuntimeException("The area position can't be used since the predict function has been setup");
            }
        }

        /**
         * Create an area by parsing expression.
         *
         * @param expression Expression that defines an area in a sheet.
         * @return
         */
        public MetaItemBuilder parse(String expression) {
            this.checkPredictFunction();
            Area area = translator.toArea(expression);
            item.setArea(area);
            return this;
        }

        /**
         * Set the position where the are starts by expression.
         *
         * @param expression Single position expression
         * @return
         */
        public MetaItemBuilder from(String expression) {
            this.checkPredictFunction();
            Position position = translator.toSinglePosition(expression);
            return from(position);
        }

        /**
         * Set the position where the area starts from.
         *
         * @param startPosition Start position in sheet for the area
         * @return
         */
        public MetaItemBuilder from(Position startPosition) {
            this.checkPredictFunction();
            if (item.getArea() == null) {
                item.setArea(new Area(startPosition));
            }
            item.getArea().setStartPosition(startPosition);
            return this;
        }

        /**
         * Set the position where the area starts.
         *
         * @param rowIdx    row index in sheet
         * @param columnIdx column index in sheet
         * @return
         */
        public MetaItemBuilder from(Integer rowIdx, Integer columnIdx) {
            this.checkPredictFunction();
            if (item.getArea() == null) {
                item.setArea(new Area(new Position(rowIdx, columnIdx)));
            }
            item.getArea().setStartPosition(new Position(rowIdx, columnIdx));
            return this;
        }

        /**
         * Set the position where the area ends by expression
         *
         * @param expression Single position expression
         * @return
         */
        public MetaItemBuilder to(String expression) {
            this.checkPredictFunction();
            Position position = translator.toSinglePosition(expression);
            return to(position);
        }

        /**
         * Set the position where the area ends.
         *
         * @param endPosition End position in sheet for the area
         * @return
         */
        public MetaItemBuilder to(Position endPosition) {
            this.checkPredictFunction();
            if (item.getArea() == null || item.getArea().getStartPosition() == null) {
                return this;
            }
            item.getArea().setEndPosition(endPosition);
            return this;
        }

        /**
         * Set the position where the area ends.
         *
         * @param rowIdx    row index in sheet
         * @param columnIdx column index in sheet
         * @return
         */
        public MetaItemBuilder to(Integer rowIdx, Integer columnIdx) {
            this.checkPredictFunction();
            if (item.getArea() == null || item.getArea().getStartPosition() == null) {
                return this;
            }
            item.getArea().setEndPosition(new Position(rowIdx, columnIdx));
            return this;
        }

        /**
         * Merge all cells in area, merge all values into multi-line text and center the text in the merged cell.
         * it requires providing fixed size area, otherwise it will be ignored.
         *
         * @return
         */
        public MetaItemBuilder merge() {
            item.setMerge(true);
            return this;
        }

        /**
         * Copy cells style from one area selected by expression to the target area like tiling,
         * if the target area has uncertain size, actual size(determined by data size) will be applied.
         * If the target area is larger than copied area, the cells style will be repeated.
         *
         * @param expression the expression should be non-dynamic, otherwise the copy will not be executed.
         * @return
         */
        public MetaItemBuilder copy(String expression) {
            item.setCopyArea(translator.toArea(expression));
            return this;
        }

        /**
         * Copy cells style from one area to the target area like tiling,
         * if the target area has uncertain size, actual size(determined by data size) will be applied.
         * If the target area is larger than copied area, the cells style will be repeated.
         *
         * @param area the area should be non-dynamic, otherwise the copy will not be executed.
         * @return
         */
        public MetaItemBuilder copy(Area area) {
            item.setCopyArea(area);
            return this;
        }

        /**
         * Copy style from one cell to the target area like tiling,
         * if the target area has uncertain size, actual size(determined by data size) will be applied.
         * If the target area is larger than copied area, the cells style will be repeated.
         *
         * @param rowIdx
         * @param colIdx
         * @return
         */
        public MetaItemBuilder copy(int rowIdx, int colIdx) {
            item.setCopyArea(new Area(new Position(rowIdx, colIdx)));
            return this;
        }

        /**
         * Insert rows in range that specified by area before copying styles and set values.
         * if the area has uncertain rows, actual rows(determined by data size) will be applied.
         *
         * @return
         */
        public MetaItemBuilder insert() {
            item.setInsert(true);
            return this;
        }

        /**
         * Insert rows in range that specified by value(size) before copying styles and set values.
         *
         * @return
         */
        public MetaItemBuilder insertByValue() {
            item.setInsert(true);
            item.setInsertByValue(true);
            return this;
        }

        public Translator translator() {
            return translator;
        }

        /**
         * Build all the meta items for all sheets.
         *
         * @return
         */
        public List<MetaItem> build() {
            for (MetaItem metaItem : items) {
                if (metaItem.isMerge() && metaItem.getArea().isDynamic()) {
                    throw new RuntimeException(String.format("You want to merge area %s (%s) but the size of it is uncertain", metaItem.getArea(), metaItem.getKey()));
                }
            }
            return items;
        }

    }

}
