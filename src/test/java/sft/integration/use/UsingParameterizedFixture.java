package sft.integration.use;

import org.junit.Assert;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.use.sut.ParameterizedFixture;

import java.io.IOException;

/*
To allow fixture re-usability, you can use private or protected methods including parameters.<br/>
In @Text annotation $name will be replace by the value of the parameter 'name' of the fixture.
*/
@RunWith(SimpleFunctionalTest.class)
@Text("Re-Use your fixtures in your class: parameterized fixtures")
public class UsingParameterizedFixture {

    private JUnitHelper jUnitHelper;
    private Elements parameterizedCalls;

    @Test
    public void parameterizedFixture() throws IOException {
        byDeclaringParameterToYourFixtureYouCanReuseIt();
        theValueOfParameterIsIncludedInAnnotatedTextUsingParameterIndexPrefixedByDollar();
        fixtureCallThatFailedIsIndicated();
    }

    @Text("By declaring parameter to your <a href=\"../../../../../src/test/java/sft/integration/use/sut/ParameterizedFixture.java\">fixture</a> you can reuse it.")
    private void byDeclaringParameterToYourFixtureYouCanReuseIt() throws IOException {
        jUnitHelper = new JUnitHelper(ParameterizedFixture.class,"target/sft-result/sft/integration/use/sut/ParameterizedFixture.html");
        jUnitHelper.run();
        parameterizedCalls = jUnitHelper.getHtmlReport().select("*.instruction");

    }

    @Text("The value of parameter is <a href=\"../../../../../target/sft-result/sft/integration/use/sut/ParameterizedFixture.html\">included</a> in annotated text using parameter index as ${index} or using @Parameter annotation as ${parameterName} .")
    private void theValueOfParameterIsIncludedInAnnotatedTextUsingParameterIndexPrefixedByDollar() {
        Assert.assertEquals("2 is greater than 1", parameterizedCalls.get(0).text());
        Assert.assertEquals("500 is greater than 38", parameterizedCalls.get(1).text());
        Assert.assertEquals("1 is greater than 2", parameterizedCalls.get(2).text());
        Assert.assertEquals("2 is greater than 1", parameterizedCalls.get(3).text());
        Assert.assertEquals("2 is lower than 5",parameterizedCalls.get(4).text());
    }

    private void fixtureCallThatFailedIsIndicated() throws IOException {
        Assert.assertTrue("Expected successful call is not", parameterizedCalls.get(0).hasClass("succeeded"));
        Assert.assertTrue("Expected successful call is not", parameterizedCalls.get(1).hasClass("succeeded"));
        Assert.assertTrue("Expected failed call is not", parameterizedCalls.get(2).hasClass("failed"));
        Assert.assertTrue("Expected ignored call is not", parameterizedCalls.get(3).hasClass("ignored"));
        Assert.assertTrue("Expected ignored call is not", parameterizedCalls.get(4).hasClass("ignored"));

    }

}
