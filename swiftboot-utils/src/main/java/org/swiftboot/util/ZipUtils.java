package org.swiftboot.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author swiftech
 * @since 1.2
 */
public class ZipUtils {

    /**
     * @param ins
     * @param suffix    suffix filter
     * @param recursive includes sub folders
     * @return
     */
    public static List<ZipEntry> searchInZip(InputStream ins, String suffix, boolean recursive) {
        try (BufferedInputStream bis = new BufferedInputStream(ins);
             ZipInputStream zis = new ZipInputStream(bis)) {
            List<ZipEntry> ret = new LinkedList<>();
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                if (!recursive && ze.getName().contains("/")) {
                    continue;
                }
                if (StringUtils.isNotBlank(suffix) && !ze.getName().endsWith(suffix)) {
                    continue;
                }
                ret.add(ze);
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search file entries in zip");
        }
    }

    /**
     * 读取 zip 流并通过 zipBuffer 从缓存中读取每个符合过滤条件的文件
     *
     * @param ins
     * @param recursive
     * @param predicate
     * @param zipBuffer
     * @param oneFileCompleted
     */
    public static void readFileInZip(InputStream ins, boolean recursive,
                                     Predicate<ZipEntry> predicate,
                                     ZipBuffer zipBuffer,
                                     OneFileCompleted oneFileCompleted) {
        try (BufferedInputStream bis = new BufferedInputStream(ins);
             ZipInputStream zis = new ZipInputStream(bis)) {
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            while ((ze = zis.getNextEntry()) != null) {
                if (!recursive && ze.getName().contains("/")) {
                    continue;
                }
                if (!predicate.test(ze)) {
                    continue;
                }
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    zipBuffer.onReadBuffer(ze, ArrayUtils.subarray(buffer, 0, len));
                }
                oneFileCompleted.onCompleted(ze);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read file entries in zip");
        }
    }

    /**
     * 读取 zip 流并通过 zipReader 返回其中符合过滤条件的每个文件内容
     *
     * @param ins
     * @param recursive
     * @param predicate
     * @param zipReader
     */
    public static void readFileInZip(InputStream ins, boolean recursive,
                                     Predicate<ZipEntry> predicate,
                                     ZipReader zipReader) {
        try (BufferedInputStream bis = new BufferedInputStream(ins);
             ZipInputStream zis = new ZipInputStream(bis)) {
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            while ((ze = zis.getNextEntry()) != null) {
                if (!recursive && ze.getName().contains("/")) {
                    continue;
                }
                if (!predicate.test(ze)) {
                    continue;
                }
                int len;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((len = zis.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }
                bos.close();
                ByteArrayInputStream fileIns = new ByteArrayInputStream(bos.toByteArray());
                zipReader.onInputStream(ze, fileIns);
                fileIns.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read file entries in zip");
        }
    }

    /**
     * 读取文件部分内容
     */
    @FunctionalInterface
    public interface ZipBuffer {
        void onReadBuffer(ZipEntry zipEntry, byte[] buffer);
    }

    /**
     * 完整读取文件内容
     */
    @FunctionalInterface
    public interface ZipReader {
        void onInputStream(ZipEntry zipEntry, InputStream ins) throws Exception;
    }

    /**
     * 一个文件读取完成
     */
    @FunctionalInterface
    public interface OneFileCompleted {
        void onCompleted(ZipEntry zipEntry);
    }
}
