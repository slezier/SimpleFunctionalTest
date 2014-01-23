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
import sft.Scenario;
import sft.report.Issue;

import java.util.List;

public class ScenarioResult {
    public final Scenario scenario;
    public final Throwable failure;
    public final Issue issue ;
    public final List<String> contextToDisplay;

    public ScenarioResult(Scenario scenarioByMethodName, Issue issue, Throwable failure) {
        this.scenario = scenarioByMethodName;
        this.contextToDisplay = scenario.useCase.displayedContext.getText();
        this.issue = issue;
        this.failure = failure;
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
        ScenarioResult scenarioResult = new ScenarioResult(scenario,Issue.FAILED,throwable);
        return scenarioResult;
    }
    public static ScenarioResult ignored(Scenario scenario) {
        return new ScenarioResult(scenario, Issue.IGNORED, null);
    }
    public static ScenarioResult success(Scenario scenario) {
        return new ScenarioResult(scenario, Issue.SUCCEEDED, null);
    }
}
