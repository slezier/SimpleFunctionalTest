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


import java.lang.reflect.Method;
import java.util.ArrayList;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isProtected;

public class FixturesHolder {
    public final Object object;
    public final Class<?> classUnderTest;
    public final ArrayList<Fixture> fixtures;


    public FixturesHolder(Object object,FixturesVisibility fixturesVisibility) throws Exception {
        this.object = object;
        classUnderTest = object.getClass();
        fixtures = extractFixtures(fixturesVisibility);
    }

    protected ArrayList<Fixture> extractFixtures(FixturesVisibility fixturesVisibility) throws Exception {
        ArrayList<Fixture> fixtures = new ArrayList<Fixture>();
        for (Method method : getSupportMethod(fixturesVisibility)) {
            fixtures.add(new Fixture(method));
        }
        return fixtures;
    }


    private ArrayList<Method> getSupportMethod(FixturesVisibility fixturesVisibility) {
        ArrayList<Method> testMethods = new ArrayList<Method>();
        for (Method method : classUnderTest.getDeclaredMethods()) {
            if( fixturesVisibility == FixturesVisibility.All ){
                testMethods.add(method);
            }else if (isPrivate(method.getModifiers()) || isProtected(method.getModifiers())) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }


    enum FixturesVisibility{
        All,PrivateOrProtectedOnly
    }

}
