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
package sft.decorators;


import sft.DefaultConfiguration;
import sft.report.HtmlReport;
import sft.result.FixtureCallResult;
import sft.result.UseCaseResult;

import java.util.List;

public abstract class Decorator  implements DecoratorReportImplementation{

    public final DefaultConfiguration configuration;
    public final String[] parameters;

    public Decorator(DefaultConfiguration configuration, String... parameters){
        this.configuration=configuration;
        this.parameters = parameters;
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String result) {
        throw new RuntimeException(getClass().getName()+" couldn't be applied on use case");
    }

    @Override
    public String applyOnScenario(String result) {
        throw new RuntimeException(getClass().getName()+" couldn't be applied on scenario");
    }

    @Override
    public String applyOnFixtures(List<String> result, List<FixtureCallResult> fixtureCallResuts) {
        throw new RuntimeException(getClass().getName()+" can't be apply on fixture");
    }

    protected DecoratorReportImplementation getImplementation() {
        HtmlReport report = this.configuration.getReport();
        return report.getDecoratorImplementation(this);
    }

    public abstract boolean comply(Decorator other);
}