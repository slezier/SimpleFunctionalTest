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

import sft.javalang.JavaToHumanTranslator;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Fixture {

    private final JavaToHumanTranslator javaToHumanTranslator = new JavaToHumanTranslator();
    public final Method method;
    public ArrayList<String> parametersName = new ArrayList<String>();

    public Fixture(Method method){
        this.method = method;
    }

    public String getName() {
        return javaToHumanTranslator.humanize(method);
    }

    public String getText(ArrayList<String> parametersName,ArrayList<String> parametersValue) {
        return javaToHumanTranslator.humanize(method,parametersName,parametersValue);
    }

}
