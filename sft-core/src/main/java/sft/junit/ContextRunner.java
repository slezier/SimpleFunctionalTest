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

import sft.ContextHandler;
import sft.result.ContextResult;

import java.lang.reflect.InvocationTargetException;

public class ContextRunner {
    private final ContextHandler contextHandler;
    private final UseCaseRunner useCaseRunner;
    private final ScenarioRunner scenarioRunner;

    public ContextRunner(UseCaseRunner useCaseRunner,ContextHandler contextHandler) {
        this.contextHandler = contextHandler;
        this.useCaseRunner = useCaseRunner;
        this.scenarioRunner = null;
    }
    public ContextRunner(ScenarioRunner scenarioRunner,ContextHandler contextHandler) {
        this.contextHandler = contextHandler;
        this.useCaseRunner = null;
        this.scenarioRunner = scenarioRunner;
    }

    public ContextResult run(SftNotifier notifier) {
        try {
            if(contextHandler != null){
                contextHandler.run();
            }
            return ContextResult.success(contextHandler);
        } catch (Throwable e) {
            if(e instanceof InvocationTargetException){
                e = e.getCause();
            }
            if(useCaseRunner==null){
                notifier.fireScenarioContextFailed(scenarioRunner,e);
            } else {
                notifier.fireUseCaseContextFailed(useCaseRunner,e);
            }
            return ContextResult.failed(contextHandler, e);
        }
    }

    public ContextResult ignore() {
        return ContextResult.ignored(contextHandler);
    }
}
