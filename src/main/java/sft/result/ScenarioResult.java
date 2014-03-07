/*******************************************************************************
 * Copyright (c) 2013, 2014 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft.result;

import sft.MethodCall;
import sft.Scenario;

import java.util.ArrayList;
import java.util.List;

public class ScenarioResult {
    public final Scenario scenario;
    public final Throwable failure;
    public final Issue issue;
    public final ArrayList<FixtureCallResult> fixtureCallResults;
    public final List<String> contextToDisplay;
    private final ErrorLocation errorLocation;

    private ScenarioResult(Scenario scenarioByMethodName, Issue issue, Throwable failure, ErrorLocation errorLocation) {
        this.scenario = scenarioByMethodName;
        this.contextToDisplay = scenario.useCase.displayedContext.getText();
        this.issue = issue;
        this.failure = failure;
        this.errorLocation = errorLocation;
        this.fixtureCallResults = generateFixtureResults();
    }

    private ScenarioResult(Scenario scenario, Issue issue) {
        this(scenario, issue, null, null);
    }

    public static ScenarioResult failed(Scenario scenario, Throwable throwable) {
        return new ScenarioResult(scenario, Issue.FAILED, throwable, ErrorLocation.during);
    }

    public static ScenarioResult ignored(Scenario scenario) {
        return new ScenarioResult(scenario, Issue.IGNORED, null, null);
    }

    public static ScenarioResult success(Scenario scenario) {
        return new ScenarioResult(scenario, Issue.SUCCEEDED);
    }

    public static ScenarioResult failedBeforeTest(Scenario scenario, Throwable throwable) {
        return new ScenarioResult(scenario, Issue.FAILED, throwable, ErrorLocation.before);
    }

    public static ScenarioResult failedAfterTest(Scenario scenario, Throwable throwable) {
        return new ScenarioResult(scenario, Issue.FAILED, throwable, ErrorLocation.after);
    }

    private ArrayList<FixtureCallResult> generateFixtureResults() {
        final ArrayList<FixtureCallResult> fixtureCallResults = new ArrayList<FixtureCallResult>();

        for (MethodCall methodCall : scenario.methodCalls) {
            Issue methodCallIssue = getIssueOf(methodCall);
            fixtureCallResults.add(new FixtureCallResult(methodCall, methodCallIssue));
        }

        return fixtureCallResults;
    }

    private Issue getIssueOf(MethodCall methodCall) {
        Issue methodIssue = resolveIssueWithUseCaseIssue();
        if (methodIssue != null) {
            return methodIssue;
        }
        methodIssue = resolveIssueWithErrorLocation();
        if (methodIssue != null) {
            return methodIssue;
        }

        return resolveIssueWithLineError(methodCall);
    }

    private Issue resolveIssueWithErrorLocation() {
        switch (errorLocation) {
            case before:
                return Issue.IGNORED;
            case after:
                return Issue.SUCCEEDED;
            default:
            case during:
                return null;

        }
    }

    private Issue resolveIssueWithUseCaseIssue() {
        switch (issue) {
            case SUCCEEDED:
                return Issue.SUCCEEDED;
            case IGNORED:
                return Issue.IGNORED;
            default:
            case FAILED:
                return null;
        }
    }

    private Issue resolveIssueWithLineError(MethodCall methodCall) {
        if (getFailedLine() > methodCall.line) {
            return Issue.SUCCEEDED;
        } else if (getFailedLine() == methodCall.line) {
            return Issue.FAILED;
        } else {
            return Issue.IGNORED;
        }
    }

    public int getFailedLine() {
        for (StackTraceElement stackTraceElement : failure.getStackTrace()) {
            if (stackTraceElement.getClassName().equals(scenario.useCase.classUnderTest.getName())) {
                if (stackTraceElement.getMethodName().equals(scenario.method.getName())) {
                    return stackTraceElement.getLineNumber();
                }
            }
        }
        return -1;
    }

    private enum ErrorLocation {before, during, after}

}
