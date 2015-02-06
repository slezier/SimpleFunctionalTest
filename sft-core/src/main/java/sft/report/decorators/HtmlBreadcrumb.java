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
import sft.result.UseCaseResult;

public class HtmlBreadcrumb extends HtmlDecorator {


    public HtmlBreadcrumb(DefaultConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String... parameters) {
        String result = getHtmlReport().applyOnUseCase(useCaseResult);
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
            final RelativePathResolver relativePathResolver = new RelativePathResolver();
            final String origin = relativePathResolver.getPathOf(initialUseCase.classUnderTest, ".html");
            final String target = relativePathResolver.getPathOf(useCaseToBreadcrumb.classUnderTest, ".html");
            final String pathToUseCaseToBreadcrumb = relativePathResolver.getRelativePathToFile(origin, target);
            return result + "<li><a href=\"" +pathToUseCaseToBreadcrumb+ "\">" + useCaseToBreadcrumb.getName() + "</a></li>";
        }
    }

}
