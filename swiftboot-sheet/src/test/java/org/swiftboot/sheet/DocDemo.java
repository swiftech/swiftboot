package org.swiftboot.sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.swiftboot.sheet.annatation.Mapping;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.exp.Exporter;
import org.swiftboot.sheet.meta.Picture;
import org.swiftboot.sheet.meta.PictureLoader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author allen
 */
public class DocDemo {

    public static void main(String[] args) {
        FileInputStream templateFileInputStream = null;
        FileOutputStream outputStream = null;

        Exporter exporter = new SwiftBootSheetFactory().createExporter(SheetFileType.TYPE_XLSX);
        SheetEntity exportEntity = new SheetEntity();
        exportEntity.setValue("single cell value");
        exportEntity.setPictureToExport(() -> {
            byte[] bytesPic = null;
            return new Picture(Workbook.PICTURE_TYPE_JPEG, bytesPic);
        });
        try {
            exporter.export(templateFileInputStream, exportEntity, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class SheetEntity {

        @Mapping("B1")
        private String value;

        @Mapping("B2:F2")
        private List<String> line;

        @Mapping("B2:C3")
        private List<List<String>> matrix;

        @Mapping("A1")
        PictureLoader pictureToExport;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<String> getLine() {
            return line;
        }

        public void setLine(List<String> line) {
            this.line = line;
        }

        public List<List<String>> getMatrix() {
            return matrix;
        }

        public void setMatrix(List<List<String>> matrix) {
            this.matrix = matrix;
        }

        public PictureLoader getPictureToExport() {
            return pictureToExport;
        }

        public void setPictureToExport(PictureLoader pictureToExport) {
            this.pictureToExport = pictureToExport;
        }
    }
}
