package sft.integration.use;

import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.StyleDecoratorSample;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/*
 The annotation @Decorate allow to prettify your html report.
 */
@RunWith(SimpleFunctionalTest.class)
public class UsingDecorator {

    @Displayable
    private SftResources sftResources;
    private Document htmlReport;

    @Test
    public void addStyleToAnUseCase() throws Exception {
        byAddingAStyleDecoratorWithParameterOnUseCase("specificStyle");
        theBodyOfTheHtmlReportWillHaveTheCssClass("specificStyle");
    }
    @Test
    @Ignore
    public void addStyleToAScenario() throws Exception {
        byAddingAStyleDecoratorWithParameterOnScenario("specificStyle");

    }

    private void byAddingAStyleDecoratorWithParameterOnScenario(String specificStyle) {
        throw new NotImplementedException();
    }

    @Text("By adding a style decorator with parameter '${style}'  on use case: @Decorate(Style.class,\"${style}\") ")
    private void byAddingAStyleDecoratorWithParameterOnUseCase(String style) throws Exception {
        JUnitHelper jUnitHelper = new JUnitHelper(this.getClass(),StyleDecoratorSample.class, "target/sft-result/sft/integration/use/sut/StyleDecoratorSample.html");
        jUnitHelper.run();
        sftResources = jUnitHelper.displayResources();
        htmlReport = jUnitHelper.getHtmlReport();
    }

    @Text("The body of the html report will have the css class '${style}'")
    private void theBodyOfTheHtmlReportWillHaveTheCssClass(String style) {
        Assert.assertTrue("Expecting class "+style+" in body",htmlReport.select("body.useCase").hasClass(style));
    }


}
