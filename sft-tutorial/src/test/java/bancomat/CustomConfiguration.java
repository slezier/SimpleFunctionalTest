package bancomat;

import sft.DefaultConfiguration;
import sft.plugins.sequenceDiagramPlugin.HtmlSequenceDiagram;
import sft.plugins.sequenceDiagramPlugin.SequenceDiagram;
import sft.report.HtmlReport;

public class CustomConfiguration extends DefaultConfiguration {
    public CustomConfiguration() {
        getReport(HtmlReport.class).addDecorator(SequenceDiagram.class, new HtmlSequenceDiagram(this));
    }
}
