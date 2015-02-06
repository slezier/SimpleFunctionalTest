package sft.integration.fixtures;

import org.junit.Assert;

import java.io.File;

public class TestFileSystem {

    private final String projectPathUsedByIdea;

    public TestFileSystem(String projectPath) {
        projectPathUsedByIdea = projectPath;
    }

    public File file(String pathFromClass) {
        String subProjectPath = getProjectPath();
        return new File(subProjectPath + pathFromClass);
    }

    public String createFilePathFromClassAndEnsureItExists(Class useCaseClass, String extension) {
        String pathToFile = getProjectPath() + "target/sft-result/" + useCaseClass.getCanonicalName().replace('.', '/') + "." + extension;
        fileExists(pathToFile);
        return pathToFile;
    }

    public void filesExists(String... filesToCheck) {
        String subProjectPath = getProjectPath();
        for (String fileToCheck : filesToCheck) {
            fileExists(subProjectPath + fileToCheck);
        }
    }

    private void fileExists(String pathname) {
        File file = new File(pathname);
        Assert.assertTrue("Missing file " + pathname, file.exists());
    }

    private String getProjectPath() {
        String subProjectPath = "";
        if (new File(".").getAbsolutePath().endsWith("/SimpleFunctionalTest/.")) {
            subProjectPath = projectPathUsedByIdea;
        }
        return subProjectPath;
    }
}