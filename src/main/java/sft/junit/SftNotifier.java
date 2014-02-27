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

public interface SftNotifier {

    void fireUseCaseIgnored(UseCaseRunner useCaseRunner);
    void fireUseCaseStarted(UseCaseRunner useCaseRunner);
    void fireUseCaseFinished(UseCaseRunner useCaseRunner);

    void fireUseCaseContextFailed(UseCaseRunner useCaseRunner,Throwable throwable);


    void fireScenarioIgnored(ScenarioRunner scenario);
    void fireScenarioStarted(ScenarioRunner scenario);
    void fireScenarioFailed(ScenarioRunner scenario, Throwable failure);
    void fireScenarioFinished(ScenarioRunner scenario);

    void fireScenarioContextFailed(ScenarioRunner useCaseRunner, Throwable e);
}
