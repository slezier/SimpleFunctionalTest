package sft.integration.set;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.DefaultConfiguration;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.decorators.Breadcrumb;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.JavaResource;
import sft.integration.fixtures.SftResources;
import sft.integration.set.sut.InkConfiguration;
import sft.integration.set.sut.InkStyleUseCase;
import sft.report.HtmlReport;

import java.io.File;
import java.io.IOException;

/*
<div>
The DefaultConfiguration provide an HtmlReport to write test result in HTML.<br /><br />

The target of the HtmlReport resourcePath, will be copied to provide facilities files. <br />
This resourcePath is a directory that contains all image, css and js files needed for the report generation and display.<br />
For example, all css and js file of this directory will be included in the html head close.
 </div>
*/
@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
public class HtmlReportSettings {
    private DefaultConfiguration configuration;
    private Document htmlReport;
    @Displayable
    private String configurationResource;
    @Displayable
    private SftResources sftResources;

    /*
    The DefaultConfiguration provide an HtmlReport to write test result in HTML.<br /><br />

    The target of the HtmlReport resourcePath, will be copied to provide facilities files. <br />
    This resourcePath is a directory that contains all image, css and js files needed for the report generation and display.<br />
    For example, all css and js file of this directory will be included in the html head close.
    */
    @Test
    public void defaultReport(){
        theDefaultConfigurationProvideAnHtmlReport();
    }


    /*
    The HtmlReport write report in a specific folder target/sft-result/.
     */
    @Test
    public void changeHtmlReportDestination() throws Exception {
        defaultReportPathOfHtmlReportIs("target/sft-result/");
        thenAllReportFilesWillBeCreatedIntoThisFolder("sft.integration.set.HtmlReportSettings", "target/sft-result/sft/integration/set/HtmlReportSettings.html");
        bySettingTheReportPathTo("site/");
        thenAllReportFilesWillBeCreatedIntoThisFolder("sft.integration.set.HtmlReportSettings", "site/sft/integration/set/HtmlReportSettings.html");

    }

    @Text("Default report path of html report is ${reportPath}")
    private void defaultReportPathOfHtmlReportIs(String reportPath) {
        configuration = new DefaultConfiguration();
        Assert.assertEquals(reportPath,configuration.getReport().getReportPath());
    }

    @Text("By setting the report path to ${reportPath}")
    private void bySettingTheReportPathTo(String reportPath) {
        configuration.getReport().setReportPath(reportPath);
    }

    @Text("Simple functional test will load classes from this folder( for example the class ${useCaseClass} will generate the html report ${reportFile})")
    private void thenAllReportFilesWillBeCreatedIntoThisFolder(String useCaseClass, String reportFile) throws Exception {
        File sourceFile = configuration.getTargetFolder().getFileFromClass(Class.forName(useCaseClass), ".html");
        String initialPath = sourceFile.getPath();
        Assert.assertTrue("expecting "+ initialPath+ " ending with "+ reportFile,initialPath.endsWith(reportFile));
    }

    /*
    The most convenient way to custom your html report, is changing resourcePath.<br />
    You can modify native css and js file provide by SimpleFunctionalTest (copied in ./target/sft-result/sft-html-default/)
    in an local directory and then set its location as the resourcePath.<br /><br />

    Css class used by HtmlReport are:
    <ul>
        <li>ignored</li>
        <li>failed</li>
        <li>succeeded</li>
        <li>useCase</li>
        <li>useCaseName</li>
        <li>beforeUseCase</li>
        <li>afterUseCase</li>
        <li>comment</li>
        <li>scenario</li>
        <li>scenarioName</li>
        <li>beforeScenario</li>
        <li>afterScenario</li>
        <li>instruction</li>
        <li>exception</li>
        <li>relatedUseCase</li>
    </ul>
    */
    @Test
    public void changeCssAndJs() throws Exception {
        whenHtmlReportUseTheDefaultEmbeddedResource("sft-html-default");
        thenHtmlReportIncludePathToJsFile("../../../sft-html-default/bootstrap.min.js, ../../../sft-html-default/jquery-1.10.2.min.js");
        andIncluedPathToCssFile("../../../sft-html-default/bootstrap-theme.min.css, ../../../sft-html-default/bootstrap.min.css, ../../../sft-html-default/sft.css");

        bySettingTheResourcePathTo("sut-html-resources");
        thenHtmlReportIncludePathToJsFile("../../../sut-html-resources/override.js");
        andIncluedPathToCssFile("../../../sut-html-resources/override.css");
    }

    @Text("When html report use the default embedded resource ${path}")
    private void whenHtmlReportUseTheDefaultEmbeddedResource(String path) {
        Assert.assertEquals(path, configuration.getReport().getResourcePath());
    }

    @Text("Then html report include path to js files ${pathToJsFiles}")
    private void thenHtmlReportIncludePathToJsFile(String pathToJsFiles) {
        String includeJs = configuration.getReport().getHtmlResources().getIncludeJsDirectives(this.getClass());
        includeJs=includeJs.replaceAll("<script src=\"","");
        includeJs=includeJs.replaceAll("\"></script>\n",", ");
        includeJs=includeJs.substring(0,includeJs.length()-2);
        Assert.assertEquals(pathToJsFiles, includeJs);
    }

    @Text("And include path to css files ${pathToCssFiles}")
    private void andIncluedPathToCssFile(String pathToCssFiles) {
        String includeCss = configuration.getReport().getHtmlResources().getIncludeCssDirectives(this.getClass());
        includeCss=includeCss.replaceAll("<link rel=\"stylesheet\" href=\"","");
        includeCss=includeCss.replaceAll("\" />\n", ", ");
        includeCss=includeCss.substring(0,includeCss.length()-2);
        Assert.assertEquals(pathToCssFiles, includeCss);
    }

    @Text("By setting the resource path to ${newResourcePath}")
    private void bySettingTheResourcePathTo(String newResourcePath) {
        configuration.getReport().setResourcePath(newResourcePath);
    }

    private void theDefaultConfigurationProvideAnHtmlReport() {
        configuration = new DefaultConfiguration();
        Assert.assertEquals(HtmlReport.class, configuration.getReport().getClass());
    }

    /*
    The html report could be customized matching your UI design vision.<br />
    A basic template engine is used. <br />
    You can modify each <a href='#' onclick='$(templates).toggle();return false;'>templates</a>. <br />
    All templates are surround by '@@@', excepts for successClass, failedClass and ignoredClass that are values. <br />

    <table id='templates' class='table' style='display: none;'>
       <tr><th>template</th><th>inner template</th><th>detail of uses</th></tr>

       <tr><td rowspan='9'>useCaseTemplate</td><td>useCase.name</td><td>Name of the use case</td></tr>
       <tr>                                    <td>useCase.css</td><td>Injection of css available in resource folder</td></tr>
       <tr>                                    <td>useCase.js</td><td>Injection of javascript files available in resource folder</td></tr>
       <tr>                                    <td>useCase.issue</td><td>Issue of the use case (depending of successClass, failedClass, ignoredClass defined as below)</td></tr>
       <tr>                                    <td>useCaseCommentTemplate</td><td>Inclusion of use case comment</td></tr>
       <tr>                                    <td>beforeUseCaseTemplate</td><td>Inclusion of use case context raised</td></tr>
       <tr>                                    <td>scenarioTemplates</td><td>Inclusion of scenario</td></tr>
       <tr>                                    <td>afterUseCaseTemplate</td><td>Inclusion of use case context ended</td></tr>
       <tr>                                    <td>relatedUseCasesTemplates</td><td>Inclusion of other use cases</td></tr>

       <tr><td>useCaseCommentTemplate</td><td>comment.text</td><td>Text of the comment</td></tr>

       <tr><td rowspan='3'>beforeUseCaseTemplate</td><td>beforeUseCase.issue</td><td>Issue of the context raise of use case (depending of successClass, failedClass, ignoredClass defined as below)</td></tr>
       <tr>                                          <td>contextInstructionTemplates</td><td>Inclusion of instructions call needed for the context  raise of use case</td></tr>
       <tr>                                          <td>exceptionTemplate</td><td>Inclusion of exception report if needed</td></tr>

       <tr><td rowspan='8'>scenarioTemplate</td><td>scenario.issue</td><td>Issue of the scenario (depending of successClass, failedClass, ignoredClass defined as below)</td></tr>
       <tr>                                     <td>scenario.name</td><td>Name of the scenario</td></tr>
       <tr>                                     <td>scenarioCommentTemplate</td><td>Inclusion of scenario comment</td></tr>
       <tr>                                     <td>beforeScenarioTemplate</td><td>Inclusion of scenario context raise</td></tr>
       <tr>                                     <td>scenarioInstructionTemplates</td><td>Inclusion of fixtures call</td></tr>
       <tr>                                     <td>afterScenarioTemplate</td><td>Inclusion of scenario context ended</td></tr>
       <tr>                                     <td>displayedContextsTemplates</td><td>Inclusion of context fact</td></tr>
       <tr>                                     <td>exceptionTemplate</td><td>Inclusion of exception if needed</td></tr>

       <tr><td>scenarioCommentTemplate</td><td>comment.text</td><td>Text of the comment</td></tr>

       <tr><td rowspan='3'>exceptionTemplate</td><td>failure.className</td><td>Class name of the exception</td></tr>
       <tr>                                      <td>failure.message</td><td>Message of the exception</td></tr>
       <tr>                                      <td>failure.stacktrace</td><td>Stacktrace of the exception</td></tr>

       <tr><td>beforeScenarioTemplate</td><td>contextInstructionTemplates</td><td>Inclusion of one instruction call</td></tr>

       <tr><td rowspan='2'>scenarioInstructionTemplate</td><td>instruction.issue</td><td>Issue of the fixture call (depending of successClass, failedClass, ignoredClass defined as below)</td></tr>
       <tr>                                                <td>instruction.text</td><td>Text of the fixture call</td></tr>

       <tr><td>afterScenarioTemplate</td><td>contextInstructionTemplates</td><td>Inclusion of one instruction call</td></tr>

       <tr><td>displayedContextsTemplate</td><td>displayedContextTemplates</td><td>Inclusion of one context detail</td></tr>

       <tr><td>displayedContextTemplate</td><td>displayedContext.text</td><td>The detail of the context</td></tr>

       <tr><td rowspan='3'>afterUseCaseTemplate</td><td>afterUseCase.issue</td><td>Issue of the context ended (depending of successClass, failedClass, ignoredClass defined as below)</td></tr>
       <tr>                                         <td>contextInstructionTemplates</td><td>Inclusion of the instructions call</td></tr>
       <tr>                                         <td>exceptionTemplate</td><td>Inclusion of exception if needed</td></tr>

       <tr><td>contextInstructionTemplate</td><td>instruction.text</td><td>Text of the instruction call</td></tr>

       <tr><td>relatedUseCasesTemplate</td><td>relatedUseCaseTemplates</td><td>Inclusion of related use cases</td></tr>

       <tr><td rowspan='3'>relatedUseCaseTemplate</td><td>relatedUseCase.link</td><td>Link to the related use case report</td></tr>
       <tr>                                           <td>relatedUseCase.issue</td><td>Issue of the related use case (depending of successClass, failedClass, ignoredClass defined as below)</td></tr>
       <tr>                                           <td>relatedUseCase.name</td><td>Name of the related use case</td></tr>

       <tr><td>parameterTemplate</td><td>parameter.value</td><td>The value of instruction or fixture call</td></tr>

       <tr><td>successClass</td><td></td><td>class of issue successful; default value: success</td></tr>
       <tr><td>failedClass</td><td></td><td>class of issue failed; default value: failed</td></tr>
       <tr><td>ignoredClass</td><td></td><td>class of issue ignored; default value : warning</td></tr>

    </table>

    */
    @Test
    public void customHtml() throws IOException {
        byChangingTemplateValues();
        weChangeReportGeneration();
    }

    private void weChangeReportGeneration() {
        final String startHtml = "<!DOCTYPE html>\n" +
                "<html>\n" +
                " <head>\n" +
                "  <title>Test: Ink style use case</title>\n" +
                "  <link rel=\"stylesheet\" href=\"../../../../other-style/css/ink-min.css\" /> \n" +
                " </head>\n" +
                " <body>\n" +
                "  <div class=\"ink-grid gutters\">\n" +
                "   <h1 class=\"ink-label error invert\">Ink style use case</h1>\n" +
                "   <div>\n" +
                "    <p class=\"note\"> Test Use Case comment </p>\n" +
                "   </div>\n" +
                "   <div class=\"ink-alert block large-80 success\"> \n" +
                "    <h4>Before Use Case</h4>\n" +
                "    <p></p>\n" +
                "    <div class=\"info ink-label invert\">\n" +
                "     Set up use case\n" +
                "    </div>\n" +
                "    <p></p>\n" +
                "   </div>\n" +
                "   <div class=\"ink-alert block large-80 success\"> \n" +
                "    <h4>Successful scenario</h4>\n" +
                "    <p></p>\n" +
                "    <p class=\"note\"> Test scenario comment </p>\n" +
                "    <hr width=\"80%\" align=\"center\" />\n" +
                "    <div class=\"info ink-label invert\">\n" +
                "     Set up scenario\n" +
                "    </div>\n" +
                "    <div class=\"success ink-label invert\">\n" +
                "     <span>Successful condition</span>\n" +
                "    </div>\n" +
                "    <div class=\"info ink-label invert\">\n" +
                "     Tear down scenario\n" +
                "    </div>\n" +
                "    <p></p>\n" +
                "   </div>\n" +
                "   <div class=\"ink-alert block large-80 error\"> \n" +
                "    <h4>Unsuccessful scenario</h4>\n" +
                "    <p></p>\n" +
                "    <div class=\"info ink-label invert\">\n" +
                "     Set up scenario\n" +
                "    </div>\n" +
                "    <div class=\"success ink-label invert\">\n" +
                "     <span>Successful condition</span>\n" +
                "    </div>\n" +
                "    <div class=\"error ink-label invert\">\n" +
                "     <span>Condition that failed</span>\n" +
                "    </div>\n" +
                "    <div class=\"warning ink-label invert\">\n" +
                "     <span>Successful condition</span>\n" +
                "    </div>\n" +
                "    <div class=\"info ink-label invert\">\n" +
                "     Tear down scenario\n" +
                "    </div>\n" +
                "    <div class=\"ink-alert block error\">\n" +
                "     <h4><span>AssertionError</span>: <span>Condition failed</span></h4>\n" +
                "     <p class=\"small\">";



        final String endHtml = "</p>\n"+
                "    </div>\n" +
                "    <p></p>\n" +
                "   </div>\n" +
                "   <div class=\"ink-alert block large-80 warning\"> \n" +
                "    <h4>Ignored scenario</h4>\n" +
                "    <p></p>\n" +
                "    <div class=\"info ink-label invert\">\n" +
                "     Set up scenario\n" +
                "    </div>\n" +
                "    <div class=\"warning ink-label invert\">\n" +
                "     <span>Successful condition</span>\n" +
                "    </div>\n" +
                "    <div class=\"warning ink-label invert\">\n" +
                "     <span>Condition that failed</span>\n" +
                "    </div>\n" +
                "    <div class=\"warning ink-label invert\">\n" +
                "     <span>Successful condition</span>\n" +
                "    </div>\n" +
                "    <div class=\"info ink-label invert\">\n" +
                "     Tear down scenario\n" +
                "    </div>\n" +
                "    <p></p>\n" +
                "   </div>\n" +
                "   <div class=\"ink-alert block large-80 success\"> \n" +
                "    <h4>After Use Case</h4>\n" +
                "    <p></p>\n" +
                "    <div class=\"info ink-label invert\">\n" +
                "     Tear down use case\n" +
                "    </div>\n" +
                "    <p></p>\n" +
                "   </div>\n" +
                "   <div class=\"large-80 \">\n" +
                "    <ul>\n" +
                "     <li><a href=\"subUseCase/SubUseCaseFailed.html\" class=\"ink-label error\">Sub use case failed</a></li>\n" +
                "     <li><a href=\"subUseCase/SubUseCaseIgnored.html\" class=\"ink-label warning\">Sub use case ignored</a></li>\n" +
                "     <li><a href=\"subUseCase/SubUseCaseSucceeded.html\" class=\"ink-label success\">Sub use case succeeded</a></li>\n" +
                "    </ul>\n" +
                "   </div>\n" +
                "  </div>\n" +
                "  <div class=\"large-80\">\n" +
                "   @Copyright nevermind\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>";

        Assert.assertTrue(htmlReport.outerHtml().startsWith(startHtml));
        Assert.assertTrue(htmlReport.outerHtml().endsWith(endHtml));
    }

    private void byChangingTemplateValues() throws IOException {
        JUnitHelper jUnitHelper = new JUnitHelper(this.getClass(),InkStyleUseCase.class, "target/sft-result/sft/integration/set/sut/InkStyleUseCase.html");
        htmlReport = jUnitHelper.html;
        sftResources = jUnitHelper.displayResources();


        JavaResource configurationClass = new JavaResource(InkConfiguration.class);
        configurationResource ="<div class=\"resources\">"+ configurationClass.getOpenResourceHtmlLink(this.getClass(), "configuration", "alert-info")+ "</div>" ;
    }


}
