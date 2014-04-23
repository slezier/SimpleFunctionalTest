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
import sft.UseCase;
import sft.decorators.Breadcrumb;
import sft.decorators.Decorator;
import sft.report.RelativeHtmlPathResolver;
import sft.result.FixtureCallResult;
import sft.result.UseCaseResult;

import java.util.List;

public class HtmlBreadcrumb extends Breadcrumb {

    public HtmlBreadcrumb(Decorator decorator) {
        super(decorator);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String result) {
        final Document parse = Jsoup.parse(result);
        parse.select(".page-header .text-center").append("<ol class=\"breadcrumb\">" + printFirstUseCase(useCaseResult.useCase,useCaseResult.useCase) + "</ol>");
        return parse.toString();
    }

    private String printFirstUseCase(UseCase initialUseCase,UseCase useCaseToBreadcrumb) {
        String result = "";
        if (useCaseToBreadcrumb.parent != null) {
            result = printFirstUseCase(initialUseCase,useCaseToBreadcrumb.parent);
        }
        if(initialUseCase == useCaseToBreadcrumb){
            return result + "<li class=\"active\">" + useCaseToBreadcrumb.getName() + "</li>";
        }else{
            final RelativeHtmlPathResolver relativeHtmlPathResolver = configuration.getReport().pathResolver;
            final String origin = relativeHtmlPathResolver.getPathOf(initialUseCase.classUnderTest, ".html");
            final String target = relativeHtmlPathResolver.getPathOf(useCaseToBreadcrumb.classUnderTest, ".html");
            final String pathToUseCaseToBreadcrumb = relativeHtmlPathResolver.getRelativePathToFile(origin, target);
            return result + "<li><a href=\"" +pathToUseCaseToBreadcrumb+ "\">" + useCaseToBreadcrumb.getName() + "</a></li>";
        }
    }

    @Override
    public String applyOnScenario(String result) {
        throw new RuntimeException("Breadcrumb can't be apply on scenario");
    }

    @Override
    public String applyOnFixtures(List<String> result, List<FixtureCallResult> fixtureCallResuts) {
        throw new RuntimeException("Breadcrumb can't be apply on scenario");
    }


}
