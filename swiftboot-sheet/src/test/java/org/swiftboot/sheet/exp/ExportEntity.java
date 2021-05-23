package org.swiftboot.sheet.exp;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.sheet.annatation.Mapping;
import org.swiftboot.sheet.meta.PictureLoader;

import java.util.List;

/**
 * entity for testing export.
 *
 * @author swiftech
 */
public class ExportEntity {

    @Mapping("A1")
    private String value1;

    @Mapping("B1")
    private String value2;

    @Mapping("B2:F2")
    private List<String> line;

    @Mapping("B2:C3")
    private List<List<Object>> matrix;

    @Mapping("G4")
    private PictureLoader pictureToExport;

    @Mapping("$'sheet.2'.B2:C3")
    private List<List<Object>> matrix2;

    @Mapping("$'sheet.3'.G4")
    private PictureLoader pictureToExport2;

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public List<String> getLine() {
        return line;
    }

    public void setLine(List<String> line) {
        this.line = line;
    }

    public List<List<Object>> getMatrix() {
        return matrix;
    }

    public void setMatrix(List<List<Object>> matrix) {
        this.matrix = matrix;
    }

    public PictureLoader getPictureToExport() {
        return pictureToExport;
    }

    public void setPictureToExport(PictureLoader pictureToExport) {
        this.pictureToExport = pictureToExport;
    }

    public List<List<Object>> getMatrix2() {
        return matrix2;
    }

    public void setMatrix2(List<List<Object>> matrix2) {
        this.matrix2 = matrix2;
    }

    public PictureLoader getPictureToExport2() {
        return pictureToExport2;
    }

    public void setPictureToExport2(PictureLoader pictureToExport2) {
        this.pictureToExport2 = pictureToExport2;
    }

    @Override
    public String toString() {
        return "ExportEntity{" +
                "value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                ", line=" + StringUtils.join(line) +
                ", matrix=" + StringUtils.join(matrix) +
                '}';
    }
}
