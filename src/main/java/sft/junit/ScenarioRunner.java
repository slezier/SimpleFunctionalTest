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
        ScenarioResult scenarioResult = new ScenarioResult(scenario);

        if (scenario.shouldBeIgnored()) {
            runner.fireScenarioIgnored(this);
            scenarioResult.isIgnored();
        } else {
            runner.fireScenarioStarted(this);


            try {
                new ContextRunner(this,scenario.useCase.beforeScenario).run(runner);

                scenario.run();

                new ContextRunner(this,scenario.useCase.afterScenario).run(runner);

            } catch (InvocationTargetException invocationTargetException) {
                Throwable throwable = invocationTargetException.getTargetException();
                runner.fireScenarioFailed(this, throwable);
                scenarioResult.setFailure(throwable);
            }catch (Throwable throwable){
                runner.fireScenarioFailed(this, throwable);
                scenarioResult.setFailure(throwable);
            }
            runner.fireScenarioFinished(this);
        }
        return scenarioResult;
    }

    public ScenarioResult ignore() {
        ScenarioResult scenarioResult = new ScenarioResult(scenario);
        scenarioResult.isIgnored();
        return scenarioResult;
    }
}
