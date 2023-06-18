package org.swiftboot.sheet.excel;

import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.swiftboot.sheet.meta.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author allen
 */
public class XSSFPictureAdapter implements PictureAdapter {

    @Override
    public Map<Position, Picture> getPictures(Sheet sheet) {
        Map<Position, Picture> ret = new HashMap<>();
        XSSFSheet xssfSheet = (XSSFSheet) sheet;
        List<XSSFShape> shapes = xssfSheet.getDrawingPatriarch().getShapes();
        for (XSSFShape shape : shapes) {
            XSSFClientAnchor anchor = (XSSFClientAnchor) shape.getAnchor();
            XSSFPicture p = (XSSFPicture) shape;
            ret.put(new Position(anchor.getRow1(), (int) anchor.getCol1()), p);
        }
        return ret;
    }
}
