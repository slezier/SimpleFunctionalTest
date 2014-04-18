package sft.integration.fixtures;

import org.junit.Assert;

import java.io.File;

public class TestFileSystem {

    public static void filesExists(String... filesToCheck) {
        for (String fileToCheck : filesToCheck) {
            Assert.assertTrue("Missing file " + fileToCheck, new File(fileToCheck).exists());
        }
    }

    public static String createFilePathFromClassAndEnsureItExists(Class useCaseClass) {
        String pathToFile = "target/sft-result/" + useCaseClass.getCanonicalName().replace('.', '/') + ".html";
        filesExists(pathToFile);
        return pathToFile;
    }
}
