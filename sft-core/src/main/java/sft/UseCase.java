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
import java.util.List;

import static java.lang.reflect.Modifier.isPublic;

public class UseCase extends FixturesHolder {


    public final UseCase parent;
    public final Decorator useCaseDecorator;
    public final ArrayList<Scenario> scenarios;
    public final ArrayList<SubUseCase> subUseCases;
    public final Helpers helpers;
    private final JavaToHumanTranslator javaToHumanTranslator;
    private String comment;


    public UseCase(Class<?> classUnderTest) throws Exception {
        this(null, classUnderTest.newInstance(), new DefaultConfiguration());
    }


    public UseCase(UseCase parent, Class<?> classUnderTest) throws Exception {
        this(parent, classUnderTest.newInstance(), new DefaultConfiguration());
    }

    public UseCase(UseCase parent, Object objectUnderTest, DefaultConfiguration configuration) throws Exception {
        super(objectUnderTest, Visibility.PrivateOrProtectedOnly, configuration);
        this.parent = parent;
        javaToHumanTranslator = new JavaToHumanTranslator();
        useCaseDecorator = DecoratorExtractor.getDecorator(this.configuration, classUnderTest.getDeclaredAnnotations());
        scenarios = extractScenarios();
        subUseCases = extractSubUseCases();
        helpers = extractFixturesHelpers();
        UseCaseJavaParser javaClassParser = new UseCaseJavaParser(this.configuration, classUnderTest);
        javaClassParser.feed(this);
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
                subUseCases.add(new SubUseCase(this, field.getType().getClass().newInstance(), configuration, decorator));
            } else {
                subUseCases.add(new SubUseCase(this, subUseCaseObject, configuration, decorator));
            }
        }
        return subUseCases;
    }

    private Helpers extractFixturesHelpers() throws Exception {
        Helpers helpers = new Helpers();
        for (Field field : getHelperFields()) {
            field.setAccessible(true);
            Object helperObject = field.get(this.object);
            if (helperObject == null) {
                throw new RuntimeException("In class " + this.classUnderTest.getSimpleName() + " the fixtures helper" + field.getName() + " needs to be instantiated");
            }
            helpers.add(new Helper(helperObject, configuration));
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

    public String getName() {
        return javaToHumanTranslator.humanize(classUnderTest);
    }

    public Fixture getFixtureByMethodName(String methodName) {
        for (Fixture fixture : fixtures) {
            if (methodName.equals(fixture.method.getName())) {
                return fixture;
            }
        }

        Fixture fixture = helpers.getFixture(methodName);

        if(fixture == null){
            throw new RuntimeException("No fixture found matching the private or protected method " + methodName + " in class " + classUnderTest.getCanonicalName() + "(use case: " + getName() + ")");
        }
        return fixture;
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

    public List<String> getDisplayedContext() {
        List<String> result = new ArrayList<String>();
        result.addAll(displayedContext.getText());
        result.addAll(helpers.getDisplayedContext());
        return result;
    }

    public boolean isRoot(){
        return parent == null;
    }
}
