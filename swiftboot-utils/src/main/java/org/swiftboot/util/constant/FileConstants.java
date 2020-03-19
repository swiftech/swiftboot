package org.swiftboot.util.constant;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author swiftech 2019-05-22
 **/
public class FileConstants {

    /**
     * 常用图片格式扩展名，小写表示
     */
    public static final String[] IMAGE_FILES = new String[]{
            "jpg", "jpeg", "png", "gif", "webp"
    };

    public static final String[] EXCEL_FILES = new String[]{
            "xls", "xlsx"
    };

    public static final String[] PDF_FILES = new String[]{
            "pdf"
    };

    public static final String[] WORD_FILES = new String[]{
            "doc", "docx"
    };

    public static boolean isImage(String fileExt) {
        return ArrayUtils.contains(FileConstants.IMAGE_FILES, fileExt.toLowerCase());
    }

    public static boolean isExcel(String fileExt) {
        return ArrayUtils.contains(FileConstants.EXCEL_FILES, fileExt.toLowerCase());
    }

    public static boolean isPdf(String fileExt) {
        return ArrayUtils.contains(FileConstants.PDF_FILES, fileExt.toLowerCase());
    }

    public static boolean isWord(String fileExt) {
        return ArrayUtils.contains(FileConstants.WORD_FILES, fileExt.toLowerCase());
    }
}
