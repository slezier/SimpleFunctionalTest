/*******************************************************************************
 * Copyright (c) 2013, 2014 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft;

import sft.environment.ResourceFolder;
import sft.report.HtmlReport;
import sft.environment.TargetFolder;

public class DefaultConfiguration {


    private static final String CLASS_FOLDER = "target/test-classes/";
    private static final String TARGET_SFT_RESULT = "target/sft-result/";
    private static final String TO_TEST_SOURCE_DIR = "src/test/java/";

    private String classFolder = CLASS_FOLDER;
    private ResourceFolder sourceFolder = new ResourceFolder(toProjectPath(CLASS_FOLDER), TO_TEST_SOURCE_DIR);
    private TargetFolder targetFolder = new TargetFolder(toProjectPath(CLASS_FOLDER), TARGET_SFT_RESULT);
    private HtmlReport htmlReport;


    public DefaultConfiguration() {
        htmlReport = new HtmlReport(this);
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

    public HtmlReport getReport() {
        return htmlReport;
    }

    public String getSourcePath() {
        return sourceFolder.path;
    }

    public void setSourcePath(String sourcePath) {
        sourceFolder = new ResourceFolder(toProjectPath(classFolder), sourcePath);
    }

    public void setTargetPath(String targetPath) {
        targetFolder = new TargetFolder(toProjectPath(classFolder), targetPath);
    }

    public String getClassFolder() {
        return classFolder;
    }

    public void setClassFolder(String classFolder) {
        sourceFolder = new ResourceFolder(toProjectPath(classFolder), sourceFolder.path);
        targetFolder = new TargetFolder(toProjectPath(classFolder), targetFolder.path);
    }

    public ResourceFolder getSourceFolder() {
        return sourceFolder;
    }

    public TargetFolder getTargetFolder() {
        return targetFolder;
    }
}
