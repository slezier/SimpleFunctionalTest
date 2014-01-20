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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import org.junit.Ignore;
import sft.javalang.JavaToHumanTranslator;
import sft.javalang.parser.MethodCall;

public class Scenario {
    public final Method method;
    public final UseCase useCase;
    private final JavaToHumanTranslator javaToHumanTranslator;
    private String comment;
    public LinkedList<MethodCall> methodCalls;

    public Scenario(UseCase useCase, Method method) {
        this.useCase = useCase;
        this.method = method;
        this.javaToHumanTranslator = new JavaToHumanTranslator();
    }

    public String getName(){
        return javaToHumanTranslator.humanize(method);
    }

    public void run() throws InvocationTargetException, IllegalAccessException {
        method.invoke(useCase.object);
    }

    public boolean shouldBeIgnored() {
        return method.getAnnotation(Ignore.class) != null;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
