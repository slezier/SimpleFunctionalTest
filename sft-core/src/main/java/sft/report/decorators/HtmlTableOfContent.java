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
import sft.DefaultConfiguration;
import sft.UseCase;
import sft.report.HtmlReport;
import sft.report.RelativePathResolver;
import sft.result.ScenarioResult;
import sft.result.SubUseCaseResult;
import sft.result.UseCaseResult;

public class HtmlTableOfContent extends HtmlDecorator {


    private final RelativePathResolver relativePathResolver = new RelativePathResolver();


    public HtmlTableOfContent(DefaultConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String... parameters) {
        String result = getHtmlReport().applyOnUseCase(useCaseResult);
        final Document parse = Jsoup.parse(result);
        parse.select(".page-header").after("<div class='panel toc'>"+printUseCase(useCaseResult.useCase, useCaseResult)+"</div>");
        return parse.toString();
    }

    private String printUseCase(UseCase initialUseCase, UseCaseResult useCaseResult) {
        String result = "<ol>";
        for (SubUseCaseResult subUseCaseResult : useCaseResult.subUseCaseResults) {

            String pathToUseCaseToBreadcrumb = relativePathResolver.getRelativePathAsFile(initialUseCase.classUnderTest, subUseCaseResult.useCaseResult.useCase.classUnderTest, ".html");

            result += "<li class='"+ configuration.getReport(HtmlReport.class).getHtmlResources().convertIssue(subUseCaseResult.useCaseResult.getIssue())+"'>" +
                    "<span><a href='"+pathToUseCaseToBreadcrumb+"'>"+subUseCaseResult.useCaseResult.useCase.getName()+"</a></span>"+
                    printScenario(subUseCaseResult.useCaseResult) +
                    printUseCase(initialUseCase, subUseCaseResult.useCaseResult) +
                    "</li>";
        }
        return result+"</ol>";
    }

    private String printScenario(UseCaseResult useCaseResult) {
        String result = "<ul>";
        for (ScenarioResult scenarioResult : useCaseResult.scenarioResults) {
            result += "<li class='"+ configuration.getReport(HtmlReport.class).getHtmlResources().convertIssue(scenarioResult.issue)+"'>" +
                    "<span>"+scenarioResult.scenario.getName()+"</span>"+
                    "</li>";
        }
        return result+ "</ul>";
    }

}