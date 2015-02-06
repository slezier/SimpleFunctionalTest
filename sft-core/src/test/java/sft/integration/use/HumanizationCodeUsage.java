package sft.integration.use;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.FixturesHelper;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.decorators.Breadcrumb;
import sft.integration.fixtures.JUnitHtmlHelper;
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


    @FixturesHelper
    private JUnitHtmlHelper jUnitHtmlHelper = new JUnitHtmlHelper();


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
        jUnitHtmlHelper.run(this.getClass(), HumanizationCamelCaseClassNameHumanized.class);
        Assert.assertEquals("Humanization camel case class name humanized", jUnitHtmlHelper.html.select("*.useCaseName").text());
        Assert.assertEquals("Camel case scenario name humanized", jUnitHtmlHelper.html.select("*.scenarioName").text());
        Assert.assertEquals("Camel case fixture name humanized", jUnitHtmlHelper.html.select("*.instruction").text());
    }

    private void underscoreAreReplacedBySpace() throws IOException {
        jUnitHtmlHelper.run(this.getClass(), Humanization_underscore_class_name_humanized.class);
        Assert.assertEquals("Humanization underscore class name humanized", jUnitHtmlHelper.html.select("*.useCaseName").text());
        Assert.assertEquals("Underscore scenario name humanized", jUnitHtmlHelper.html.select("*.scenarioName").text());
        Assert.assertEquals("Underscore fixture name humanized", jUnitHtmlHelper.html.select("*.instruction").text());
    }

    private void textsAreDisplayedUnchanged() throws IOException {
        jUnitHtmlHelper.run(this.getClass(),HumanizationUsingTextAnnotation.class);
        Assert.assertEquals("Use case name specified in @Text", jUnitHtmlHelper.html.select("*.useCaseName").text());
        Assert.assertEquals("Scenario name specified in @Text", jUnitHtmlHelper.html.select("*.scenarioName").text());
        Assert.assertEquals("Fixture name specified in @Text", jUnitHtmlHelper.html.select("*.instruction").text());
    }


}
