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


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isProtected;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

public class FixturesHolder {
    public final Object object;
    public final Class<?> classUnderTest;
    public final ArrayList<Fixture> fixtures;
    public final DefaultConfiguration configuration;
    public final DisplayedContext displayedContext;
    private final Visibility visibility;
    public final ContextHandler beforeUseCase;
    public final ContextHandler afterUseCase;
    public final ContextHandler beforeScenario;
    public final ContextHandler afterScenario;


    public FixturesHolder(Object object,Visibility visibility,DefaultConfiguration configuration) throws Exception {
        classUnderTest = object.getClass();
        this.configuration = extractConfiguration(configuration);
        this.object = object;
        this.visibility = visibility;
        fixtures = extractFixtures();
        displayedContext = extractDisplayedContext(object);
        beforeUseCase = extractBeforeClassContextHandler();
        afterUseCase = extractAfterClassContextHandler();
        beforeScenario = extractBeforeContextHandler();
        afterScenario = extractAfterContextHandler();
    }

    private ContextHandler extractBeforeClassContextHandler() {
        Method method = getBeforeClassMethod();
        if (method == null) {
            return null;
        } else {
            return new ContextHandler(this, method);
        }
    }

    private ContextHandler extractAfterClassContextHandler() {
        Method method = getAfterClassMethod();
        if (method == null) {
            return null;
        } else {
            return new ContextHandler(this, method);
        }
    }

    private ContextHandler extractBeforeContextHandler() {
        Method method = getBeforeMethod();
        if (method == null) {
            return null;
        } else {
            return new ContextHandler(this, method);
        }
    }

    private ContextHandler extractAfterContextHandler() {
        Method method = getAfterMethod();
        if (method == null) {
            return null;
        } else {
            return new ContextHandler(this, method);
        }
    }

    private Method getBeforeClassMethod() {
        for (Method method : classUnderTest.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeClass.class)) {
                assertIsAPublicStaticMethod("BeforeClass", method);
                return method;
            }
        }
        return null;
    }

    private Method getAfterClassMethod() {
        for (Method method : classUnderTest.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AfterClass.class)) {
                assertIsAPublicStaticMethod("AfterClass", method);
                return method;
            }
        }
        return null;
    }

    private Method getBeforeMethod() {
        for (Method method : classUnderTest.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                assertIsAPublicAndNotStaticMethod("Before", method);
                return method;
            }
        }
        return null;
    }

    private Method getAfterMethod() {
        for (Method method : classUnderTest.getDeclaredMethods()) {
            if (method.isAnnotationPresent(After.class)) {
                assertIsAPublicAndNotStaticMethod("After", method);
                return method;
            }
        }
        return null;
    }


    private void assertIsAPublicStaticMethod(String annotation, Method method) {
        int modifiers = method.getModifiers();
        if (isPublic(modifiers) && isStatic(modifiers)) {
            //OK
        } else {
            throw new RuntimeException("Method " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + " annotated with @" + annotation + " should be public and static.");
        }
    }

    private void assertIsAPublicAndNotStaticMethod(String annotation, Method method) {
        int modifiers = method.getModifiers();
        if (isPublic(modifiers) && !isStatic(modifiers)) {
            //OK
        } else {
            throw new RuntimeException("Method " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + " annotated with @" + annotation + " should be public and not static.");
        }
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

    private DefaultConfiguration extractConfiguration(DefaultConfiguration defaultConfiguration) throws IllegalAccessException, InstantiationException {
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
