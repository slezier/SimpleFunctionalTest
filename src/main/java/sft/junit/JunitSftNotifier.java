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
package sft.junit;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class JunitSftNotifier implements SftNotifier {
    private final RunNotifier runNotifier;
    private final Result result;

    public JunitSftNotifier(RunNotifier runNotifier) {
        this.runNotifier = runNotifier;

        result = new Result();
        this.runNotifier.addFirstListener(result.createListener());
    }


    @Override
    public void fireUseCaseStarted(UseCaseRunner useCaseRunner) {
        runNotifier.fireTestRunStarted(useCaseRunner.getDescription());
    }


    @Override
    public void fireUseCaseFinished(UseCaseRunner useCaseRunner) {
        runNotifier.fireTestRunFinished(result);
    }

    @Override
    public void fireUseCaseIgnored(UseCaseRunner useCaseRunner) {
        runNotifier.fireTestIgnored(useCaseRunner.getDescription());
    }

    @Override
    public void fireUseCaseContextFailed(UseCaseRunner useCaseRunner,Throwable throwable){
        runNotifier.fireTestFailure(new Failure(useCaseRunner.getDescription(), throwable));
    }

    @Override
    public void fireScenarioStarted(ScenarioRunner scenario) {
        runNotifier.fireTestStarted(scenario.getDescription());
    }


    @Override
    public void fireScenarioFailed(ScenarioRunner scenarioRunner, Throwable throwable) {
        runNotifier.fireTestFailure(new Failure(scenarioRunner.getDescription(), throwable));
    }

    @Override
    public void fireScenarioFinished(ScenarioRunner scenario) {
        runNotifier.fireTestFinished(scenario.getDescription());
    }

    @Override
    public void fireScenarioIgnored(ScenarioRunner scenario) {
        runNotifier.fireTestIgnored(scenario.getDescription());
    }

    @Override
    public void fireScenarioContextFailed(ScenarioRunner useCaseRunner, Throwable e) {
        //To change body of created methods use File | Settings | File Templates.
    }
}
