package sft.integration.use;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.DisplayContext;

import java.io.IOException;

@RunWith(SimpleFunctionalTest.class)
public class DisplayingTestContext {
    private JUnitHelper functionalTest;

    @Displayable
    private SftResources sftResources;
    private Document html;

    @Test
    public void displayTestContextWithObjectsAnnotatedByDisplayable() throws IOException {
        allPrivateObjectsAnnotatedByDisplayableShouldBeDysplayedAfterScenario();
        displayableObjectsAreDisplayedOnlyIfThereAreNotNull();
        allDisplayableObjectsAreDisplayedAfterScenario();
        allDisplayableObjectsAreUnsetBetweenScenarios();
    }

    private void allPrivateObjectsAnnotatedByDisplayableShouldBeDysplayedAfterScenario() throws IOException {
        functionalTest = new JUnitHelper(this.getClass(),DisplayContext.class, "target/sft-result/sft/integration/use/sut/DisplayContext.html");
        functionalTest.run();
        sftResources = functionalTest.displayResources();
        html = functionalTest.getHtmlReport();
    }

    private void displayableObjectsAreDisplayedOnlyIfThereAreNotNull() {
        Element firstScenario = html.select("*.scenario").get(0);
        Assert.assertTrue(firstScenario.select("*.displayableContext").isEmpty());
    }

    private void allDisplayableObjectsAreDisplayedAfterScenario() {
        Element secondScenario = html.select("*.scenario").get(1);
        Element contextDisplayed = secondScenario.select("*.displayableContext").get(0);
        Assert.assertNotNull("Missing displayableContext", contextDisplayed);
        Elements texts = contextDisplayed.select(">div");
        Assert.assertEquals("first context display", texts.get(0).text());
        Assert.assertEquals("second context display", texts.get(1).text());
    }

    private void allDisplayableObjectsAreUnsetBetweenScenarios() {
        Element thirdScenario = html.select("*.scenario").get(2);

        Element contextDisplayed = thirdScenario.select("*.displayableContext").get(0);
        Assert.assertNotNull("Missing displayableContext", contextDisplayed);
        Elements texts = contextDisplayed.select(">div");
        Assert.assertEquals("second context display", texts.get(0).text());
    }



}
