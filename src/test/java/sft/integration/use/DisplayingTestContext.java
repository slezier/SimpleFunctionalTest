package sft.integration.use;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.Displayable;
import sft.FixturesHelper;
import sft.SimpleFunctionalTest;
import sft.decorators.Breadcrumb;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.JavaResource;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.DelegatedFixtures;
import sft.integration.use.sut.DisplayContext;
import sft.integration.use.sut.subUseCase.DisplayContextFromHelper;
import sft.integration.use.sut.subUseCase.DisplayableFixturesHelper;

import java.io.IOException;

@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
public class DisplayingTestContext {
    @FixturesHelper
    private JUnitHelper functionalTest=new JUnitHelper();

    @Displayable
    private String helperClassSource;

    @Test
    public void displayTestContextWithObjectsAnnotatedByDisplayable() throws IOException {
        allPrivateObjectsAnnotatedByDisplayableShouldBeDysplayedAfterScenario();
        displayableObjectsAreDisplayedOnlyIfThereAreNotNull();
        allDisplayableObjectsAreDisplayedAfterScenario();
        allDisplayableObjectsAreUnsetBetweenScenarios();
    }

    @Test
    public void usingDisplayableObjectFromFixturesHelper() throws IOException {
        aScenarioUsingFixturesHelper();
        displaysPublicFieldAnnotatedWithDisplayableAnnotationFromFixturesHelper();
    }

    private void aScenarioUsingFixturesHelper() throws IOException {
        functionalTest.run(this.getClass(),DisplayContextFromHelper.class);
        JavaResource fixtureHelperSource = new JavaResource(DisplayableFixturesHelper.class);
        helperClassSource ="<div class=\"resources\">"+
                fixtureHelperSource.getOpenResourceHtmlLink(this.getClass(), "helper", "alert-info")+ "</div>" ;

    }

    private void displaysPublicFieldAnnotatedWithDisplayableAnnotationFromFixturesHelper() {
        Element secondScenario = functionalTest.html.select("*.scenario").get(0);
        Element contextDisplayed = secondScenario.select("*.displayableContext").get(0);
        Assert.assertNotNull("Missing displayableContext", contextDisplayed);
        Elements texts = contextDisplayed.select(">div");
        Assert.assertEquals("It is displayed", texts.get(0).text());
    }

    private void allPrivateObjectsAnnotatedByDisplayableShouldBeDysplayedAfterScenario() throws IOException {
        functionalTest.run(this.getClass(),DisplayContext.class);
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
