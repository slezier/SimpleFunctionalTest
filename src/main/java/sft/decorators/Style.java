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

public class Style extends Decorator {

    public Style(DefaultConfiguration configuration, String... parameters) {
        super(configuration, parameters);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult){
        return getImplementation().applyOnUseCase(useCaseResult);
    }

    @Override
    public String applyOnScenario(String result){
        return getImplementation().applyOnScenario(result);
    }

    @Override
    public String applyOnFixtures(List<String> fixtures, List<FixtureCallResult> fixtureCallResuts){
        return getImplementation().applyOnFixtures(fixtures,fixtureCallResuts);
    }

    @Override
    public boolean comply(Decorator other) {
        return other instanceof  Style && this.toString().equals(other.toString());
    }

    @Override
    public String toString(){
        String result = "";
        for (String style : parameters) {
            result += " "+style;
        }
        return "Style("+result+")";
    }
}
