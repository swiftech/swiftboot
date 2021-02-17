package org.swiftboot.util;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author swiftech
 **/
public class BufferedIoUtilsTest {

    public static final String path =
            "https://github.com/swiftech/swiftboot/archive/master.zip";

    @Test
    public void test() {
        try {
            URL url = new URL(path);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            InputStream ins = httpConn.getInputStream();
            Assertions.assertNotNull(ins);
            BufferedIoUtils.writeInputStreamToFile(ins, 1024,
                    createOutputFile("swiftboot-master.zip"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadFileWriteFile() {
        try {
            InputStream ins = ClasspathResourceUtils.openResourceStream("IoUtilsTest.txt");
            Assertions.assertNotNull(ins);
            BufferedIoUtils.writeInputStreamToFile(ins, 1024,
                    createOutputFile("IoUtilsTests.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO
     */
    @Test
    public void testCallback() {
        File outFile = createOutputFile("swiftboot-master.zip");
        try (FileOutputStream fout = new FileOutputStream(outFile)) {
            URL url = new URL(path);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            System.out.println("download " + path);
            InputStream inputStream = httpConn.getInputStream();
            FileOutputStream finalFout = fout;
            BufferedIoUtils.readInputStream(inputStream, 1024,
                    t -> {
                        System.out.println(t.length);
                        finalFout.write(t);
                    });
            System.out.println("complete: " + outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create output file in "Temp" dir in user home dir
     *
     * @param fileUri
     * @return
     */
    protected File createOutputFile(String fileUri) {
        File dir = new File(SystemUtils.getUserHome(), "Temp/swiftboot/");
        File f = new File(dir, fileUri);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        return f;
    }

    protected OutputStream createOutputStream(String fileUri) throws FileNotFoundException {
        File f = createOutputFile(fileUri);
        System.out.println("Prepared to output to file: " + f.toString());
        return new FileOutputStream(f.toString());
    }
}
