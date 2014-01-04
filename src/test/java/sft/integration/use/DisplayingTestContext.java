package sft.integration.use;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.use.sut.DisplayContext;

import java.io.IOException;

@RunWith(SimpleFunctionalTest.class)
public class DisplayingTestContext {
    /*
    @Display(AS_STRING)
    private Object displayContext1;

    @Display(AS_PROPERTY)
    private Object displayContext2;
     */

    private JUnitHelper functionalTest;

    @Test
    public void displayTestContextWithAnObjectAnnotatedByDisplayable() throws IOException {
        allPrivateObjectsAnnotatedByDisplayable();
        areDisplayedAfterEachFixtureCallAndThenUnset();
    }

    private void allPrivateObjectsAnnotatedByDisplayable() {
        functionalTest = new JUnitHelper(DisplayContext.class, "target/sft-result/sft/integration/use/sut/DisplayContext.html");
        functionalTest.run();
    }

    private void areDisplayedAfterEachFixtureCallAndThenUnset() throws IOException {
        Document html = functionalTest.getHtmlReport();

        Element firstInstruction = html.select("*.instruction").get(0);
        Element secondInstruction = firstInstruction.nextElementSibling();
        Assert.assertTrue("Second element of the scenario should be an instruction", secondInstruction.hasClass("instruction"));
        Element contextDisplayed = secondInstruction.nextElementSibling();
        Assert.assertTrue("Third element of the scenario should be an contextDisplayed", contextDisplayed.hasClass("displayableContext"));
        Assert.assertEquals("context display",contextDisplayed.text());
    }

}
