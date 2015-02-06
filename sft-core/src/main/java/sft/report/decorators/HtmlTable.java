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
import sft.report.HtmlReport;
import sft.result.FixtureCallResult;

import java.util.List;

public class HtmlTable extends HtmlDecorator {

    public HtmlTable(DefaultConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String applyOnFixtures(List<FixtureCallResult> fixtureCallResults, String... parameters) {
        String result = "<table class='table'>";
        final String title = getTitle(parameters);
        if(title != null ){
            result+="<caption>"+ title +"</caption>";
        }
        result+="<thead>";
        result+="<tr>";
        for (String paramaterName : fixtureCallResults.get(0).fixtureCall.fixture.parametersName) {
            result+="<th>" +paramaterName+"</th>";
        }
        result+="<th></th></tr>";

        result+="</thead>";
        result+="<tbody>";
        for (FixtureCallResult fixtureCallResutfixture : fixtureCallResults) {
            result+="<tr>" ;
            for (String value : fixtureCallResutfixture.fixtureCall.parametersValues) {
                result+="<td>" +value+"</td>";
            }

            result+="<td class='instruction "+ getHtmlReport().getHtmlResources().convertIssue(fixtureCallResutfixture.issue)+"''><span></span></td></tr>";
        }
        result+="</tbody>";
        return result+"</table>";
    }

    private String getTitle(String... parameters){
        return parameters[0];
    }
}
