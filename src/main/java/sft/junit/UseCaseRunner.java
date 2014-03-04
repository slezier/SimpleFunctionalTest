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
package sft.junit;


import org.junit.runner.Description;
import sft.Scenario;
import sft.UseCase;
import sft.result.UseCaseResult;

import java.util.ArrayList;

public class UseCaseRunner {


    private final ArrayList<UseCaseRunner> subUseCasesRunners = new ArrayList<UseCaseRunner>();
    private final ArrayList<ScenarioRunner> scenarioRunners = new ArrayList<ScenarioRunner>();
    private final ContextRunner beforeUseCaseRunner;
    private final ContextRunner afterUseCaseRunner;
    private final UseCase useCase;

    public UseCaseRunner(Class<?> klass) throws Exception {
        this( new UseCase(klass) );
    }

    public UseCaseRunner(UseCase useCase) throws Exception {
        this.useCase = useCase;
        for (Scenario scenario : useCase.scenarios) {
            scenarioRunners.add(new ScenarioRunner(scenario));
        }
        for (UseCase subUseCase : useCase.subUseCases) {
            subUseCasesRunners.add(new UseCaseRunner(subUseCase));
        }
        beforeUseCaseRunner = new ContextRunner(this,useCase.beforeUseCase);
        afterUseCaseRunner = new ContextRunner(this,useCase.afterUseCase);
    }

    public Description getDescription() {
        Description description = Description.createTestDescription(useCase.classUnderTest, useCase.getName());
        for (ScenarioRunner scenarioRunner : scenarioRunners) {
            description.addChild(scenarioRunner.getDescription());
        }
        for (UseCaseRunner subUseCaseRunner : subUseCasesRunners) {
            description.addChild(subUseCaseRunner.getDescription());
        }
        return description;
    }

    public UseCaseResult run(JunitSftNotifier notifier) {
        UseCaseResult useCaseResult = new UseCaseResult(this.useCase);

        if (useCase.shouldBeIgnored()) {
            for (ScenarioRunner scenarioRunner : scenarioRunners) {
                useCaseResult.scenarioResults.add(scenarioRunner.ignore());
            }
            notifier.fireUseCaseIgnored(this);
        } else {
            notifier.fireUseCaseStarted(this);
            useCaseResult.beforeResult =  beforeUseCaseRunner.run(notifier);

            if(useCaseResult.beforeResult.isSuccessful()){
                for (ScenarioRunner scenarioRunner : scenarioRunners) {
                    useCaseResult.scenarioResults.add(scenarioRunner.run(notifier));
                }
                useCaseResult.afterResult = afterUseCaseRunner.run(notifier);
            }else{
                for (ScenarioRunner scenarioRunner : scenarioRunners) {
                    useCaseResult.scenarioResults.add(scenarioRunner.ignore());
                }
                useCaseResult.afterResult = afterUseCaseRunner.ignore();
            }

            for (UseCaseRunner subUseCaseRunner : subUseCasesRunners) {
                useCaseResult.subUseCaseResults.add(subUseCaseRunner.run(notifier));
            }
            notifier.fireUseCaseFinished(this);
        }
        try {
            useCase.configuration.getReport().report(useCaseResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return useCaseResult;
    }


}
