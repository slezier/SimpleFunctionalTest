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
import sft.result.FixtureCallResult;
import sft.result.UseCaseResult;

import java.util.List;

public class Breadcrumb extends Decorator {

    public DefaultConfiguration configuration;

    @Override
    public Decorator withParameters(String... parameters) {
        return this;
    }

    @Override
    public Decorator withConfiguration(DefaultConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String result) {
        return this.configuration.getReport().getDecoratorImplementation(this).applyOnUseCase(useCaseResult, result);
    }

    @Override
    public String applyOnScenario(String result) {
        throw new RuntimeException("Breadcrumb can't be apply on scenario");
    }

    @Override
    public String applyOnFixtures(List<String> result, List<FixtureCallResult> fixtureCallResuts) {
        throw new RuntimeException("Breadcrumb can't be apply on fixture");
    }

    @Override
    public boolean comply(Decorator other) {
        return other instanceof  Breadcrumb;
    }

}
