package sft.integration;

import sft.DefaultConfiguration;
import sft.report.HtmlReport;

public class SftDocumentationConfiguration extends DefaultConfiguration {

    public SftDocumentationConfiguration() {
        getReport(HtmlReport.class).setResourcePath("sft-html-documentation");
    }
}
