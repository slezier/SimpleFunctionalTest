package sft.integration.set.sut;

import sft.DefaultConfiguration;
import sft.report.HtmlReport;

public class InkConfiguration extends DefaultConfiguration{

    public InkConfiguration(){
        getReport(HtmlReport.class).setResourcePath("other-style");
        getReport(HtmlReport.class).useCaseTemplate = "<!DOCTYPE html><html><head><title>Test: @@@useCase.name@@@</title>" +
                "@@@useCase.css@@@" +
                "@@@useCase.js@@@" +
                "</head>" +
                "<body>"+
                "<div class='ink-grid gutters'>"+
                "<h1 class='ink-label @@@useCase.issue@@@ invert'>@@@useCase.name@@@</h1>"+
                "@@@useCaseCommentTemplate@@@" +
                "@@@beforeUseCaseTemplate@@@" +
                "@@@scenarioTemplates@@@" +
                "@@@afterUseCaseTemplate@@@" +
                "@@@relatedUseCasesTemplates@@@" +
                "</div>"+
                "<div class='large-80'>@Copyright nevermind</div>" +
                "</body>"+
                "</html>";
        getReport(HtmlReport.class).useCaseCommentTemplate = "<div >"+
                "<p class='note'>"+
                "@@@comment.text@@@"+
                "</p>" +
                "</div>";
        getReport(HtmlReport.class).beforeUseCaseTemplate = "<div class='ink-alert block large-80 @@@beforeUseCase.issue@@@'> " +
                "<h4>Before Use Case</h4>"+
                "<p>"+
                "@@@contextInstructionTemplates@@@" +
                "@@@exceptionTemplate@@@" +
                "</p>"+
                "</div>";
        getReport(HtmlReport.class).scenarioTemplate = "<div class='ink-alert block large-80 @@@scenario.issue@@@'> " +
                "<h4>@@@scenario.name@@@</h4>" +
                "<p>"+
                "@@@scenarioCommentTemplate@@@" +
                "@@@beforeScenarioTemplate@@@" +
                "@@@scenarioInstructionTemplates@@@" +
                "@@@afterScenarioTemplate@@@" +
                "@@@displayedContextsTemplates@@@" +
                "@@@exceptionTemplate@@@"+
                "</p>"+
                "</div>";
        getReport(HtmlReport.class).scenarioCommentTemplate = "<p class='note'>"+
                "@@@comment.text@@@"+
                "</p>" +
                "<hr width='80%' align='center'/>";
        getReport(HtmlReport.class).exceptionTemplate = "<div class='ink-alert block error'>" +
                "<h4><span>@@@failure.className@@@</span>: <span>@@@failure.message@@@</span></h4>" +
                "<p class='small'>@@@failure.stacktrace@@@</p>" +
                "</div>";
        getReport(HtmlReport.class).beforeScenarioTemplate = "@@@contextInstructionTemplates@@@";
        getReport(HtmlReport.class).scenarioInstructionTemplate = "<div class='@@@instruction.issue@@@ ink-label invert'>" +
                "<span>@@@instruction.text@@@</span>" +
                "</div>";
        getReport(HtmlReport.class).afterScenarioTemplate = "@@@contextInstructionTemplates@@@";
        getReport(HtmlReport.class).displayedContextsTemplate = "<div>" +
                "@@@displayedContextTemplates@@@" +
                "</div>";
        getReport(HtmlReport.class).displayedContextTemplate = "<div>" +
                "<span>@@@displayedContext.text@@@</span>" +
                "</div>";
        getReport(HtmlReport.class).afterUseCaseTemplate = "<div class='ink-alert block large-80 @@@afterUseCase.issue@@@'> " +
                "<h4>After Use Case</h4>"+
                "<p>"+
                "@@@contextInstructionTemplates@@@" +
                "@@@exceptionTemplate@@@" +
                "</p>"+
                "</div>";
        getReport(HtmlReport.class).contextInstructionTemplate = "<div class='info ink-label invert'>" +
                "@@@instruction.text@@@" +
                "</div>";
        getReport(HtmlReport.class).relatedUseCasesTemplate = "<div class='large-80 '><ul>" +
                "@@@relatedUseCaseTemplates@@@"+
                "</ul></div>";
        getReport(HtmlReport.class).relatedUseCaseTemplate = "<li>" +
                "<a href='@@@relatedUseCase.link@@@' class='ink-label @@@relatedUseCase.issue@@@'>@@@relatedUseCase.name@@@</a>"+
                "</li>";
        getReport(HtmlReport.class).parameterTemplate = "<div>" +
                "<span>@@@parameter.value@@@</span>"+
                "</div>";
        getReport(HtmlReport.class).successClass = "success";
        getReport(HtmlReport.class).failedClass = "error";
        getReport(HtmlReport.class).ignoredClass = "warning";

    }

}
