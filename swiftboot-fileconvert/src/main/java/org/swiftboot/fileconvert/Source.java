package org.swiftboot.fileconvert;

import java.io.InputStream;

/**
 * @author swiftech
 **/
public class Source {

    private String fileType;

    private InputStream inputStream;

    public Source() {
    }

    public Source(String fileType, InputStream inputStream) {
        this.fileType = fileType;
        this.inputStream = inputStream;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public String toString() {
        return "Source{" +
                "fileType='" + fileType + '\'' +
                ", inputStream=" + inputStream +
                '}';
    }
}
