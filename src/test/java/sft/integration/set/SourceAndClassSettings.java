package sft.integration.set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.DefaultConfiguration;
import sft.SimpleFunctionalTest;
import sft.Text;

import java.io.File;

/*
SimpleFunctionalTest is sized for maven usage.<br />
If you use another build engine (ant...), you probably want to target other source and class folder.
 */
@RunWith(SimpleFunctionalTest.class)
public class SourceAndClassSettings {
    private static final String TO_PROJECT_PATH = "target/test-classes/../../";
    private DefaultConfiguration defaultConfiguration;
    private String actualClassFolder;

    @Test
    public void changeSourceFolder() throws Exception {
        theDefaultSourceFolderScannedIs("src/test/java/");
        classesWillBeParsedFromThisFolder("sft.integration.set.SourceAndClassSettings", "src/test/java/sft/integration/set/SourceAndClassSettings.java");
        bySettingTheSourceFolderPropertyTo("src/test/other/folder/");
        classesWillBeParsedFromThisFolder("sft.integration.set.SourceAndClassSettings", "src/test/other/folder/sft/integration/set/SourceAndClassSettings.java");
    }

    @Text("The default source folder scanned is ${sourceFolder}")
    private void theDefaultSourceFolderScannedIs(String sourceFolder) {
        defaultConfiguration = new DefaultConfiguration();
        Assert.assertEquals(sourceFolder, defaultConfiguration.getSourcePath());
    }

    @Text("By setting the source folder property to ${sourceFolder}")
    private void bySettingTheSourceFolderPropertyTo(String sourceFolder) {
        defaultConfiguration.setSourcePath(sourceFolder);
    }

    @Text("Simple functional test will load classes from this folder( for example the class ${aClass} will be loaded from ${aPath})")
    private void classesWillBeParsedFromThisFolder(String aClass, String aPath) throws Exception {
        File sourceFile = defaultConfiguration.getSourceFolder().getFileFromClass(Class.forName(aClass), ".java");
        String initialPath = sourceFile.getPath();
        String relativePath = initialPath.substring(initialPath.indexOf(TO_PROJECT_PATH) + TO_PROJECT_PATH.length());
        Assert.assertEquals(aPath, relativePath);
    }

    /*
    Class folder allow to define the original project path
    */
    @Test
    public void changeClassFolder() {
        theDefaultClassFolderIs("target/test-classes/");
        projectFolderIsTargetedWith("../../");
        bySettingTheClassFolderPropertyTo("bin/");
        projectFolderIsTargetedWith("../");
    }

    @Text("The default class folder is ${classFolder}")
    private void theDefaultClassFolderIs(String classFolder) {
        defaultConfiguration = new DefaultConfiguration();
        Assert.assertEquals(classFolder,defaultConfiguration.getClassFolder());
        this.actualClassFolder = classFolder;
    }

    @Text("By setting the class folder property to ${classFolder}")
    private void bySettingTheClassFolderPropertyTo(String classFolder) {
        defaultConfiguration.setClassFolder(classFolder);
    }


    @Text("Project folder is targeted with ${pathToProjectFolder}")
    private void projectFolderIsTargetedWith(String pathToProjectFolder) {
        String sourceFolder =  "src/test/java/sft/integration/set/SourceAndClassSettings.java";
        File sourceFile = defaultConfiguration.getSourceFolder().getFileFromClass(sft.integration.set.SourceAndClassSettings.class, ".java");
        String initialPath = sourceFile.getPath();

        String validationPath = actualClassFolder + pathToProjectFolder + sourceFolder;
        Assert.assertNotEquals("Expecting "+validationPath + " in "+initialPath,-1,initialPath.indexOf(validationPath));
    }

}
