package sft;

import sft.javalang.parser.HelperJavaParser;
import sft.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Helpers {
    public ArrayList<Helper> helpers = new ArrayList<Helper>();

    public void add(Helper helper) {
        helpers.add(helper);
    }

    public void runBeforeScenario(ScenarioRunner scenarioRunner, SftNotifier runner) {
        for (Helper helper : helpers) {
            new ContextRunner(scenarioRunner,helper.beforeScenario).run(runner);
        }
    }

    public void runAfterScenario(ScenarioRunner scenarioRunner, SftNotifier runner) {
        for (Helper helper : helpers) {
            new ContextRunner(scenarioRunner,helper.afterScenario).run(runner);
        }
    }

    public Fixture getFixture(String methodName) {
        for (Helper fixturesHelper : helpers) {
            for (Fixture fixture : fixturesHelper.fixtures) {
                if (methodName.endsWith("." + fixture.method.getName())) {
                    return fixture;
                }
            }
        }
        return null;
    }

    public ArrayList<String> getDisplayedContext() {
        ArrayList<String> contexts = new ArrayList<String>();

        for (Helper fixturesHelper : helpers) {
            contexts.addAll(fixturesHelper.displayedContext.getText());
        }

        return contexts;
    }

    public void runBeforeUseCase(UseCaseRunner useCaseRunner, SftNotifier notifier) {
        for (Helper helper : helpers) {
            new ContextRunner(useCaseRunner, helper.beforeUseCase).run(notifier);
        }
    }

    public void runAfterUseCase(UseCaseRunner useCaseRunner, SftNotifier notifier) {
        for (Helper helper :  helpers) {
            new ContextRunner(useCaseRunner, helper.afterUseCase).run(notifier);
        }
    }

    public void completeDescriptionFromSource(DefaultConfiguration configuration) throws IOException {
        for (Helper helper : helpers) {
            HelperJavaParser helperJavaParser = new HelperJavaParser(configuration,helper.object);
            helperJavaParser.feed(helper);
        }
    }
}
