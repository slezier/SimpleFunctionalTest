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
import sft.Fixture;
import sft.result.UseCaseResult;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Group implements Decorator {

    private String name;
    private DefaultConfiguration configuration;

    @Override
    public Decorator withParameters(String... parameters) {
        if(parameters != null && parameters.length > 0){
            this.name = parameters[0];
        }
        return this;
    }

    @Override
    public Decorator withConfiguration(DefaultConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String result) {
        throw new NotImplementedException();
    }

    @Override
    public String applyOnScenario(String result) {
        throw new NotImplementedException();
    }

    @Override
    public String applyOnFixture(String result) {
        return result;
    }

    @Override
    public boolean comply(Decorator other) {
        return other instanceof  Group && this.toString().equals(other.toString());
    }

    @Override
    public String toString(){
        return "Group("+name+")";
    }
}
