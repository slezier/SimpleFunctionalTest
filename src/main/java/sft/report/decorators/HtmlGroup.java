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
import sft.result.FixtureCallResult;
import sft.result.ScenarioResult;
import sft.result.SubUseCaseResult;

import java.util.List;

public class HtmlGroup extends HtmlDecorator {

    public HtmlGroup(DefaultConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String applyOnScenarios(List<ScenarioResult> scenarioResults, String... parameters) {
        if (scenarioResults.isEmpty()) {
            return "";
        }
        final String name = getName(parameters);
        if (name == null || "".equals(name)) {
            return getHtmlReport().applyOnScenarios(scenarioResults);
        }

        String result = "<div class='scenarios panel panel-default'>";
        result += "<div class='panel-heading'><h2 class='group'>" + name + "</h2></div>";
        result += "<div class='panel-body'>";

        for (ScenarioResult scenarioResult : scenarioResults) {
            result += getHtmlReport().applyOnScenario(scenarioResult);
        }

        return result+ "</div></div>";
    }

    @Override
    public String applyOnFixtures(List<FixtureCallResult> fixtureCallResuts, String... parameters) {
        if (fixtureCallResuts.isEmpty()) {
            return "";
        }
        String result = "<div>";
        final String name = getName(parameters);
        if (name != null) {
            result += "<h4 class='group'>" + name + "</h4>";
        }
        for (FixtureCallResult fixture : fixtureCallResuts) {
            result += getHtmlReport().generateFixtureCall(fixture);
        }
        return result + "</div>";
    }

    @Override
    public String applyOnSubUseCases(List<SubUseCaseResult> useCaseResult, String... parameters) {
        return getHtmlReport().applyOnSubUseCases(getName(parameters), useCaseResult);
    }

    private String getName(String... parameters){
        if (parameters != null && parameters.length > 0) {
            return parameters[0];
        }else{
            return null;
        }
    }
}
