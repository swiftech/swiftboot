package org.swiftboot.fileconvert;

import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * @author swiftech
 **/
public class HtmlToPdfTest extends BaseTest {

    @Test
    public void test() {
        InputStream html = this.getClass().getResourceAsStream("/source.html");

        try {

            fileConverter.convert(new Source("html", html), new Target("pdf", new ConvertCallback() {
                @Override
                public OutputStream onPage(int page) throws Exception {
                    FileOutputStream out = new FileOutputStream(new File(targetDir, "target.pdf"));
                    return out;
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
