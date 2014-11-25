package sft.result;

import java.util.ArrayList;

public class ResultDigest {

    public final ArrayList<ScenarioResult> scenariosSucceeded = new ArrayList<ScenarioResult>();
    public final ArrayList<ScenarioResult> scenariosFailed = new ArrayList<ScenarioResult>();
    public final ArrayList<ScenarioResult> scenariosIgnored = new ArrayList<ScenarioResult>();
    public int nbScenariosOk;

    public ResultDigest(UseCaseResult useCaseResult) {
        extractFrom(useCaseResult);
    }

    private void extractFrom(UseCaseResult useCaseResult) {

        for (ScenarioResult scenarioResult : useCaseResult.scenarioResults) {
            switch (scenarioResult.issue) {
                case FAILED:
                    scenariosFailed.add(scenarioResult);
                    break;
                case IGNORED:
                    scenariosIgnored.add(scenarioResult);
                    break;
                default:
                case SUCCEEDED:
                    scenariosSucceeded.add(scenarioResult);
                    nbScenariosOk++;
            }
        }
        for (SubUseCaseResult subUseCaseResult : useCaseResult.subUseCaseResults) {
            extractFrom(subUseCaseResult.useCaseResult);
        }

    }
}
