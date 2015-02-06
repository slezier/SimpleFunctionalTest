package sft.reports.markdown;

import sft.DefaultConfiguration;

public class MarkdownReportConfiguration extends DefaultConfiguration {
    public MarkdownReportConfiguration(){
        super();
        addReport(new MarkdownReport(this));
    }
}