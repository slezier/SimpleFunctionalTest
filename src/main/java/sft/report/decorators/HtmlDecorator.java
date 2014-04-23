package sft.report.decorators;

import sft.DefaultConfiguration;
import sft.decorators.Decorator;
import sft.decorators.DecoratorReportImplementation;
import sft.result.FixtureCallResult;
import sft.result.UseCaseResult;

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
    public String applyOnUseCase(UseCaseResult useCaseResult, String result) {
        throw new RuntimeException("Decorator " + getClass().getName()+" not implemented for use case");
    }

    @Override
    public String applyOnScenario(String result) {
        throw new RuntimeException("Decorator " + getClass().getName()+" not implemented for use case");
    }

    @Override
    public String applyOnFixtures(List<String> result, List<FixtureCallResult> fixtureCallResuts) {
        throw new RuntimeException("Decorator " + getClass().getName()+" not implemented for use case");
    }
}
