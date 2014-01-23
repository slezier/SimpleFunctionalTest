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


import org.junit.runner.Description;
import sft.Scenario;
import sft.UseCase;
import sft.report.Issue;
import sft.result.ContextResult;
import sft.result.ScenarioResult;

import java.lang.reflect.InvocationTargetException;

public class ScenarioRunner {
    public final Scenario scenario;
    private final Description description;

    public ScenarioRunner(Scenario scenario) {
        this.scenario = scenario;
        description = Description.createTestDescription(scenario.useCase.classUnderTest, scenario.getName());
    }

    public Description getDescription() {
        return description;
    }

    public ScenarioResult run(JunitSftNotifier runner) {

        if (scenario.shouldBeIgnored()) {
            runner.fireScenarioIgnored(this);
            return ScenarioResult.ignored(scenario);
        } else {
            runner.fireScenarioStarted(this);
            try {

                ContextResult beforeScenarioResult = new ContextRunner(this, scenario.useCase.beforeScenario).run(runner);
                if( beforeScenarioResult.issue== Issue.FAILED){
                    return ScenarioResult.failedBeforeTest(scenario, beforeScenarioResult.exception);
                }


                scenario.run();

                ContextResult afterScenarioResult = new ContextRunner(this, scenario.useCase.afterScenario).run(runner);
                if( afterScenarioResult.issue== Issue.FAILED){
                    return ScenarioResult.failedAfterTest(scenario, afterScenarioResult.exception);
                }

                return ScenarioResult.success(scenario);
            } catch (InvocationTargetException invocationTargetException) {
                runner.fireScenarioFailed(this, invocationTargetException.getTargetException());
                return ScenarioResult.failed(scenario, invocationTargetException.getTargetException());
            }catch (Throwable throwable){
                runner.fireScenarioFailed(this, throwable);
                return ScenarioResult.failed(scenario,throwable);
            }finally {
                runner.fireScenarioFinished(this);
            }
        }
    }

    public ScenarioResult ignore() {
        return ScenarioResult.ignored(scenario);
    }
}
