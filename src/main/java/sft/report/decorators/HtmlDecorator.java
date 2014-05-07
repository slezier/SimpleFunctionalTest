package sft.report.decorators;

import sft.DefaultConfiguration;
import sft.decorators.DecoratorReportImplementation;
import sft.report.HtmlReport;
import sft.result.FixtureCallResult;
import sft.result.ScenarioResult;
import sft.result.SubUseCaseResult;
import sft.result.UseCaseResult;

import java.util.List;

public class HtmlDecorator implements DecoratorReportImplementation {

    protected final DefaultConfiguration configuration;

    public HtmlDecorator(DefaultConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String... parameters) {
        throw new RuntimeException("Decorator " + getClass().getName() + " not implemented for use case");
    }

    @Override
    public String applyOnScenario(ScenarioResult scenarioResult, String... parameters) {
        throw new RuntimeException("Decorator " + getClass().getName() + " not implemented for use case");
    }

    @Override
    public String applyOnFixtures(List<FixtureCallResult> fixtureCallResuts, String... parameters) {
        throw new RuntimeException("Decorator " + getClass().getName() + " not implemented for use case");
    }

    @Override
    public String applyOnSubUseCase(List<SubUseCaseResult> useCaseResult, String... parameters) {
        throw new RuntimeException("Decorator " + getClass().getName() + " not implemented for sub use case");
    }

    protected HtmlReport getHtmlReport() {
        return (HtmlReport) configuration.getReport();
    }
}
