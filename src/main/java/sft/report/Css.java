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
        if (!fileSystem.targetFolder.getFile(CSS_FILE_NAME).exists()) {
            fileSystem.targetFolder.makeDir(".");
            fileSystem.targetFolder.copyFromResources(CSS_FILE_NAME);
            fileSystem.targetFolder.copyFromResources("success_16.png");
            fileSystem.targetFolder.copyFromResources("success_24.png");
            fileSystem.targetFolder.copyFromResources("success_32.png");
            fileSystem.targetFolder.copyFromResources("failed_16.png");
            fileSystem.targetFolder.copyFromResources("failed_24.png");
            fileSystem.targetFolder.copyFromResources("failed_32.png");
            fileSystem.targetFolder.copyFromResources("ignored_16.png");
            fileSystem.targetFolder.copyFromResources("ignored_24.png");
            fileSystem.targetFolder.copyFromResources("ignored_32.png");
            fileSystem.targetFolder.copyFromResources("essai");
        }
    }

    public String convertIssue(Issue issue) {
        return issue.toString().toLowerCase();
    }

    public String getRelativePath(Class<?> useCaseClass) {
        RelativeHtmlPathResolver relativeHtmlPathResolver = new RelativeHtmlPathResolver();
        String callerPath = relativeHtmlPathResolver.getPathOf(useCaseClass, ".html");
        return relativeHtmlPathResolver.getRelativePathToFile(callerPath, CSS_FILE_NAME);
    }

}
