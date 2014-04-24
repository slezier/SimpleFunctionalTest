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
import sft.result.ScenarioResult;
import sft.result.UseCaseResult;

import java.util.List;

public class NullDecorator extends Decorator {

    public NullDecorator(DefaultConfiguration configuration, String... parameters) {
        super(configuration, parameters);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult) {
        return getImplementation().applyOnUseCase(useCaseResult);
    }

    @Override
    public String applyOnScenario(ScenarioResult scenarioResult) {
        return getImplementation().applyOnScenario(scenarioResult);
    }

    @Override
    public String applyOnFixtures(List<String> fixtures, List<FixtureCallResult> fixtureCallResuts) {
        return getImplementation().applyOnFixtures(fixtures,fixtureCallResuts);
    }

    @Override
    public boolean comply(Decorator other) {
        return other.getClass()==NullDecorator.class;
    }
}
