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
import sft.decorators.Decorator;
import sft.decorators.DecoratorExtractor;
import sft.javalang.JavaToHumanTranslator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

public class Scenario {
    public final Method method;
    public final UseCase useCase;
    public final Decorator decorator;
    private final JavaToHumanTranslator javaToHumanTranslator;
    public LinkedList<MethodCall> methodCalls = new LinkedList<MethodCall>();
    private String comment;

    public Scenario(UseCase useCase, Method method) throws Exception {
        this.useCase = useCase;
        this.method = method;
        this.javaToHumanTranslator = new JavaToHumanTranslator();
        this.decorator = DecoratorExtractor.getDecorator(useCase.configuration, method.getDeclaredAnnotations());

    }

    public String getName() {
        return javaToHumanTranslator.humanize(method);
    }

    public void run() throws InvocationTargetException, IllegalAccessException {
        method.invoke(useCase.object);
    }

    public boolean shouldBeIgnored() {
        return method.getAnnotation(Ignore.class) != null;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
