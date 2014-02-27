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
package sft;


import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.InitializationError;
import sft.junit.JunitSftNotifier;
import sft.junit.UseCaseRunner;
import sft.report.HtmlReport;
import sft.result.UseCaseResult;

import java.io.IOException;


public class SimpleFunctionalTest extends Runner {

    private static UseCaseRunner rootRunner;



    public SimpleFunctionalTest(Class<?> klass) throws InitializationError, InstantiationException, IllegalAccessException, IOException {
        rootRunner = new UseCaseRunner(klass);
    }

    @Override
    public Description getDescription(){
        return rootRunner.getDescription();
    }

    @Override
    public void run(final RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
        try {
            UseCaseResult useCaseResult = rootRunner.run(new JunitSftNotifier(notifier));
        } catch (AssumptionViolatedException e) {
            testNotifier.fireTestIgnored();
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            testNotifier.addFailure(e);
        }
    }

}
