package org.swiftboot.util;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author swiftech
 **/
public class BufferedIoUtilsTest {

    public static final String path =
            "https://excellmedia.dl.sourceforge.net/project/swiftgantt/swiftgantt_stable/0.4.0/SwiftGantt_0.4.0_release_withdemo.zip";

    @Test
    public void test() {
        try {
            URL url = new URL(path);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpConn.getInputStream();
            BufferedIoUtils.writeInputStreamToFile(inputStream, 1024,
                    new File(SystemUtils.getUserHome(), "tmp/SwiftGantt_0.4.0_release_withdemo.zip"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadFileWriteFile() {
        try {
            File readFile = new File(SystemUtils.getUserHome(), "password.txt");
            InputStream inputStream = new FileInputStream(readFile);
            BufferedIoUtils.writeInputStreamToFile(inputStream, 1024,
                    new File(SystemUtils.getUserHome(), "tmp/password.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCallback() {
        File outFile = new File(SystemUtils.getUserHome(), "tmp/SwiftGantt_0.4.0_release_withdemo.zip");
        FileOutputStream fout = null;
        try {
            URL url = new URL(path);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            System.out.println("download " + path);
            InputStream inputStream = httpConn.getInputStream();
            fout = new FileOutputStream(outFile);
            FileOutputStream finalFout = fout;
            BufferedIoUtils.readInputStream(inputStream, 1024,
                    t -> {
                        System.out.println(t.length);
                        finalFout.write(t);
                    });
            System.out.println("complete: " + outFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
