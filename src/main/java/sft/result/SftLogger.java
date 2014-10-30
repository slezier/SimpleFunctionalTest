package sft.result;

import sft.Scenario;
import sft.junit.ScenarioRunner;
import sft.junit.SftNotifier;
import sft.junit.UseCaseRunner;

import java.util.ArrayList;

public class SftLogger implements SftNotifier {

    private ArrayList<Scenario> scenariosFailed  = new ArrayList<Scenario>();
    private ArrayList<Scenario> scenariosSucceeded  = new ArrayList<Scenario>();
    private ArrayList<Scenario> scenariosIgnored  = new ArrayList<Scenario>();


    @Override
    public void fireUseCaseIgnored(UseCaseRunner useCaseRunner) {

    }

    @Override
    public void fireUseCaseStarted(UseCaseRunner useCaseRunner) {

    }

    @Override
    public void fireUseCaseFinished(UseCaseRunner useCaseRunner) {

    }

    @Override
    public void fireUseCaseContextFailed(UseCaseRunner useCaseRunner, Throwable throwable) {

    }

    @Override
    public void fireScenarioIgnored(ScenarioRunner scenario) {
        scenariosIgnored.add(scenario.scenario);
    }

    @Override
    public void fireScenarioStarted(ScenarioRunner scenario) {

    }

    @Override
    public void fireScenarioFailed(ScenarioRunner scenario, Throwable failure) {
        scenariosIgnored.add(scenario.scenario);
    }

    @Override
    public void fireScenarioFinished(ScenarioRunner scenario) {
        scenariosSucceeded.add(scenario.scenario);

    }

    @Override
    public void fireScenarioContextFailed(ScenarioRunner useCaseRunner, Throwable e) {
    }
}
