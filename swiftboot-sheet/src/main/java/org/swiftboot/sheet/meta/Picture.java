package org.swiftboot.sheet.meta;

/**
 * Stands for a picture that will be import from or export to Excel sheets.
 *
 * @author swiftech
 */
public class Picture {

    /**
     * Example:
     * Workbook.PICTURE_TYPE_JPEG
     * Workbook.PICTURE_TYPE_PNG
     */
    int poiPictureType;

    /**
     * Data of picture in binary.
     */
    byte[] data;

    /**
     * used for import only.
     */
    String mimeType;

    public Picture(int poiPictureType, byte[] data) {
        this.poiPictureType = poiPictureType;
        this.data = data;
    }

    public int getPoiPictureType() {
        return poiPictureType;
    }

    public Picture setPoiPictureType(int poiPictureType) {
        this.poiPictureType = poiPictureType;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public Picture setData(byte[] data) {
        this.data = data;
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Picture setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }
}
