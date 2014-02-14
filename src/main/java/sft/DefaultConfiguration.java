/*******************************************************************************
 * Copyright (c) 2013 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft;

import sft.environment.FileSystem;
import sft.environment.ResourceFolder;
import sft.report.HtmlReport;
import sft.report.TargetFolder;

public class DefaultConfiguration {


    private static final String CLASS_FOLDER = "target/test-classes/";
    private static final String TARGET_SFT_RESULT = "target/sft-result/";
    private static final String TO_TEST_SOURCE_DIR = "src/test/java/";

    private HtmlReport htmlReport;
    private FileSystem fileSystem = new FileSystem(CLASS_FOLDER,TO_TEST_SOURCE_DIR, TARGET_SFT_RESULT);

    public DefaultConfiguration(){
        htmlReport = new HtmlReport(this);
    }

    public HtmlReport getReport() {
        return htmlReport;
    }

    public String getSourcePath() {
        return fileSystem.sourceFolder.path;
    }

    public void setSourcePath(String sourcePath) {
        fileSystem  = fileSystem.changeSourcePath(sourcePath);
    }

    public String getClassFolder() {
        return fileSystem.classFolder;
    }
    public void setClassFolder(String classFolder) {
        fileSystem  = fileSystem.changeClassFolder(classFolder);
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }
    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem=fileSystem;
    }


    public ResourceFolder getSourceFolder(){
        return this.fileSystem.sourceFolder;
    }
    public TargetFolder getTargetFolder(){
        return this.fileSystem.targetFolder;
    }
}
