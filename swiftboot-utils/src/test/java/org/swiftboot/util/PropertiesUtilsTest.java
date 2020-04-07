package org.swiftboot.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * allen
 */
class PropertiesUtilsTest {

    @Test
    void filterPropertiesByLocale() {
        File[] resourceFiles = ClasspathResourceUtils.getResourceFilesForce(R.class, ".properties");
        if (resourceFiles == null || resourceFiles.length == 0) {
            throw new RuntimeException(String.format("Loading resources aborted, unable to find any resource files with %s", R.class));
        }
        List<File> resourceFileList = Arrays.asList(resourceFiles);
        System.out.println("resourceFileList: " + resourceFileList.size());
        List<File> filesToLoad = PropertiesUtils.filterPropertiesByLocale(resourceFileList,
                new PropertiesUtils.ExtractHandler<File>() {
                    @Override
                    public String getFileName(File file) {
                        return file.getName();
                    }

                    @Override
                    public void onPureFileName(String pureName) {
                        System.out.println("# " + pureName);
                        try {
                            Class<?> resClass = Class.forName(pureName); // file name must be the same with related resource class full name
                            System.out.println(resClass);
                        } catch (ClassNotFoundException e) {
                            System.out.println("Not a information resource file: " + e.getLocalizedMessage());
                        }
                    }
                });
        for (File resourceFile : filesToLoad) {
            System.out.println("Load: " + resourceFile);
        }
    }
}