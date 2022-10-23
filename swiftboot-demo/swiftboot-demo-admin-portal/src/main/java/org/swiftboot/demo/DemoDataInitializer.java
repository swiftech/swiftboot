package org.swiftboot.demo;

import org.swiftboot.data.model.Initializer;

/**
 * @author swiftech
 * @deprecated
 */
public class DemoDataInitializer {


    public static void main(String[] args) {
        String dataFolderPath = null;
        if (args == null || args.length < 1) {
            System.out.println("Folder path of data files not provided");
            return;
        }
        dataFolderPath = args[0];
        Initializer initializer = new Initializer();
        initializer.preAssignIdToAllDataFiles(dataFolderPath);
    }

}
