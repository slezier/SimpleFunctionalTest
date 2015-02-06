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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultConfiguration {


    private static final String TARGET_SFT_RESULT = "target/sft-result/";

    private ProjectFolder projectFolder = new ProjectFolder();
    private Map<Class, Report> reports = new HashMap<Class, Report>();


    public DefaultConfiguration() {
        addReport(new HtmlReport(this));
    }

    public String getSourcePath() {
        return projectFolder.getSourcesTestPath();
    }

    public void setSourcePath(String sourcePath) {
        projectFolder.setSourcesTest(sourcePath);
    }

    public String getClassFolder() {
        return projectFolder.getClassesTestPath();
    }

    public void setClassFolder(String classFolder) {
        projectFolder.setClassesTest(classFolder);
        getReport(HtmlReport.class).setReportFolder(TARGET_SFT_RESULT);
    }

    public ResourceFolder getSourceFolder() {
        return getProjectFolder().getResourceFolder();
    }
    public ProjectFolder getProjectFolder() {
        return projectFolder;
    }

    public void addReport(Report report) {
        reports.put(report.getClass(), report);
    }
    public <T extends  Report> T getReport( Class< T > reportClass){
        return (T) reports.get( reportClass );
    }

    public Collection<Report> getReports() {
        return reports.values();
    }
}
