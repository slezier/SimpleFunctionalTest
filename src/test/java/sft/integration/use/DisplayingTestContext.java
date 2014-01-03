package sft.integration.use;

import org.jsoup.nodes.Document;
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

    @Ignore
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

        Elements select = html.select("*.instruction");

        Assert.assertTrue("Instruction shouldn't display context", select.get(0).select("*.contextDisplayed").isEmpty());
        Elements expectedContextDisplayed = select.get(1).select("*.contextDisplayed");
        Assert.assertFalse("Instruction should display context", expectedContextDisplayed.isEmpty());
        Assert.assertEquals("context displayed", expectedContextDisplayed);
        Assert.assertTrue("Instruction shouldn't display context", select.get(2).select("*.contextDisplayed").isEmpty());
    }

}
