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


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isProtected;

public class FixturesHolder {
    public final Object object;
    public final Class<?> classUnderTest;
    public final ArrayList<Fixture> fixtures;
    public final DefaultConfiguration configuration;
    public final DisplayedContext displayedContext;
    private final Visibility visibility;


    public FixturesHolder(Object object,Visibility visibility,DefaultConfiguration configuration) throws Exception {
        this.configuration = extractConfiguration(object.getClass(),configuration);
        this.object = object;
        classUnderTest = object.getClass();
        this.visibility = visibility;
        fixtures = extractFixtures();
        displayedContext = extractDisplayedContext(object);
    }


    private DisplayedContext extractDisplayedContext(Object object) {
        return new DisplayedContext(object, extractDisplayableFields());
    }


    private ArrayList<Field> extractDisplayableFields() {
        ArrayList<Field> displayableFields = new ArrayList<Field>();
        for (Field field : classUnderTest.getDeclaredFields()) {
            if (field.isAnnotationPresent(Displayable.class)){
                if( visibility == Visibility.All ){
                    displayableFields.add(field);
                }else if (isPrivate(field.getModifiers()) ) {
                    displayableFields.add(field);
                }
            }
        }
        return displayableFields;
    }

    private static DefaultConfiguration extractConfiguration(Class<?> classUnderTest,DefaultConfiguration defaultConfiguration) throws IllegalAccessException, InstantiationException {
        Using explicitConfiguration = classUnderTest.getAnnotation(Using.class);
        if (explicitConfiguration != null) {
            return explicitConfiguration.value().newInstance();

        }
        return defaultConfiguration;
    }

    protected ArrayList<Fixture> extractFixtures() throws Exception {
        ArrayList<Fixture> fixtures = new ArrayList<Fixture>();
        for (Method method : getSupportMethod()) {
            fixtures.add(new Fixture(method,configuration));
        }
        return fixtures;
    }


    private ArrayList<Method> getSupportMethod() {
        ArrayList<Method> testMethods = new ArrayList<Method>();
        for (Method method : classUnderTest.getDeclaredMethods()) {
            if( visibility == Visibility.All ){
                testMethods.add(method);
            }else if (isPrivate(method.getModifiers()) || isProtected(method.getModifiers())) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }


    enum Visibility {
        All,PrivateOrProtectedOnly
    }

}
