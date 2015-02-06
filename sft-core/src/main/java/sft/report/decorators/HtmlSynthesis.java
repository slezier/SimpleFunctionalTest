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
import sft.Scenario;
import sft.UseCase;
import sft.report.RelativePathResolver;
import sft.report.TemplateString;
import sft.result.ResultDigest;
import sft.result.ScenarioResult;
import sft.result.UseCaseResult;

import java.util.ArrayList;
import java.util.List;

public class HtmlSynthesis extends HtmlDecorator {

    public String useCaseTemplate =
                    "@@@failedDigest@@@"+
                    "@@@ignoredDigest@@@"+
                    "@@@succeededDigest@@@";

    public String digestTemplate =
            "      <div class=\"digest @@@issue@@@\"><span class=\"title\">@@@nbScenarios@@@ scenarios @@@issue@@@</span>\n" +
            "@@@scenariosList@@@" +
            "      </div>" ;

    public String scenariosListTemplate =
            "       <a id=\"@@@issue@@@_fold\" onClick=\"$('#@@@issue@@@_fold').toggle();$('#@@@issue@@@_unfold').toggle();\"  @@@fold@@@>+</a>\n" +
                    "         <span id=\"@@@issue@@@_unfold\" @@@unfold@@@>\n" +
                    "           <a onClick=\"$('#@@@issue@@@_fold').toggle();$('#@@@issue@@@_unfold').toggle();\" >-</a>\n" +
                    "             <ul>\n"+
                    "@@@scenarios@@@" +
                    "             </ul>\n"+
                    "          </span>\n";

    public String relatedScenarioTemplate =
            "            <li class=\"relatedUseCase @@@relatedUseCase.issue@@@\">\n" +
                    "              <a href=\"@@@relatedUseCase.link@@@\"><span>@@@digest.scenario.path@@@</span></a>\n" +
                    "            </li>\n";
    private RelativePathResolver pathResolver = new RelativePathResolver();


    public HtmlSynthesis(DefaultConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String... parameters) {
        String result = getHtmlReport().applyOnUseCase(useCaseResult);
        final Document parse = Jsoup.parse(result);
        parse.select(".page-header").after("<div class='panel synthesis'>"+printSynthesis(useCaseResult)+"</div>");
        return parse.toString();
    }

    private String printSynthesis(UseCaseResult useCaseResult) {
        Class<?> classUnderTest = useCaseResult.useCase.classUnderTest;
        ResultDigest digest = new ResultDigest(useCaseResult);

        return new TemplateString(useCaseTemplate)
                .replace("@@@failedDigest@@@", "" + getScenariosDigest(useCaseResult.useCase, digest.scenariosFailed, "failed", true))
                .replace("@@@ignoredDigest@@@", getScenariosDigest(useCaseResult.useCase, digest.scenariosIgnored, "ignored", true))
                .replace("@@@succeededDigest@@@", getScenariosDigest(useCaseResult.useCase, digest.scenariosSucceeded, "succeeded", false))
                .getText();
    }

    private String getScenariosDigest(UseCase useCase, ArrayList<ScenarioResult> scenariosFailed, String issue, boolean fold) {
        return new TemplateString(digestTemplate)
                .replace("@@@issue@@@", "" + issue)
                .replace("@@@nbScenarios@@@", "" + scenariosFailed.size())
                .replace("@@@scenariosList@@@", "" + getScenariosList(useCase, scenariosFailed, issue, fold))
                .getText();
    }


    private String getScenariosList(UseCase useCase, ArrayList<ScenarioResult> scenariosSucceeded, String issue, boolean fold) {
        String foldStyle = "";
        String unfoldStyle = "style=\"display: none;\"";
        if (fold) {
            foldStyle = "style=\"display: none;\"";
            unfoldStyle = "";
        }
        return new TemplateString(scenariosListTemplate)
                .replace("@@@issue@@@", issue)
                .replace("@@@fold@@@", foldStyle)
                .replace("@@@unfold@@@", unfoldStyle)
                .replace("@@@scenarios@@@", "" + applyOnSubUseCases(useCase, scenariosSucceeded))
                .getText();
    }

    public String applyOnSubUseCases(UseCase parentUseCase, List<ScenarioResult> scenarioResults) {
        String relatedUseCase = "";
        for (ScenarioResult scenarioResult : scenarioResults) {
            relatedUseCase += new TemplateString(relatedScenarioTemplate)
                    .replace("@@@relatedUseCase.issue@@@", getHtmlReport().getHtmlResources().convertIssue(scenarioResult.issue))
                    .replace("@@@relatedUseCase.link@@@", getRelativeUrl(scenarioResult.scenario.useCase, parentUseCase))
                    .replace("@@@digest.scenario.path@@@", generatePath(parentUseCase, scenarioResult.scenario))
                    .replace("@@@digest.scenario.name@@@", scenarioResult.scenario.getName())
                    .replace("@@@digest.useCase.name@@@", scenarioResult.scenario.useCase.getName())
                    .getText();
        }
        return relatedUseCase;

    }

    private String generatePath(UseCase parentUseCase, Scenario scenario) {
        return generatePath(parentUseCase,scenario.useCase) + " : " + scenario.getName();
    }

    private String generatePath(UseCase parentUseCase, UseCase useCase) {
        String result = "";
        if( useCase.parent != null && useCase.parent != parentUseCase){
            result = generatePath(parentUseCase, useCase.parent) + " : ";
        }
        return result + useCase.getName();
    }

    private String getRelativeUrl(UseCase subUseCase, UseCase parentUseCase) {
        String source = pathResolver.getPathOf(parentUseCase.classUnderTest, ".html");
        String target = pathResolver.getPathOf(subUseCase.classUnderTest, ".html");
        return pathResolver.getRelativePathToFile(source, target);
    }

}