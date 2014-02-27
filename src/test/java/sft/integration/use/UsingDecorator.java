package sft.integration.use;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.DecoratorSample;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/*
 The annotation @Decorate allow to prettify your html report.
 */
@RunWith(SimpleFunctionalTest.class)
public class UsingDecorator {

    @Displayable
    private SftResources sftResources;

    @Test
    @Ignore
    public void addStyleToAnUseCase(){
        byAddingAStyleDecoratorWithParameterOnUseCase("specificStyle");
        theBodyOfTheHtmlReportWillHaveTheCssClass("specificStyle");
    }

    @Text("By adding a style decorator with parameter '${style}'  on use case: @Decorate(Style.class,\"${style}\") ")
    private void byAddingAStyleDecoratorWithParameterOnUseCase(String style) {
        JUnitHelper jUnitHelper = new JUnitHelper(this.getClass(),DecoratorSample.class, "target/sft-result/sft/integration/use/sut/DecoratorSample.html");
        jUnitHelper.run();
        sftResources = jUnitHelper.displayResources();
        throw new NotImplementedException();
    }

    @Text("The body of the html report will have the css class '${style}'")
    private void theBodyOfTheHtmlReportWillHaveTheCssClass(String style) {
        throw new NotImplementedException();
    }


}
