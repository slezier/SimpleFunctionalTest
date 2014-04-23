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
package sft.report.decorators;

import sft.DefaultConfiguration;
import sft.decorators.Decorator;
import sft.decorators.Group;
import sft.result.FixtureCallResult;

import java.util.List;

public class HtmlGroup extends Group {

    public HtmlGroup(DefaultConfiguration configuration, String... parameters) {
        super(configuration, parameters);
    }
    public HtmlGroup(Decorator decorator) {
        super(decorator);
    }

    @Override
    public String applyOnFixtures(List<String> fixtures, List<FixtureCallResult> fixtureCallResuts) {
        if (fixtures.isEmpty()) {
            return "";
        }
        String result = "<div>";
        if (getName() != null) {
            result += "<h4 class='group'>" + getName() + "</h4>";
        }
        for (String fixture : fixtures) {
            result += fixture;
        }
        return result + "</div>";
    }

    @Override
    public boolean comply(Decorator other) {
        return other instanceof Group && this.toString().equals(other.toString());
    }

    private String getName(){
        if (parameters != null && parameters.length > 0) {
            return parameters[0];
        }else{
            return null;
        }
    }

    @Override
    public String toString() {
        return "Group(" + getName() + ")";
    }

}
