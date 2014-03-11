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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sft.DefaultConfiguration;
import sft.UseCase;
import sft.report.RelativeHtmlPathResolver;
import sft.result.FixtureCallResult;
import sft.result.ScenarioResult;
import sft.result.UseCaseResult;

import java.util.List;

public class TableOfContent implements Decorator {

    private DefaultConfiguration configuration;

    @Override
    public Decorator withParameters(String... parameters) {

        return this;
    }

    @Override
    public Decorator withConfiguration(DefaultConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String result) {
        final Document parse = Jsoup.parse(result);
        parse.select(".page-header").after("<div class='panel toc'>"+printUseCase(useCaseResult.useCase, useCaseResult)+"</div>");
        return parse.toString();
    }

    private String printUseCase(UseCase initialUseCase, UseCaseResult useCaseResult) {
        String result = "<ol>";
        for (UseCaseResult subUseCaseResult : useCaseResult.subUseCaseResults) {
            final RelativeHtmlPathResolver relativeHtmlPathResolver = configuration.getReport().pathResolver;
            final String origin = relativeHtmlPathResolver.getPathOf(initialUseCase.classUnderTest, ".html");
            final String target = relativeHtmlPathResolver.getPathOf(subUseCaseResult.useCase.classUnderTest, ".html");
            final String pathToUseCaseToBreadcrumb = relativeHtmlPathResolver.getRelativePathToFile(origin, target);

            result += "<li class='"+configuration.getReport().getHtmlResources().convertIssue(subUseCaseResult.getIssue())+"'>" +
                    "<span><a href='"+pathToUseCaseToBreadcrumb+"'>"+subUseCaseResult.useCase.getName()+"</a></span>"+
                    printScenario(subUseCaseResult) +
                    printUseCase(initialUseCase, subUseCaseResult) +
                    "</li>";
        }
        return result+"</ol>";
    }

    private String printScenario(UseCaseResult useCaseResult) {
        String result = "<ul>";
        for (ScenarioResult scenarioResult : useCaseResult.scenarioResults) {
            result += "<li class='"+configuration.getReport().getHtmlResources().convertIssue(scenarioResult.issue)+"'>" +
                    "<span>"+scenarioResult.scenario.getName()+"</span>"+
                    "</li>";
        }
        return result+ "</ul>";
    }

    @Override
    public String applyOnScenario(String result) {
        throw new RuntimeException("TableOfContent can't be apply on scenario");
    }

    @Override
    public String applyOnFixtures(List<String> result, List<FixtureCallResult> fixtureCallResuts) {
        throw new RuntimeException("TableOfContent can't be apply on fixture");
    }

    @Override
    public boolean comply(Decorator other) {
        return other instanceof TableOfContent;
    }
}
