package sft.integration.use;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.decorators.Breadcrumb;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.HumanizationCamelCaseClassNameHumanized;
import sft.integration.use.sut.HumanizationUsingTextAnnotation;
import sft.integration.use.sut.Humanization_underscore_class_name_humanized;

import java.io.IOException;

/*
    Class and methods could be humanized 3 different ways:
 */
@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
@Text("Humanized your code: using camelCase or underscore transformation or annotation")
public class HumanizationCodeUsage {

    @Displayable
    private SftResources sftResources1;
    @Displayable
    private SftResources sftResources2;
    @Displayable
    private SftResources sftResources3;

    @Test
    public void namingInCamelCase() throws IOException {
        allCaseChangesAreReplacedBySpaces();
    }
    @Test
    public void namingUsingUnderscore() throws IOException {
        underscoreAreReplacedBySpace();
    }
    @Test
    public void usingTextAnnotation() throws IOException {
        textsAreDisplayedUnchanged();
    }

    private void allCaseChangesAreReplacedBySpaces() throws IOException {
        JUnitHelper functionalTest = new JUnitHelper(this.getClass(),HumanizationCamelCaseClassNameHumanized.class,"target/sft-result/sft/integration/use/sut/HumanizationCamelCaseClassNameHumanized.html");
        functionalTest.run();
        sftResources1 =functionalTest.displayResources();
        functionalTest.getHtmlReport();
        Assert.assertEquals("Humanization camel case class name humanized", functionalTest.getHtmlReport().select("*.useCaseName").text());
        Assert.assertEquals("Camel case scenario name humanized", functionalTest.getHtmlReport().select("*.scenarioName").text());
        Assert.assertEquals("Camel case fixture name humanized", functionalTest.getHtmlReport().select("*.instruction").text());
    }

    private void underscoreAreReplacedBySpace() throws IOException {
        JUnitHelper functionalTest = new JUnitHelper(this.getClass(),Humanization_underscore_class_name_humanized.class,"target/sft-result/sft/integration/use/sut/Humanization_underscore_class_name_humanized.html");
        functionalTest.run();
        sftResources2 =functionalTest.displayResources();
        functionalTest.getHtmlReport();
        Assert.assertEquals("Humanization underscore class name humanized", functionalTest.getHtmlReport().select("*.useCaseName").text());
        Assert.assertEquals("Underscore scenario name humanized", functionalTest.getHtmlReport().select("*.scenarioName").text());
        Assert.assertEquals("Underscore fixture name humanized", functionalTest.getHtmlReport().select("*.instruction").text());
    }

    private void textsAreDisplayedUnchanged() throws IOException {
        JUnitHelper functionalTest = new JUnitHelper(this.getClass(),HumanizationUsingTextAnnotation.class,"target/sft-result/sft/integration/use/sut/HumanizationUsingTextAnnotation.html");
        functionalTest.run();
        sftResources3 =functionalTest.displayResources();
        Assert.assertEquals("Use case name specified in @Text", functionalTest.getHtmlReport().select("*.useCaseName").text());
        Assert.assertEquals("Scenario name specified in @Text", functionalTest.getHtmlReport().select("*.scenarioName").text());
        Assert.assertEquals("Fixture name specified in @Text", functionalTest.getHtmlReport().select("*.instruction").text());
    }


}
