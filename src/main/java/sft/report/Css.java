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
package sft.report;


import java.io.IOException;

public class Css {

    public static final String CSS_FILE_NAME = "sft.css";
    private FileSystem fileSystem = new FileSystem();

    public void ensureIsCreated() throws IOException {
        if (!fileSystem.targetFileExists( CSS_FILE_NAME)) {
            fileSystem.makeTargetDirs(".");
            fileSystem.copyFileToTargetDir(CSS_FILE_NAME);
            fileSystem.copyFileToTargetDir("success_16.png");
            fileSystem.copyFileToTargetDir("success_24.png");
            fileSystem.copyFileToTargetDir("success_32.png");
            fileSystem.copyFileToTargetDir("failed_16.png");
            fileSystem.copyFileToTargetDir("failed_24.png");
            fileSystem.copyFileToTargetDir("failed_32.png");
            fileSystem.copyFileToTargetDir("ignored_16.png");
            fileSystem.copyFileToTargetDir("ignored_24.png");
            fileSystem.copyFileToTargetDir("ignored_32.png");
        }
    }


    public String convertIssue(Issue issue) {
        return issue.toString().toLowerCase();
    }

    public String getRelativePath(Class<?> useCaseClass) {
        String s = useCaseClass.getCanonicalName();
        int packageLevel = s.split("\\.").length - 1;
        String result = "";
        while (packageLevel > 0) {
            result += "../";
            packageLevel--;

        }
        return result + CSS_FILE_NAME;
    }
}
