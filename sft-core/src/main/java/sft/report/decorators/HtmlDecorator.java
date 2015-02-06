package sft.report.decorators;

import sft.DefaultConfiguration;
import sft.decorators.Decorator;
import sft.decorators.DecoratorReportImplementation;
import sft.report.HtmlReport;
import sft.result.FixtureCallResult;
import sft.result.ScenarioResult;
import sft.result.SubUseCaseResult;
import sft.result.UseCaseResult;

import java.util.ArrayList;
import java.util.List;

public class HtmlDecorator implements DecoratorReportImplementation{

    protected final DefaultConfiguration configuration;

    public HtmlDecorator(DefaultConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String... parameters) {
        throw new RuntimeException("Decorator " + getClass().getName() + " not implemented for use case");
    }

    @Override
    public String applyOnFixtures(List<FixtureCallResult> fixtureCallResults, String... parameters) {
        throw new RuntimeException("Decorator " + getClass().getName() + " not implemented for fixtures");
    }

    @Override
    public String applyOnSubUseCases(List<SubUseCaseResult> useCaseResult, String... parameters) {
        throw new RuntimeException("Decorator " + getClass().getName() + " not implemented for sub use cases");
    }

    @Override
    public String applyOnScenarios(List<ScenarioResult> scenarioResults, String... parameters) {
        throw new RuntimeException("Decorator " + getClass().getName() + " not implemented for scenarios");
    }

    protected HtmlReport getHtmlReport() {
        return configuration.getReport(HtmlReport.class);
    }
}
