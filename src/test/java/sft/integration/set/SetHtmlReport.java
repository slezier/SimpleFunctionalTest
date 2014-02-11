package sft.integration.set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.DefaultConfiguration;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.report.HtmlReport;

/*
<div>
The DefaultConfiguration provide an HtmlReport to write test result in HTML.<br /><br />

The target of the HtmlReport resourcePath, will be copied to provide facilities files. <br />
This resourcePath is a directory that contains all image, css and js files needed for the report generation and display.<br />
For example, all css and js file of this directory will be included in the html head close.
 </div>
*/
@RunWith(SimpleFunctionalTest.class)
public class SetHtmlReport {
    private DefaultConfiguration configuration;

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


}
