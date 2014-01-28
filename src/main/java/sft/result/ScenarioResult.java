/*******************************************************************************
 * Copyright (c) 2013 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft.result;

import sft.Fixture;
import sft.MethodCall;
import sft.Scenario;
import sft.report.Issue;

import java.util.List;

public class ScenarioResult {
    public final Scenario scenario;
    public final Throwable failure;
    public final Issue issue ;
    public final List<String> contextToDisplay;
    private final ErrorLocation errorLocation;

    private ScenarioResult(Scenario scenarioByMethodName, Issue issue, Throwable failure,ErrorLocation errorLocation) {
        this.scenario = scenarioByMethodName;
        this.contextToDisplay = scenario.useCase.displayedContext.getText();
        this.issue = issue;
        this.failure = failure;
        this.errorLocation =errorLocation;
    }

    private ScenarioResult(Scenario scenario, Issue issue) {
        this(scenario,issue,null,null);
    }

    public boolean beforeScenarioFailed(){
        return errorLocation==ErrorLocation.before;
    }
    public boolean afterScenarioFailed(){
        return errorLocation==ErrorLocation.after;
    }
    public boolean scenarioFailed(){
        return errorLocation==ErrorLocation.during;
    }

    public Fixture getFailedCall() {
        for (StackTraceElement stackTraceElement : failure.getStackTrace()) {
            String canonicalName = scenario.method.getDeclaringClass().getCanonicalName();
            String className = stackTraceElement.getClassName();
            if(className.equals(canonicalName)){
                Fixture fixture = scenario.useCase.getFixtureByMethodName(stackTraceElement.getMethodName());
                if(fixture != null ){
                    return fixture;
                }
            }
        }
        throw  new RuntimeException("No fixture failed !!!!");
    }

    public int getFailedLine() {
        for (StackTraceElement stackTraceElement : failure.getStackTrace()) {
            if(stackTraceElement.getClassName().equals(scenario.useCase.classUnderTest.getName())){
                if(stackTraceElement.getMethodName().equals(scenario.method.getName())){
                    return stackTraceElement.getLineNumber();
                 }
            }
        }
        return -1;
    }

    public static ScenarioResult failed(Scenario scenario,Throwable throwable) {
        return new ScenarioResult(scenario,Issue.FAILED,throwable,ErrorLocation.during);
    }
    public static ScenarioResult ignored(Scenario scenario) {
        return new ScenarioResult(scenario, Issue.IGNORED, null,null);
    }
    public static ScenarioResult success(Scenario scenario) {
        return new ScenarioResult(scenario, Issue.SUCCEEDED);
    }

    public static ScenarioResult failedBeforeTest(Scenario scenario, Throwable throwable) {
        return new ScenarioResult(scenario,Issue.FAILED,throwable,ErrorLocation.before);
    }

    public static ScenarioResult failedAfterTest(Scenario scenario, Throwable throwable) {
        return new ScenarioResult(scenario,Issue.FAILED,throwable,ErrorLocation.after);
    }

    public boolean failureOccurs(Fixture fixture, MethodCall testFixture) {
        return scenarioFailed() && fixture == getFailedCall() && getFailedLine() == testFixture.line;
    }

    private enum ErrorLocation{ before,during,after}

}
