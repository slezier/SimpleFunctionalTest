package sft;

import sft.environment.ResourceFolder;
import sft.environment.TargetFolder;

public class ProjectFolder {

    public static final String MAVEN_CLASSES_TEST_PATH = "target/test-classes/";
    public static final String MAVEN_SOURCES_TEST_PATH = "src/test/java/";

    private String sourcesTest = MAVEN_SOURCES_TEST_PATH;
    private String classesTest = MAVEN_CLASSES_TEST_PATH;

    public void setSourcesTest(String pathToSourcesTest){
        this.sourcesTest = pathToSourcesTest;
    }

    public void setClassesTest(String pathToClassesTest){
        this.classesTest = pathToClassesTest;
    }

    public String getClassesTestPath() {
        return classesTest;
    }

    public ResourceFolder getResourceFolder() {
        return new ResourceFolder(toProjectPath(), sourcesTest);
    }

    public TargetFolder getTargetFolder(String path ){
        return new TargetFolder(toProjectPath(classesTest), path);
    }

    private static String toProjectPath(String classFolder) {
        String toProjectPath = "";
        for (String folder : classFolder.split("/")) {
            if (!folder.equals(".") && !folder.equals("")) {
                toProjectPath += "../";
            }
        }
        return toProjectPath;
    }


    private String toProjectPath() {
        String toProjectPath = "";
        for (String folder : classesTest.split("/")) {
            if (!folder.equals(".") && !folder.equals("")) {
                toProjectPath += "../";
            }
        }
        return toProjectPath;
    }

    public String getSourcesTestPath() {
        return sourcesTest;
    }
}
