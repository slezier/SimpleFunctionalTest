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
import java.util.ArrayList;
import java.util.List;

public class HtmlResources {

    public static final String HTML_DEPENDENCIES_FOLDER = "sft-html-default";
    private FileSystem fileSystem = new FileSystem();
    private static List<String> filesUsed;

    public HtmlResources ensureIsCreated() throws IOException {
        if (filesUsed == null) {
            filesUsed = fileSystem.targetFolder.copyFromResources(HTML_DEPENDENCIES_FOLDER);
        }
        return this;
    }

    public String convertIssue(Issue issue) {
        return issue.toString().toLowerCase();
    }

    public String getRelativePath(Class<?> useCaseClass) {
        RelativeHtmlPathResolver relativeHtmlPathResolver = new RelativeHtmlPathResolver();
        String callerPath = relativeHtmlPathResolver.getPathOf(useCaseClass, ".html");
        return relativeHtmlPathResolver.getRelativePathToFile(callerPath, HTML_DEPENDENCIES_FOLDER);
    }

    public String getIncludeCssDirectives(Class<?> useCaseClass) {
        RelativeHtmlPathResolver pathResolver = new RelativeHtmlPathResolver();
        String callerPath = pathResolver.getPathOf(useCaseClass, ".html");

        String includeCssDirectives = "";
        for (String cssToInclude : getCssToInclude()) {
            includeCssDirectives += "<link rel=\"stylesheet\" href=\"" + pathResolver.getRelativePathToFile(callerPath,cssToInclude) + "\" />\n";
        }
        return  includeCssDirectives;
    }

    public String getIncludeJsDirectives(Class<?> useCaseClass) {
        RelativeHtmlPathResolver pathResolver = new RelativeHtmlPathResolver();
        String callerPath = pathResolver.getPathOf(useCaseClass, ".html");

        String includeJsDirectives = "";
        for (String jsToInclude : getJsToInclude()) {
            includeJsDirectives += "<script src=\"" + pathResolver.getRelativePathToFile(callerPath,jsToInclude) + "\"></script>\n";
        }
        return  includeJsDirectives;
    }

    public List<String> getCssToInclude() {
        ArrayList<String> cssFiles = new ArrayList<String>();
        for (String file : filesUsed) {
            if( file.endsWith(".css")){
                cssFiles.add(file);
            }
        }
        return  cssFiles;
    }

    public List<String> getJsToInclude() {
        ArrayList<String> jsFiles = new ArrayList<String>();
        for (String file : filesUsed) {
            if( file.endsWith(".js")){
                jsFiles.add(file);
            }
        }
        return  jsFiles;
    }

}
