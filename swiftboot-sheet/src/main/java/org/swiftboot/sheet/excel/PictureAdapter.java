package org.swiftboot.sheet.excel;

import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.Position;

import java.util.Map;

/**
 * @author allen
 */
public interface PictureAdapter {

    /**
     *
     * @param sheet
     * @return
     */
    Map<Position, Picture> getPictures(Sheet sheet);

    static PictureAdapter createAdapter(String fileType) {
        if (SheetFileType.TYPE_XLS.equals(fileType)) {
            return new HSSFPictureAdapter();
        }
        else if (SheetFileType.TYPE_XLSX.equals(fileType)) {
            return new XSSFPictureAdapter();
        }
        else {
            throw new RuntimeException("Not supported");
        }
    }
}
