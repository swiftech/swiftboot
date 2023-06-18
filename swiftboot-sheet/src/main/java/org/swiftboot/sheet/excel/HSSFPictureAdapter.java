package org.swiftboot.sheet.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.swiftboot.sheet.meta.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author allen
 */
public class HSSFPictureAdapter implements PictureAdapter {

    @Override
    public Map<Position, Picture> getPictures(Sheet sheet) {
        Map<Position, Picture> ret = new HashMap<>();
        HSSFSheet hssfSheet = (HSSFSheet) sheet;
        List<HSSFShape> children = hssfSheet.getDrawingPatriarch().getChildren();
        for (HSSFShape child : children) {
            HSSFClientAnchor anchor = (HSSFClientAnchor) child.getAnchor();
            HSSFPicture p = (HSSFPicture) child;
            ret.put(new Position(anchor.getRow1(), (int) anchor.getCol1()), p);
        }
        return ret;
    }
}
