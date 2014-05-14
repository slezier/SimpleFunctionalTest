package sft.integration.use;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.Displayable;
import sft.FixturesHelper;
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


    @FixturesHelper
    private JUnitHelper jUnitHelper = new JUnitHelper();


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
        jUnitHelper.run(this.getClass(), HumanizationCamelCaseClassNameHumanized.class);
        Assert.assertEquals("Humanization camel case class name humanized", jUnitHelper.html.select("*.useCaseName").text());
        Assert.assertEquals("Camel case scenario name humanized", jUnitHelper.html.select("*.scenarioName").text());
        Assert.assertEquals("Camel case fixture name humanized", jUnitHelper.html.select("*.instruction").text());
    }

    private void underscoreAreReplacedBySpace() throws IOException {
        jUnitHelper.run(this.getClass(), Humanization_underscore_class_name_humanized.class);
        Assert.assertEquals("Humanization underscore class name humanized", jUnitHelper.html.select("*.useCaseName").text());
        Assert.assertEquals("Underscore scenario name humanized", jUnitHelper.html.select("*.scenarioName").text());
        Assert.assertEquals("Underscore fixture name humanized", jUnitHelper.html.select("*.instruction").text());
    }

    private void textsAreDisplayedUnchanged() throws IOException {
        jUnitHelper.run(this.getClass(),HumanizationUsingTextAnnotation.class);
        Assert.assertEquals("Use case name specified in @Text", jUnitHelper.html.select("*.useCaseName").text());
        Assert.assertEquals("Scenario name specified in @Text", jUnitHelper.html.select("*.scenarioName").text());
        Assert.assertEquals("Fixture name specified in @Text", jUnitHelper.html.select("*.instruction").text());
    }


}
