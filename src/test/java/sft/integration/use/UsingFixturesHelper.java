package sft.integration.use;

import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.DelegatedFixtures;
import sft.integration.use.sut.FixturesHelperUsage;

import java.io.IOException;

/*
     Fixtures could be group into FixturesHelper, and then could be shared between use cases.
 */
@RunWith(SimpleFunctionalTest.class)
@Text("Re-Use your fixtures between your classes: fixtures helpers")
public class UsingFixturesHelper {

    private JUnitHelper jUnitHelper;
    @Displayable
    private String helperClassSource;
    @Displayable
    private SftResources sftResources;


    @Test
    public void usingFixturesHelper() throws IOException {
        toShareFixturesBetweenYourClassesWithoutUsingInheritanceYouHaveToWriteItsInAFixturesHelperClass();
        inTheUseCaseInstanciateThisFixturesHelperWithAnnotation();
        thenAllFixturesOfThisFixturesHelperClassAreSeenAsFixturesOfTheUseCaseClassAndCanBeUsedAsItsOwn();
    }

    private void toShareFixturesBetweenYourClassesWithoutUsingInheritanceYouHaveToWriteItsInAFixturesHelperClass() {
        helperClassSource ="<div class=\"resources\">"+SftResources.getOpenResourceHtmlLink(this.getClass(),"helper", new SftResources(this.getClass(), DelegatedFixtures.class).getLinkToSource(),"alert-info")+ "</div>" ;
    }

    @Text("In the use case class add non-public field instantiating this fixtures helper class annotated by @FixturesHelper.")
    private void inTheUseCaseInstanciateThisFixturesHelperWithAnnotation() {
        jUnitHelper = new JUnitHelper(this.getClass(),FixturesHelperUsage.class, "target/sft-result/sft/integration/use/sut/FixturesHelperUsage.html");
        jUnitHelper.run();
        sftResources=jUnitHelper.displayResources();
    }

    private void thenAllFixturesOfThisFixturesHelperClassAreSeenAsFixturesOfTheUseCaseClassAndCanBeUsedAsItsOwn() throws IOException {
        Document htmlReport = jUnitHelper.getHtmlReport();
        Elements  delegatedFixtureCall = htmlReport.select("div.instruction span");
        Assert.assertEquals("First fixture",delegatedFixtureCall.get(0).text());
        Assert.assertEquals("Second fixture with parameter \"ABCDEFGHIJKLMNOPQRSTUVWXYZ\" and 99",delegatedFixtureCall.get(1).text());
    }
}
