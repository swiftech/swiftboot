package org.swiftboot.sheet.meta;

/**
 * Stands for a picture that will be export to Excel sheet.
 *
 * @author allen
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

}
