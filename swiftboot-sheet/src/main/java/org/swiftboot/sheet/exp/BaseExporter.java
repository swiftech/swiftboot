package org.swiftboot.sheet.exp;

/**
 * @author allen
 */
public abstract class BaseExporter implements Exporter{

    private String fileType;

    public BaseExporter(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
