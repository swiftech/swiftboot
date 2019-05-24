package org.swiftboot.fileconvert;

/**
 * @author swiftech
 **/
public class Target {

    private String fileType;

    private ConvertCallback convertCallback;

    public Target() {
    }

    public Target(String fileType) {
        this.fileType = fileType;
    }

    public Target(String fileType, ConvertCallback convertCallback) {
        this.fileType = fileType;
        this.convertCallback = convertCallback;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public ConvertCallback getConvertCallback() {
        return convertCallback;
    }

    public void setConvertCallback(ConvertCallback convertCallback) {
        this.convertCallback = convertCallback;
    }

}
