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

public abstract class Decorator {

    protected DefaultConfiguration configuration;
    protected String[] parameters;

    public Decorator withParameters(String... parameters) {
        this.parameters =parameters;
        return this;
    }
    public Decorator withConfiguration(DefaultConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public abstract String applyOnUseCase(UseCaseResult useCaseResult, String result);

    public abstract String applyOnScenario(String result);

    public abstract String applyOnFixtures(List<String> instructions, List<FixtureCallResult> fixtureCallResuts);

    public abstract boolean comply(Decorator other);
}