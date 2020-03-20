package org.swiftboot.fileconvert;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.swiftboot.fileconvert.impl.*;
import org.swiftboot.util.constant.FileConstants;

/**
 * @author swiftech
 **/
public class ConvertFactory {

//    private MultiKeyMap<String, Class<? extends Convert>> mapping = new MultiKeyMap<>();
//
//    public void init() {
//        mapping.put("html", "pdf", Html2PdfConvert.class);
//        mapping.put("pdf", )
//    }


    public Convert createConvert(Source source, Target target) {
        if ("html".equals(source.getFileType())) {
            if ("pdf".equals(target.getFileType())) {
                return new Html2PdfConvert();
            }
            else {
                throw new RuntimeException("输出文件格式不支持: " + target.getFileType());
            }
        }
        else if ("pdf".equals(source.getFileType())) {
            // 图片
            if (FileConstants.isImage(target.getFileType())) {
                return new Pdf2ImageConvert();
            }
            else {
                throw new RuntimeException("输出文件格式不支持: " + target.getFileType());
            }
        }
        else if ("docx".equals(source.getFileType())) {
            if (FileConstants.isImage(target.getFileType())) {
                return new Word2ImageConvert();
            }
            else if (FileConstants.isPdf(target.getFileType())) {
                return new WordDocx2PdfConvert();
            }
            else {
                throw new RuntimeException("输出文件格式不支持: " + target.getFileType());
            }
        }
        else if ("jpg".equals(source.getFileType())) {
            return new Image2ImageConvert();
        }
        else {
            throw new RuntimeException("输入文件格式不支持: " + source.getFileType());
        }
    }
}
