package sft.integration.use;

import org.junit.Assert;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.decorators.Breadcrumb;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.ParameterizedFixture;

import java.io.IOException;

/*
To allow fixture re-usability, you can use private or protected methods including parametersValues.<br/>
In @Text annotation ${xxx} will be replace by the value of the proper parameter.
*/
@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
@Text("Re-Use your fixtures in your class: parameterized fixtures")
public class UsingParameterizedFixture {

    private JUnitHelper jUnitHelper;
    private Elements parameterizedCalls;
    @Displayable
    private SftResources sftResources;

    @Test
    public void parameterizedFixture() throws IOException {
        byDeclaringParameterToYourFixtureYouCanReuseIt();
        theValueOfParameterIsIncludedInAnnotatedTextUsingParameterNamePrefixedByDollar();
        valuesOfFixtureCallsThatFailedAreIndicated();
    }

    private void byDeclaringParameterToYourFixtureYouCanReuseIt() throws IOException {
        jUnitHelper = new JUnitHelper(this.getClass(),ParameterizedFixture.class,"target/sft-result/sft/integration/use/sut/ParameterizedFixture.html");
        jUnitHelper.run();
        sftResources=jUnitHelper.displayResources();
        parameterizedCalls = jUnitHelper.getHtmlReport().select("*.instruction");

    }

    @Text("The value of parameter is included in annotated text using parameter name surround by dollar like ${parameterName}")
    private void theValueOfParameterIsIncludedInAnnotatedTextUsingParameterNamePrefixedByDollar() {
        Assert.assertEquals("2 is greater than 1", parameterizedCalls.get(0).text());
        Assert.assertEquals("500 is greater than 38", parameterizedCalls.get(1).text());
        Assert.assertEquals("1 is greater than 2", parameterizedCalls.get(2).text());
        Assert.assertEquals("2 is greater than 1", parameterizedCalls.get(3).text());
        Assert.assertEquals("2 is lower than 5",parameterizedCalls.get(4).text());
    }

    private void valuesOfFixtureCallsThatFailedAreIndicated() throws IOException {
        Assert.assertTrue("Expected successful call is not", parameterizedCalls.get(0).hasClass("succeeded"));
        Assert.assertTrue("Expected successful call is not", parameterizedCalls.get(1).hasClass("succeeded"));
        Assert.assertTrue("Expected failed call is not", parameterizedCalls.get(2).hasClass("failed"));
        Assert.assertTrue("Expected ignored call is not", parameterizedCalls.get(3).hasClass("ignored"));
        Assert.assertTrue("Expected ignored call is not", parameterizedCalls.get(4).hasClass("ignored"));

    }

}
