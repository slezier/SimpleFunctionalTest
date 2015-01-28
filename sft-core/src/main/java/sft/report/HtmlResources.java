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
package sft.report;


import sft.DefaultConfiguration;
import sft.report.decorators.HtmlDecorator;
import sft.result.Issue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlResources {

    public final String path;
    private final HtmlReport htmlReport;
    private List<String> filesUsed;
    private DefaultConfiguration configuration;

    public HtmlResources() {
        this(new DefaultConfiguration(), new HtmlReport(new DefaultConfiguration()), HtmlReport.HTML_DEPENDENCIES_FOLDER);
    }

    public HtmlResources(DefaultConfiguration configuration, HtmlReport htmlReport,String htmlDependenciesFolder) {
        this.configuration = configuration;
        this.htmlReport = htmlReport;
        path = htmlDependenciesFolder;
        ensureIsCreated();
    }

    public HtmlResources ensureIsCreated() {
        try {
            filesUsed = htmlReport.getTargetFolder().copyFromResources(path);
        } catch (IOException e) {
            new RuntimeException("The resource: " + path + " is not accessible",e);
        }
        return this;
    }

    public String convertIssue(Issue issue) {

        return configuration.getReport(HtmlReport.class).getIssueConverter(issue);
    }

    public String getIncludeCssDirectives(Class<?> useCaseClass) {
        RelativeHtmlPathResolver pathResolver = new RelativeHtmlPathResolver();
        String callerPath = pathResolver.getPathOf(useCaseClass, ".html");

        String includeCssDirectives = "";
        for (String cssToInclude : getCssToInclude()) {
            includeCssDirectives += "<link rel=\"stylesheet\" href=\"" + pathResolver.getRelativePathToFile(callerPath, cssToInclude) + "\" />\n";
        }
        return includeCssDirectives;
    }

    public String getIncludeJsDirectives(Class<?> useCaseClass) {
        RelativeHtmlPathResolver pathResolver = new RelativeHtmlPathResolver();
        String callerPath = pathResolver.getPathOf(useCaseClass, ".html");

        String includeJsDirectives = "";
        for (String jsToInclude : getJsToInclude()) {
            includeJsDirectives += "<script src=\"" + pathResolver.getRelativePathToFile(callerPath, jsToInclude) + "\"></script>\n";
        }
        return includeJsDirectives;
    }

    private List<String> getCssToInclude() {
        ArrayList<String> cssFiles = new ArrayList<String>();
        for (String file : filesUsed) {
            if (file.endsWith(".css")) {
                cssFiles.add(file);
            }
        }
        return cssFiles;
    }

    private List<String> getJsToInclude() {
        ArrayList<String> jsFiles = new ArrayList<String>();
        for (String file : filesUsed) {
            if (file.endsWith(".js")) {
                jsFiles.add(file);
            }
        }
        return jsFiles;
    }

}
