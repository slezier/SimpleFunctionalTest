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
import sft.SubUseCase;
import sft.result.SubUseCaseResult;

public class SubUseCaseRunner {

    private final SubUseCase subUseCase;
    private final UseCaseRunner subUseCaseRunner;

    public SubUseCaseRunner(SubUseCase subUseCase) throws Exception {
        this.subUseCase= subUseCase;
        this.subUseCaseRunner = new UseCaseRunner(subUseCase.subUseCase);
    }


    public Description getDescription() {
        return subUseCaseRunner.getDescription();
    }

    public SubUseCaseResult run(JunitSftNotifier notifier) {
        return new SubUseCaseResult(subUseCase,subUseCaseRunner.run(notifier));
    }
}
