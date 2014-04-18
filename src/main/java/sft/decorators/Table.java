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
package sft.decorators;

import sft.DefaultConfiguration;
import sft.result.FixtureCallResult;
import sft.result.UseCaseResult;

import java.util.List;

public class Table extends Decorator {

    @Override
    public Decorator withConfiguration(DefaultConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String result) {
        throw new RuntimeException("Table couldn't be applied on use case");
    }

    @Override
    public String applyOnScenario(String result) {
        throw new RuntimeException("Table couldn't be applied on scenario");
    }

    @Override
    public String applyOnFixtures(List<String> instructions, List<FixtureCallResult> fixtureCallResuts) {
        String result = "<table class='table'>";
        if(getTitle() != null ){
            result+="<caption>"+ getTitle()+"</caption>";
        }


        result+="<thead>";
        result+="<tr>";
        for (String paramaterName : fixtureCallResuts.get(0).fixtureCall.fixture.parametersName) {
            result+="<th>" +paramaterName+"</th>";
        }
        result+="<th></th></tr>";

        result+="</thead>";
        result+="<tbody>";
        for (FixtureCallResult fixtureCallResutfixture : fixtureCallResuts) {
            result+="<tr>" ;
            for (String value : fixtureCallResutfixture.fixtureCall.parametersValues) {
                result+="<td>" +value+"</td>";
            }

            result+="<td class='instruction "+configuration.getReport().getHtmlResources().convertIssue(fixtureCallResutfixture.issue)+"''><span></span></td></tr>";
        }
        result+="</tbody>";
        return result+"</table>";
    }

    private String getTitle(){
        return parameters[0];
    }

    @Override
    public boolean comply(Decorator other) {
        return other.getClass() == Table.class;
    }
}
