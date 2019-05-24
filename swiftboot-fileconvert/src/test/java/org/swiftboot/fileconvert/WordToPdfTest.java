package org.swiftboot.fileconvert;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author swiftech
 **/
public class WordToPdfTest extends BaseTest {

    @Test
    public void testDoc() {
        InputStream msdoc = this.getClass().getResourceAsStream("/source.doc");

        try {
            fileConverter.convert(new Source("doc", msdoc), new Target("pdf", new ConvertCallback() {
                @Override
                public OutputStream onPage(int page) throws Exception {
                    return new FileOutputStream(new File(targetDir, "target.pdf"));
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDocx() {
        InputStream msdoc = this.getClass().getResourceAsStream("/source.docx");

        try {
            fileConverter.convert(new Source("doc", msdoc), new Target("pdf", new ConvertCallback() {
                @Override
                public OutputStream onPage(int page) throws Exception {
                    return new FileOutputStream(new File(targetDir, "target.pdf"));
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
