package org.swiftboot.sheet.imp;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.sheet.annatation.Mapping;

import java.util.List;

/**
 * entity for testing import.
 *
 * @author allen
 */
public class ImportEntity {

    @Mapping("A1")
    private String value1;

    @Mapping("B1")
    private String value2;

    @Mapping("B2:F2")
    private List<String> line;

    @Mapping("B2:C3")
    private List<List<String>> matrix;

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

    public List<List<String>> getMatrix() {
        return matrix;
    }

    public void setMatrix(List<List<String>> matrix) {
        this.matrix = matrix;
    }

    @Override
    public String toString() {
        return "ImportEntity{" +
                "value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                ", line=" + StringUtils.join(line) +
                ", matrix=" + StringUtils.join(matrix) +
                '}';
    }
}
