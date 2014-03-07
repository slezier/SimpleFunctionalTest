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

public class FixtureCall {

    public final String name;
    public final int line;
    public final Fixture fixture;
    public ArrayList<String> parametersValues = new ArrayList<String>();

    public FixtureCall(String name, int line, Fixture fixture, ArrayList<String> parametersValues) {
        this.name = name;
        this.line = line;
        this.fixture= fixture;
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
