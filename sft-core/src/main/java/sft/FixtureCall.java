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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class FixtureCall {

    public final UseCase useCase;
    public final String name;
    public final int line;
    public final Fixture fixture;
    public final int emptyLine;
    public ArrayList<String> parametersValues = new ArrayList<String>();

    public FixtureCall(UseCase useCase, String name, int line, Fixture fixture, ArrayList<String> parametersValues, int emptyLine) {
        this.useCase = useCase;
        this.name = name;
        this.line = line;
        this.fixture= fixture;
        this.emptyLine = emptyLine;
        this.parametersValues.addAll(parametersValues);
    }

    public Map<String, String> getParameters() {
        final HashMap<String, String> parameters = new HashMap<String, String>();
        for (int index = 0; index < fixture.parametersName.size(); index++) {
            String name = fixture.parametersName.get(index);
            String value = parametersValues.get(index);
            parameters.put(name,value);
        }
        return parameters;
    }
}
