package sft.integration.use;

import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.use.sut.FixturesHelperUsage;

import java.io.IOException;

/*

     Fixtures could be group into FixturesHelper, and then could be shared between use cases.

    <div>
        <table class="table">
            <tr>
                <th>Java/JUnit item</th>
                <th>Functional test item</th>
                <th>HTML item</th>
            <tr/>
            <tr>
                <td>Private or protected field with @FixturesHelper annotation</td>
                <td>Fixtures helper</td>
                <td>HTML line corresponding of FixturesHelper fixture name</td>
            <tr/>
        </table>
    </div>
 */
@RunWith(SimpleFunctionalTest.class)
@Text("Re-Use your fixtures between your classes: fixtures helpers")
public class UsingFixturesHelper {

    private JUnitHelper jUnitHelper;

    @Test
    public void usingFixturesHelper() throws IOException {
        toShareFixturesBetweenYourClassesYouHaveToWriteFixturesHelperClass();
        inTheUseCaseInstanciateThisFixturesHelperWithAnnotation();
        fixturesOfHelperAreUsedByUseCaseAsItsOwn();
    }

    @Text("To share fixtures between your classes without using inheritance, you have to write its in a new class: <a href=\"../../../../../src/test/java/sft/integration/use/sut/DelegatedFixtures.java\">a fixtures helper class</a>.")
    private void toShareFixturesBetweenYourClassesYouHaveToWriteFixturesHelperClass() {
    }

    @Text("In the <a href=\"../../../../../src/test/java/sft/integration/use/sut/FixturesHelperUsage.java\">use case class</a> add non-public field instantiating this fixtures helper class annotated by @FixturesHelper.")
    private void inTheUseCaseInstanciateThisFixturesHelperWithAnnotation() {
        jUnitHelper = new JUnitHelper(FixturesHelperUsage.class, "target/sft-result/sft/integration/use/sut/FixturesHelperUsage.html");
        jUnitHelper.run();
    }

    @Text("Then all fixtures of this fixtures helper class are seen as fixtures of the use-case class and  <a href=\"../../../../../target/sft-result/sft/integration/use/sut/FixturesHelperUsage.html\">can be used as its own</a>.")
    private void fixturesOfHelperAreUsedByUseCaseAsItsOwn() throws IOException {
        Document htmlReport = jUnitHelper.getHtmlReport();
        Elements  delegatedFixtureCall = htmlReport.select("div.instruction span");
        Assert.assertEquals("First fixture",delegatedFixtureCall.get(0).text());
        Assert.assertEquals("Second fixture with parameter \"ABCDEFGHIJKLMNOPQRSTUVWXYZ\" and 99",delegatedFixtureCall.get(1).text());
    }
}
