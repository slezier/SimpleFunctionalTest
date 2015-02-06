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
import sft.*;
import sft.report.HtmlReport;
import sft.result.SoutReport;
import sft.result.UseCaseResult;

import java.util.ArrayList;

public class UseCaseRunner {


    private final ArrayList<SubUseCaseRunner> subUseCasesRunners = new ArrayList<SubUseCaseRunner>();
    private final ArrayList<ScenarioRunner> scenarioRunners = new ArrayList<ScenarioRunner>();
    private final ContextRunner beforeUseCaseRunner;
    private final ContextRunner afterUseCaseRunner;
    private final UseCase useCase;

    public UseCaseRunner(Class<?> klass) throws Exception {
        this(new UseCase(klass));
    }

    public UseCaseRunner(UseCase useCase) throws Exception {
        this.useCase = useCase;
        for (Scenario scenario : useCase.scenarios) {
            scenarioRunners.add(new ScenarioRunner(scenario));
        }
        for (SubUseCase subUseCase : useCase.subUseCases) {
            subUseCasesRunners.add(new SubUseCaseRunner(subUseCase));
        }
        beforeUseCaseRunner = new ContextRunner(this, useCase.beforeUseCase);
        afterUseCaseRunner = new ContextRunner(this, useCase.afterUseCase);
    }

    public Class<?> getClassUnderTest(){
        return this.useCase.classUnderTest;
    }

    public Description getDescription() {
        Description description = Description.createTestDescription(useCase.classUnderTest, useCase.getName());
        for (ScenarioRunner scenarioRunner : scenarioRunners) {
            description.addChild(scenarioRunner.getDescription());
        }
        for (SubUseCaseRunner subUseCaseRunner : subUseCasesRunners) {
            description.addChild(subUseCaseRunner.getDescription());
        }
        return description;
    }

    public UseCaseResult run(SftNotifier notifier) {
        UseCaseResult useCaseResult = new UseCaseResult(this.useCase);

        if (useCase.shouldBeIgnored()) {
            for (ScenarioRunner scenarioRunner : scenarioRunners) {
                useCaseResult.scenarioResults.add(scenarioRunner.ignore());
            }
            notifier.fireUseCaseIgnored(this);
        } else {
            notifier.fireUseCaseStarted(this);
            useCase.helpers.runBeforeUseCase(this,notifier);
            useCaseResult.beforeResult = beforeUseCaseRunner.run(notifier);

            if (useCaseResult.beforeResult.isSuccessful()) {
                for (ScenarioRunner scenarioRunner : scenarioRunners) {
                    useCaseResult.scenarioResults.add(scenarioRunner.run(notifier));
                }
                useCaseResult.afterResult = afterUseCaseRunner.run(notifier);
            } else {
                for (ScenarioRunner scenarioRunner : scenarioRunners) {
                    useCaseResult.scenarioResults.add(scenarioRunner.ignore());
                }
                useCaseResult.afterResult = afterUseCaseRunner.ignore();
            }

            for (SubUseCaseRunner subUseCaseRunner : subUseCasesRunners) {
                useCaseResult.subUseCaseResults.add(subUseCaseRunner.run(notifier));
            }

            useCase.helpers.runAfterUseCase(this,notifier);
            notifier.fireUseCaseFinished(this);
        }
        try {
            for (Report report: useCase.configuration.getReports()) {
                report.report(useCaseResult);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if( useCase.isRoot()){
            new SoutReport().generate(useCaseResult);
        }
        return useCaseResult;
    }


}
