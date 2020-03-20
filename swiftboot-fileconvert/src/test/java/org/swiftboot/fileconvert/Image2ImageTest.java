package org.swiftboot.fileconvert;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author swiftech
 */
public class Image2ImageTest extends BaseTest {

    @Test
    public void test( ){
        InputStream ins = this.getClass().getResourceAsStream("/source.jpg");

        try {

            fileConverter.convert(new Source("jpg", ins), new Target("jpg", new ConvertCallback() {
                @Override
                public OutputStream onPage(int page) throws Exception {
                    FileOutputStream out = new FileOutputStream(new File(targetDir, "target.jpg"));
                    return out;
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
