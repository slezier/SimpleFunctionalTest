package sft.integration.set.sut;

import sft.DefaultConfiguration;

public class InkConfiguration extends DefaultConfiguration{

    public InkConfiguration(){
        getReport().setResourcePath("other-style");
        getReport().setUseCaseTemplate("<!DOCTYPE html><html><head><title>Test: @@@useCase.name@@@</title>" +
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
                "</body>"+
                "</html>");
        getReport().setUseCaseCommentTemplate(
                "<div >"+
                "<p class='note'>"+
                "@@@comment.text@@@"+
                "</p>" +
                "</div>");
        getReport().setBeforeUseCaseTemplate(
                "<div class='ink-alert block large-80  @@@beforeUseCase.issue@@@'> " +
                "<h4>Before Use Case</h4>"+
                "<p>"+
                "@@@contextInstructionTemplates@@@" +
                "@@@exceptionTemplate@@@" +
                "</p>"+
                "</div>");
        getReport().setScenarioTemplate(
                "<div class='ink-alert block large-80 @@@scenario.issue@@@'> " +
                "<h4>@@@scenario.name@@@</h4>" +
                "<p>"+
                "@@@scenarioCommentTemplate@@@" +
                "@@@beforeScenarioTemplate@@@" +
                "@@@scenarioInstructionTemplates@@@" +
                "@@@afterScenarioTemplate@@@" +
                "@@@displayedContextsTemplates@@@" +
                "@@@exceptionTemplate@@@"+
                "</p>"+
                "</div>");
        getReport().setScenarioCommentTemplate(
                "<p class='note'>"+
                "@@@comment.text@@@"+
                "</p>" +
                "<hr width='80%' align='center'/>");
        getReport().setExceptionTemplate(
                "<div class='ink-alert block error'>" +
                "<h4><span>@@@failure.className@@@</span>: <span>@@@failure.message@@@</span></h4>" +
                "<p class='small'>@@@failure.stacktrace@@@</p>" +
                "</div>");
        getReport().setBeforeScenarioTemplate(
                "@@@contextInstructionTemplates@@@");
        getReport().setScenarioInstructionTemplate(
                "<div class='@@@instruction.issue@@@ ink-label invert'>" +
                "<span>@@@instruction.text@@@</span>" +
                "</div>");
        getReport().setAfterScenarioTemplate(
                "@@@contextInstructionTemplates@@@");
        getReport().setDisplayedContextsTemplate(
                "<div>" +
                "@@@displayedContextTemplates@@@" +
                "</div>");
        getReport().setDisplayedContextTemplate(
                "<div>" +
                "<span>@@@displayedContext.text@@@</span>" +
                "</div>");
        getReport().setAfterUseCaseTemplate(
                "<div class='ink-alert block large-80 @@@afterUseCase.issue@@@'> " +
                "<h4>After Use Case</h4>"+
                "<p>"+
                "@@@contextInstructionTemplates@@@" +
                "@@@exceptionTemplate@@@" +
                "</p>"+
                "</div>");
        getReport().setContextInstructionTemplate(
                "<div class='info ink-label invert'>" +
                "@@@instruction.text@@@" +
                "</div>");
        getReport().setRelatedUseCasesTemplate(
                "<div class='large-80 '><ul>" +
                "@@@relatedUseCaseTemplates@@@"+
                "</ul></div>");
        getReport().setRelatedUseCaseTemplate(
                "<li>" +
                "<a href='@@@relatedUseCase.link@@@' class='ink-label @@@relatedUseCase.issue@@@'>@@@relatedUseCase.name@@@</a>"+
                "@@@relatedUseCaseErrorTemplate@@@"+
                "</li>");
        getReport().setRelatedUseCaseErrorTemplate(
                "<div>" +
                "@@@error.message@@@"+
                "</div>");
        getReport().setRelatedUseCaseErrorTemplate(
                "<div>" +
                "<span>@@@parameter.value@@@</span>"+
                "</div>");
        getReport().setSuccessClass("success");
        getReport().setFailedClass("error");
        getReport().setIgnoredClass("warning");

    }

}
