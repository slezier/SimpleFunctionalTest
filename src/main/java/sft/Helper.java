/*******************************************************************************
 * Copyright (c) 2013 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static java.lang.reflect.Modifier.isPublic;

public class Helper extends FixturesHolder {
    public final Object object;

    public Helper(Object helperObject) {
        object = helperObject;
        fixtures = extractFixtures();

    }


    private ArrayList<Fixture> extractFixtures() {
        ArrayList<Fixture> fixtures = new ArrayList<Fixture>();
        for(Method method : getSupportMethod() ){
            Fixture fixture = new Fixture(method);
            fixtures.add(fixture);
        }
        return fixtures;
    }

    private ArrayList<Method> getSupportMethod() {
        ArrayList<Method> testMethods = new ArrayList<Method>();
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (isPublic(method.getModifiers()) ) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }

}
