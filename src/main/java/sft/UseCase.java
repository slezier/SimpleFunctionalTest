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
import org.junit.Ignore;
import org.junit.Test;
import sft.decorators.Decorator;
import sft.decorators.DecoratorExtractor;
import sft.javalang.JavaToHumanTranslator;
import sft.javalang.parser.UseCaseJavaParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

public class UseCase extends FixturesHolder {


    public final UseCase parent;
    public final Decorator useCaseDecorator;
    public final ArrayList<Scenario> scenarios;
    public final ArrayList<SubUseCase> subUseCases;
    public final ArrayList<Helper> fixturesHelpers;
    public final ContextHandler beforeUseCase;
    public final ContextHandler afterUseCase;
    public final ContextHandler beforeScenario;
    public final ContextHandler afterScenario;
    public final DisplayedContext displayedContext;
    private final JavaToHumanTranslator javaToHumanTranslator;
    private String comment;


    public UseCase(Class<?> classUnderTest) throws Exception {
        this(null,classUnderTest.newInstance(), new DefaultConfiguration());
    }


    public UseCase(UseCase parent,Class<?> classUnderTest) throws Exception {
        this(parent, classUnderTest.newInstance(), new DefaultConfiguration());
    }

    public UseCase(UseCase parent, Object objectUnderTest, DefaultConfiguration configuration) throws Exception {
        super(objectUnderTest, FixturesVisibility.PrivateOrProtectedOnly,configuration);
        this.parent = parent;
        javaToHumanTranslator = new JavaToHumanTranslator();
        useCaseDecorator = DecoratorExtractor.getDecorator(this.configuration,classUnderTest.getDeclaredAnnotations());
        scenarios = extractScenarios();
        subUseCases = extractSubUseCases();
        fixturesHelpers = extractFixturesHelpers();
        beforeUseCase = extractBeforeClassContextHandler();
        afterUseCase = extractAfterClassContextHandler();
        beforeScenario = extractBeforeContextHandler();
        afterScenario = extractAfterContextHandler();
        displayedContext = extractDisplayedContext(objectUnderTest);
        UseCaseJavaParser javaClassParser = new UseCaseJavaParser(configuration, classUnderTest);
        javaClassParser.feed(this);
    }


    private DisplayedContext extractDisplayedContext(Object object) {
        return new DisplayedContext(object, extractDisplayableFields());
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

    private ArrayList<Scenario> extractScenarios() throws Exception {
        ArrayList<Scenario> scenarios = new ArrayList<Scenario>();
        for (Method method : getTestMethods()) {
            scenarios.add(new Scenario(this, method));
        }
        return scenarios;
    }

    private ArrayList<SubUseCase> extractSubUseCases() throws Exception {
        ArrayList<SubUseCase> subUseCases = new ArrayList<SubUseCase>();
        for (Field field : getPublicFields()) {
            Object subUseCaseObject = field.get(object);
            final Decorator decorator = DecoratorExtractor.getDecorator(configuration, field.getDeclaredAnnotations());
            if (subUseCaseObject == null) {
                subUseCases.add(new SubUseCase(this,field.getType().getClass().newInstance(), configuration, decorator));
            } else {
                subUseCases.add(new SubUseCase(this,subUseCaseObject, configuration,decorator));
            }
        }
        return subUseCases;
    }


    private ArrayList<Helper> extractFixturesHelpers() throws Exception {
        ArrayList<Helper> helpers = new ArrayList<Helper>();
        for (Field field : getHelperFields()) {
            field.setAccessible(true);
            Object helperObject = field.get(this.object);
            helpers.add(new Helper(helperObject,configuration));
        }

        return helpers;
    }

    private ArrayList<Field> getPublicFields() {
        ArrayList<Field> fields = new ArrayList<Field>();
        for (Field field : classUnderTest.getFields()) {
            if (Modifier.isPublic(field.getModifiers())) {
                fields.add(field);
            }
        }
        return fields;
    }

    private ArrayList<Field> getHelperFields() {
        ArrayList<Field> helpersFields = new ArrayList<Field>();
        for (Field field : classUnderTest.getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof FixturesHelper) {
                    if (Modifier.isPublic(field.getModifiers())) {
                        throw new RuntimeException("The FixturesHelper field " + field.getName() + " shall not be public");
                    } else {
                        helpersFields.add(field);
                    }
                }
            }
        }
        return helpersFields;
    }

    private ArrayList<Method> getTestMethods() {
        ArrayList<Method> testMethods = new ArrayList<Method>();
        for (Method method : classUnderTest.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class) && isPublic(method.getModifiers())) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }

    private ArrayList<Field> extractDisplayableFields() {
        ArrayList<Field> displayableFields = new ArrayList<Field>();
        for (Field field : classUnderTest.getDeclaredFields()) {
            if (field.isAnnotationPresent(Displayable.class) &&
                    isPrivate(field.getModifiers())) {
                displayableFields.add(field);
            }
        }
        return displayableFields;
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

    public String getName() {
        return javaToHumanTranslator.humanize(classUnderTest);
    }

    public Fixture getFixtureByMethodName(String methodName) {
        for (Fixture fixture : fixtures) {
            if (methodName.equals(fixture.method.getName())) {
                return fixture;
            }
        }
        for (Helper fixturesHelper : fixturesHelpers) {
            for (Fixture fixture : fixturesHelper.fixtures) {
                if (methodName.endsWith("." + fixture.method.getName())) {
                    return fixture;
                }
            }
        }
        throw new RuntimeException("No fixture found matching the private or protected method " + methodName + " in class " + classUnderTest.getCanonicalName() + "(use case: " + getName() + ")");
    }

    public boolean shouldBeIgnored() {
        return classUnderTest.getAnnotation(Ignore.class) != null;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean haveComment() {
        return comment != null;
    }


}
