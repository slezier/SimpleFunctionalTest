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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import sft.DefaultConfiguration;
import sft.result.FixtureCallResult;
import sft.result.ScenarioResult;
import sft.result.SubUseCaseResult;
import sft.result.UseCaseResult;

import java.util.List;

public class HtmlStyle extends HtmlDecorator {


    public HtmlStyle(DefaultConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String... parameters) {
        String result = getHtmlReport().applyOnUseCase(useCaseResult);
        return addStyleToElementWithClass(result, ".useCase",parameters);
    }

    protected String[] getStyles(String... parameters) {
        if (parameters == null || parameters.length == 0) {
            throw new RuntimeException("Style decorator need one or more parameters");
        }
        return parameters;
    }

    private String addStyleToElementWithClass(String result, String cssQuery, String... parameters) {
        final Document parse = Jsoup.parse(result);

        final Elements elements = parse.select(cssQuery);
        if (elements == null || elements.size() == 0) {
            throw new RuntimeException("The decorator " + this.getClass().getCanonicalName() + " need class " + cssQuery + " in generated html to be usable.");
        }
        for (String style : getStyles(parameters)) {
            elements.addClass(style);
        }
        return parse.toString();
    }

    @Override
    public String applyOnScenarios(List<ScenarioResult> scenarioResults, String... parameters) {
        String result ="";
        for (ScenarioResult scenarioResult : scenarioResults) {
            result +=addStyleToElementWithClass(getHtmlReport().applyOnScenario(scenarioResult), ".scenario", parameters);

        }
        return result;
    }

    @Override
    public String applyOnFixtures(List<FixtureCallResult> fixtureCallResuts, String... parameters) {
        String result = "";
        for (FixtureCallResult fixture : fixtureCallResuts) {
            final String html = getHtmlReport().generateFixtureCall(fixture);
            result += addStyleToElementWithClass(html, ".instruction", parameters);
        }
        return result;
    }

    @Override
    public String applyOnSubUseCases(List<SubUseCaseResult> subUseCaseResults, String... parameters) {

        final String html = getHtmlReport().applyOnSubUseCases(null, subUseCaseResults);

        return addStyleToElementWithClass(html, ".relatedUseCase",parameters);
    }
}
