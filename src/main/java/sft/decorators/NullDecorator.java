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
import sft.result.UseCaseResult;

import java.util.List;

public class NullDecorator implements Decorator {
    @Override
    public Decorator withParameters(String... parameters) {
        return this;
    }

    @Override
    public Decorator withConfiguration(DefaultConfiguration configuration) {
        return this;
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String result) {
        return result;
    }

    @Override
    public String applyOnScenario(String result) {
        return result;
    }

    @Override
    public String applyOnFixtures(List<String> fixtures) {
        String result ="";
        for (String fixture : fixtures) {
            result+=fixture;
        }
        return result;
    }

    @Override
    public boolean comply(Decorator other) {
        return other.getClass()==NullDecorator.class;
    }
}
