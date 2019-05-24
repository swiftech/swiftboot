package org.swiftboot.fileconvert;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @author swiftech
 **/
public class FileConverter {

    private ConvertFactory convertFactory = new ConvertFactory();

    public void convert(Source source, Target target) throws ConvertException {

        try {
            System.out.println(source + " -> " + target);
            if (StringUtils.isBlank(source.getFileType()) || StringUtils.isBlank(target.getFileType())) {
                throw new ConvertException("未指定文件类型");
            }
            if (source.getInputStream() == null || source.getInputStream().available() <= 0) {
                throw new ConvertException("输入流不可用");
            }
            if (target.getConvertCallback() == null) {
                throw new ConvertException("回调不可用");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConvertException();
        }

        // Check input stream's content
//        String s = IoUtils.readAllToString(source.getInputStream());
//        System.out.println(StringUtils.abbreviate(s, 100));

        Convert convert = convertFactory.createConvert(source, target);

        convert.convert(source.getInputStream(), target.getConvertCallback());

        try {
            source.getInputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
