package sft.integration.set.sut;

import sft.DefaultConfiguration;

public class InkConfiguration extends DefaultConfiguration{

    public InkConfiguration(){
        getReport().setResourcePath("other-style");
        getReport().useCaseTemplate = "<!DOCTYPE html><html><head><title>Test: @@@useCase.name@@@</title>" +
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
        getReport().useCaseCommentTemplate = "<div >"+
                "<p class='note'>"+
                "@@@comment.text@@@"+
                "</p>" +
                "</div>";
        getReport().beforeUseCaseTemplate = "<div class='ink-alert block large-80 @@@beforeUseCase.issue@@@'> " +
                "<h4>Before Use Case</h4>"+
                "<p>"+
                "@@@contextInstructionTemplates@@@" +
                "@@@exceptionTemplate@@@" +
                "</p>"+
                "</div>";
        getReport().scenarioTemplate = "<div class='ink-alert block large-80 @@@scenario.issue@@@'> " +
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
        getReport().scenarioCommentTemplate = "<p class='note'>"+
                "@@@comment.text@@@"+
                "</p>" +
                "<hr width='80%' align='center'/>";
        getReport().exceptionTemplate = "<div class='ink-alert block error'>" +
                "<h4><span>@@@failure.className@@@</span>: <span>@@@failure.message@@@</span></h4>" +
                "<p class='small'>@@@failure.stacktrace@@@</p>" +
                "</div>";
        getReport().beforeScenarioTemplate = "@@@contextInstructionTemplates@@@";
        getReport().scenarioInstructionTemplate = "<div class='@@@instruction.issue@@@ ink-label invert'>" +
                "<span>@@@instruction.text@@@</span>" +
                "</div>";
        getReport().afterScenarioTemplate = "@@@contextInstructionTemplates@@@";
        getReport().displayedContextsTemplate = "<div>" +
                "@@@displayedContextTemplates@@@" +
                "</div>";
        getReport().displayedContextTemplate = "<div>" +
                "<span>@@@displayedContext.text@@@</span>" +
                "</div>";
        getReport().afterUseCaseTemplate = "<div class='ink-alert block large-80 @@@afterUseCase.issue@@@'> " +
                "<h4>After Use Case</h4>"+
                "<p>"+
                "@@@contextInstructionTemplates@@@" +
                "@@@exceptionTemplate@@@" +
                "</p>"+
                "</div>";
        getReport().contextInstructionTemplate = "<div class='info ink-label invert'>" +
                "@@@instruction.text@@@" +
                "</div>";
        getReport().relatedUseCasesTemplate = "<div class='large-80 '><ul>" +
                "@@@relatedUseCaseTemplates@@@"+
                "</ul></div>";
        getReport().relatedUseCaseTemplate = "<li>" +
                "<a href='@@@relatedUseCase.link@@@' class='ink-label @@@relatedUseCase.issue@@@'>@@@relatedUseCase.name@@@</a>"+
                "</li>";
        getReport().parameterTemplate = "<div>" +
                "<span>@@@parameter.value@@@</span>"+
                "</div>";
        getReport().successClass = "success";
        getReport().failedClass = "error";
        getReport().ignoredClass = "warning";

    }

}
