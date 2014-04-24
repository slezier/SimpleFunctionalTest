package sft.report.decorators;

import sft.DefaultConfiguration;
import sft.decorators.Decorator;
import sft.decorators.DecoratorReportImplementation;
import sft.report.HtmlReport;
import sft.result.FixtureCallResult;
import sft.result.ScenarioResult;
import sft.result.UseCaseResult;

import java.io.IOException;
import java.util.List;

public class HtmlDecorator implements DecoratorReportImplementation {

    protected final DefaultConfiguration configuration;
    protected final String[] parameters;

    public HtmlDecorator(DefaultConfiguration configuration, String[] parameters) {
        this.configuration = configuration;
        this.parameters = parameters;
    }

    public HtmlDecorator(Decorator decorator) {
        this(decorator.configuration,decorator.parameters);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult) {
        throw new RuntimeException("Decorator " + getClass().getName()+" not implemented for use case");
    }

    @Override
    public String applyOnScenario(ScenarioResult scenarioResult) {
        throw new RuntimeException("Decorator " + getClass().getName()+" not implemented for use case");
    }

    @Override
    public String applyOnFixtures( List<FixtureCallResult> fixtureCallResuts) {
        throw new RuntimeException("Decorator " + getClass().getName()+" not implemented for use case");
    }

    protected HtmlReport getHtmlReport(){
        return (HtmlReport) configuration.getReport();
    }
}
