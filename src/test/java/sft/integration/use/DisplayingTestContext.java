package sft.integration.use;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.decorators.Breadcrumb;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.DisplayContext;

import java.io.IOException;

@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
public class DisplayingTestContext {
    private JUnitHelper functionalTest;

    @Displayable
    private SftResources sftResources;

    @Test
    public void displayTestContextWithObjectsAnnotatedByDisplayable() throws IOException {
        allPrivateObjectsAnnotatedByDisplayableShouldBeDysplayedAfterScenario();
        displayableObjectsAreDisplayedOnlyIfThereAreNotNull();
        allDisplayableObjectsAreDisplayedAfterScenario();
        allDisplayableObjectsAreUnsetBetweenScenarios();
    }

    private void allPrivateObjectsAnnotatedByDisplayableShouldBeDysplayedAfterScenario() throws IOException {
        functionalTest = new JUnitHelper(this.getClass(),DisplayContext.class);
        sftResources = functionalTest.displayResources;
    }

    private void displayableObjectsAreDisplayedOnlyIfThereAreNotNull() {
        Element firstScenario = functionalTest.html.select("*.scenario").get(0);
        Assert.assertTrue(firstScenario.select("*.displayableContext").isEmpty());
    }

    private void allDisplayableObjectsAreDisplayedAfterScenario() {
        Element secondScenario = functionalTest.html.select("*.scenario").get(1);
        Element contextDisplayed = secondScenario.select("*.displayableContext").get(0);
        Assert.assertNotNull("Missing displayableContext", contextDisplayed);
        Elements texts = contextDisplayed.select(">div");
        Assert.assertEquals("first context display", texts.get(0).text());
        Assert.assertEquals("second context display", texts.get(1).text());
    }

    private void allDisplayableObjectsAreUnsetBetweenScenarios() {
        Element thirdScenario = functionalTest.html.select("*.scenario").get(2);

        Element contextDisplayed = thirdScenario.select("*.displayableContext").get(0);
        Assert.assertNotNull("Missing displayableContext", contextDisplayed);
        Elements texts = contextDisplayed.select(">div");
        Assert.assertEquals("second context display", texts.get(0).text());
    }



}
