package org.swiftboot.sheet.excel;

import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.sheet.meta.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author allen
 */
public class XSSFPictureAdapter implements PictureAdapter {

    private static final Logger log = LoggerFactory.getLogger(XSSFPictureAdapter.class);

    @Override
    public Map<Position, Picture> getPictures(Sheet sheet) {
        Map<Position, Picture> ret = new HashMap<>();
        XSSFSheet xssfSheet = (XSSFSheet) sheet;
        List<XSSFShape> shapes = xssfSheet.getDrawingPatriarch().getShapes();
        for (XSSFShape shape : shapes) {
            XSSFClientAnchor anchor = (XSSFClientAnchor) shape.getAnchor();
            log.debug(String.format("Try to read picture from row %d col %d", anchor.getRow1(), anchor.getCol1()));
            if (shape instanceof XSSFPicture) {
                XSSFPicture p = (XSSFPicture) shape;
                ret.put(new Position(anchor.getRow1(), (int) anchor.getCol1()), p);
            }
            else {
                log.debug(String.format("This is not a picture object but '%s'", shape.getClass().getSimpleName()));
            }
        }
        return ret;
    }
}
