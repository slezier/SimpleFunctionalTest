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

import java.util.ArrayList;
import java.util.List;

public class ScenarioResult {
    public final Scenario scenario;
    private Throwable failure = null;
    public Issue issue = Issue.SUCCEEDED;
    private List<String> contextToDisplay;

    public ScenarioResult(Scenario scenarioByMethodName) {
        this.scenario = scenarioByMethodName;
    }

    public void setFailure(Throwable failure) {
        issue = Issue.FAILED;
        this.failure = failure;
    }

    public void setContextToDisplay(List<String> contextToDisplay){
        this.contextToDisplay = contextToDisplay;
    }

    public List<String> getContextToDisplay(){
        if(contextToDisplay==null){
            return new ArrayList<String>();
        }
        return contextToDisplay;
    }

    public Throwable getFailure() {
        return failure;
    }

    public void isIgnored() {
        issue = Issue.IGNORED;
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
}
