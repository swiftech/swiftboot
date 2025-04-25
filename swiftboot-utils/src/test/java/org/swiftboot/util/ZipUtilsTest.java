package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 * @author swiftech
 */
public class ZipUtilsTest {
    private final static Long MILLS_IN_DAY = 86400000L;

    @Test
    public void testSearchInZip() {
        try (InputStream inputStream = StringUtils.class.getProtectionDomain().getCodeSource().getLocation().openStream()) {
            List<ZipEntry> zipEntries = ZipUtils.searchInZip(inputStream, ".properties", true);
            for (ZipEntry ze : zipEntries) {
                System.out.println(System.out.format("File: %s Size: %d Last Modified %s %n",
                        ze.getName(), ze.getSize(),
                        LocalDate.ofEpochDay(ze.getTime() / MILLS_IN_DAY)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
