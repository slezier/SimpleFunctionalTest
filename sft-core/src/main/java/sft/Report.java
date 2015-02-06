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

import sft.decorators.Decorator;
import sft.decorators.DecoratorReportImplementation;
import sft.report.decorators.HtmlDecorator;
import sft.result.ScenarioResult;
import sft.result.UseCaseResult;

import java.util.List;

public abstract class Report {
    protected final DefaultConfiguration configuration;

    public Report (DefaultConfiguration configuration) {
        this.configuration = configuration;
    }
    public abstract DecoratorReportImplementation getDecoratorImplementation(Decorator decorator);

    public abstract void addDecorator(Class<? extends Decorator> decoratorClass, DecoratorReportImplementation decoratorImplementation);

    public abstract void report(UseCaseResult useCaseResult) throws Exception;

}
