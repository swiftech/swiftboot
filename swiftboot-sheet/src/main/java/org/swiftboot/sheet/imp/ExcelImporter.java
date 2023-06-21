package org.swiftboot.sheet.imp;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.sheet.excel.ExcelCellInfo;
import org.swiftboot.sheet.excel.ExcelSheetInfo;
import org.swiftboot.sheet.excel.PictureAdapter;
import org.swiftboot.sheet.meta.*;
import org.swiftboot.sheet.util.PoiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static org.swiftboot.sheet.util.PoiUtils.getValueFromCell;

/**
 * Importer for Microsoft Excel 97-2003 and 2007
 *
 * @author swiftech
 */
public class ExcelImporter extends BaseImporter {

    private static final Logger log = LoggerFactory.getLogger(ExcelImporter.class);

    private final ThreadLocal<ExcelCellInfo> cellInfo = new ThreadLocal<>();
    private final ThreadLocal<Boolean> foundTarget = new ThreadLocal<>();
    // base position is the position of cell from where the extracting start.
    private final ThreadLocal<Position> basePosition = new ThreadLocal<>();
    private final ThreadLocal<Map<Position, Picture>> pictureMap = new ThreadLocal<>();
    private final ThreadLocal<SheetId> sheetId = new ThreadLocal<>();

    /**
     * @param fileType SheetFileType.TYPE_XLS or SheetFileType.TYPE_XLSX
     */
    public ExcelImporter(String fileType) {
        super(fileType);
    }

    @Override
    public Map<String, Object> importFromStream(InputStream templateFileStream, SheetMeta meta) throws IOException {
        Workbook wb = PoiUtils.initWorkbook(templateFileStream, super.getFileType());
        cellInfo.set(new ExcelCellInfo());
        cellInfo.get().setWorkbook(wb);

        Map<String, Object> ret = new HashMap<>();

        final AtomicReference<Sheet> sheetRef = new AtomicReference<>();
        meta.setAllowFreeSize(true);
        meta.accept(sheetId -> {
            log.debug("Sheet: " + sheetId);
            this.sheetId.set(sheetId);
            sheetRef.set(PoiUtils.getSheet(wb, sheetId.getSheetIndex()));
            if (sheetRef.get() == null) {
                log.warn("No sheet found: " + sheetId);
                return;
            }
            cellInfo.get().setSheet(sheetRef.get());
            // callback to user client to handle the sheet.
            if (meta.getSheetHandler(sheetId) != null) {
                ExcelSheetInfo sheetInfo = new ExcelSheetInfo(wb, sheetRef.get());
                SheetHandler<ExcelSheetInfo> handler = (SheetHandler<ExcelSheetInfo>) meta.getSheetHandler(sheetId);
                handler.onSheet(sheetInfo);
            }
            if (meta.getMetaMap().isWithImages()) {
                PictureAdapter pictureAdapter = PictureAdapter.createAdapter(getFileType());
                pictureMap.set(pictureAdapter.getPictures(sheetRef.get()));
            }
        }, (metaItem, startPos, rowCount, columnCount) -> {
            basePosition.set(isStaticWay(metaItem) ? startPos : null);
            log.trace(String.format("Item: '%s' %s rows:%s cols:%s", metaItem.getKey(), startPos, rowCount, columnCount));
            List<List<Object>> matrix = new ArrayList<>();

            if (rowCount == null) {
                // uncertain rows reading
                int i = 0;
                do {
                    cellInfo.get().setRowIdx(i - startPos.getRow());
                    Row row;
                    if (isStaticWay(metaItem)) {
                        if (i < basePosition.get().getRow()) {
                            i++;
                            continue; // skip for static way.
                        }
                    }
                    else {
                        if (basePosition.get() != null) {
                            break;
                        }
                    }
                    row = sheetRef.get().getRow(i); // retrieve to be detected.
                    List<Object> rowValues = getValuesInRow(meta, metaItem, row, columnCount, (CellHandler<ExcelCellInfo>) metaItem.getCellHandler());
                    if (rowValues == null || rowValues.isEmpty()) {
                        log.debug(String.format("no data of this row: %d", i));
                        break;
                    }
                    matrix.add(rowValues);
                    i++;
                }
                while (true);
            }
            else {
                for (int i = 0; i < sheetRef.get().getPhysicalNumberOfRows(); i++) {
                    cellInfo.get().setRowIdx(i);
                    Row row;
                    if (isStaticWay(metaItem)
                            && (i < basePosition.get().getRow() || i >= (basePosition.get().getRow() + rowCount))) {
                        continue; // skip for static way.
                    }
                    else {
                        if (basePosition.get() != null && i >= (basePosition.get().getRow() + rowCount)) {
                            break;
                        }
                        row = sheetRef.get().getRow(i); // retrieve to be detected for predict way.
                    }
                    if (row == null) {
                        log.warn("No row found at " + i);
                        continue;
                    }
                    List<Object> rowValues = getValuesInRow(meta, metaItem, row, columnCount, (CellHandler<ExcelCellInfo>) metaItem.getCellHandler());
                    if (CollectionUtils.isNotEmpty(rowValues)) matrix.add(rowValues);
                }
            }
            Object value = shrinkMatrix(matrix, rowCount, columnCount);
            ret.put(metaItem.getKey(), value);
        });

        return ret;
    }

    /**
     * Get values from {@code startPos} by {@code columnCount} in {@code row}.
     * TODO need check the anchored picture for empty row validation.
     *
     * @param metaItem
     * @param row
     * @param columnCount
     * @param cellHandler
     * @return
     */
    private List<Object> getValuesInRow(SheetMeta sheetMeta, MetaItem metaItem, Row row, int columnCount, CellHandler<ExcelCellInfo> cellHandler) {
        if (row == null) {
            return null;
        }
        int colCount = Math.max(columnCount, 1);
        List<Object> values = new ArrayList<>();

        boolean isRowEmpty = true;
        for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
            Cell cell = row.getCell(j);
            if (cell == null || cell.getCellType() == CellType.BLANK || StringUtils.isBlank(cell.toString())) {
                isRowEmpty = isRowEmpty && true;
            }
            else {
                isRowEmpty = isRowEmpty && false;
            }
            log.trace(String.format("Get value from [%d,%d] as %s", row.getRowNum(), j, cell.getCellType()));
            Object valueFromCell = getValueFromCell(cell);
            cellInfo.get().setCell(cell);
            cellInfo.get().setValue(valueFromCell);
            if (isNeedPredict(metaItem)) {
                if (!metaItem.getPredicate().test(cellInfo.get())) {
                    continue; // not the anchor cell and keep processing.
                }
                foundTarget.set(true);
                basePosition.set(new Position(row.getRowNum(), j));
            }

            if (j >= basePosition.get().getColumn()
                    && j < basePosition.get().getColumn() + colCount) {
                if (pictureMap.get() != null && pictureMap.get().containsKey(new Position(row.getRowNum(), j))) {
                    Picture picture = pictureMap.get().get(new Position(row.getRowNum(), j));
                    PictureData picData = picture.getPictureData();
                    org.swiftboot.sheet.meta.Picture p = new org.swiftboot.sheet.meta.Picture(picData.getPictureType(), picData.getData());
                    p.setMimeType(picData.getMimeType());
                    Function<org.swiftboot.sheet.meta.Picture, ?> imageConverter = sheetMeta.getMetaMap().getImageConverter(sheetId.get());
                    if (imageConverter != null) {
                        values.add(imageConverter.apply(p));
                    }
                    else {
                        values.add(picData);
                    }
                }
                else {
                    cellInfo.get().setColIdx(j);
                    values.add(valueFromCell);
                    if (cellHandler != null) {
                        cellHandler.onCell(cellInfo.get());
                    }
                }
            }
        }
        if (isRowEmpty) {
            return null;
        }
        return values;
    }

    private boolean isNeedPredict(MetaItem metaItem) {
        return (foundTarget.get() == null || !foundTarget.get())
                && metaItem.getPredicate() != null;
    }

}
