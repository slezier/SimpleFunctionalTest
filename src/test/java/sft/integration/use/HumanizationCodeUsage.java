package sft.integration.use;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.use.sut.HumanizationCamelCaseClassNameHumanized;
import sft.integration.use.sut.HumanizationUsingTextAnnotation;
import sft.integration.use.sut.Humanization_underscore_class_name_humanized;

import java.io.IOException;

/*
    Class and methods could be humanized 3 different ways:
 */
@RunWith(SimpleFunctionalTest.class)
@Text("Humanized your code: using camelCase or underscore transformation or annotation")
public class HumanizationCodeUsage {

    @Test
    public void humanizationRules() throws IOException {
        camelCaseNameAllCaseChangesAreReplacedBySpaces();
        nameWithUnderscoreUnderscoreAreReplacedBySpace();
        textAnnotationsTextsAreDisplayedUnchanged();
    }

    @Text("Camel case name: all case changes are replaced by spaces (" +
            "<a href=\"../../../../../src/test/java/sft/integration/use/sut/HumanizationCamelCaseClassNameHumanized.java\">java</a>, " +
            "<a href=\"../../../../../target/sft-result/sft/integration/use/sut/HumanizationCamelCaseClassNameHumanized.html\">html</a>).")
    private void camelCaseNameAllCaseChangesAreReplacedBySpaces() throws IOException {
        JUnitHelper functionalTest = new JUnitHelper(HumanizationCamelCaseClassNameHumanized.class,"target/sft-result/sft/integration/use/sut/HumanizationCamelCaseClassNameHumanized.html");
        functionalTest.run();
        functionalTest.getHtmlReport();
        Assert.assertEquals("Humanization camel case class name humanized", functionalTest.getHtmlReport().select("*.useCaseName").text());
        Assert.assertEquals("Camel case scenario name humanized", functionalTest.getHtmlReport().select("*.scenarioName").text());
        Assert.assertEquals("Camel case fixture name humanized", functionalTest.getHtmlReport().select("*.instruction").text());
    }

    @Text("Name with underscore: all underscore are replaced by spaces (" +
            "<a href=\"../../../../../src/test/java/sft/integration/use/sut/Humanization_underscore_class_name_humanized.java\">java</a>, " +
            "<a href=\"../../../../../target/sft-result/sft/integration/use/sut/Humanization_underscore_class_name_humanized.html\">html</a>).")
    private void nameWithUnderscoreUnderscoreAreReplacedBySpace() throws IOException {
        JUnitHelper functionalTest = new JUnitHelper(Humanization_underscore_class_name_humanized.class,"target/sft-result/sft/integration/use/sut/Humanization_underscore_class_name_humanized.html");
        functionalTest.run();
        functionalTest.getHtmlReport();
        Assert.assertEquals("Humanization underscore class name humanized", functionalTest.getHtmlReport().select("*.useCaseName").text());
        Assert.assertEquals("Underscore scenario name humanized", functionalTest.getHtmlReport().select("*.scenarioName").text());
        Assert.assertEquals("Underscore fixture name humanized", functionalTest.getHtmlReport().select("*.instruction").text());
    }

    @Text("Text annotations: Texts are displayed unchanged(" +
            "<a href=\"../../../../../src/test/java/sft/integration/use/sut/HumanizationUsingTextAnnotation.java\">java</a>, " +
            "<a href=\"../../../../../target/sft-result/sft/integration/use/sut/HumanizationUsingTextAnnotation.html\">html</a>)")
    private void textAnnotationsTextsAreDisplayedUnchanged() throws IOException {
        JUnitHelper functionalTest = new JUnitHelper(HumanizationUsingTextAnnotation.class,"target/sft-result/sft/integration/use/sut/HumanizationUsingTextAnnotation.html");
        functionalTest.run();
        Assert.assertEquals("Use case name specified in @Text", functionalTest.getHtmlReport().select("*.useCaseName").text());
        Assert.assertEquals("Scenario name specified in @Text", functionalTest.getHtmlReport().select("*.scenarioName").text());
        Assert.assertEquals("Fixture name specified in @Text", functionalTest.getHtmlReport().select("*.instruction").text());
    }


}
