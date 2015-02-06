package sft.integration.use;

import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.Displayable;
import sft.FixturesHelper;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.decorators.Breadcrumb;
import sft.integration.fixtures.JUnitHtmlHelper;
import sft.integration.fixtures.JavaResource;
import sft.integration.use.sut.DelegatedFixtures;
import sft.integration.use.sut.FixturesHelperUsage;

import java.io.IOException;

/*
     Fixtures could be group into FixturesHelper, and then could be shared between use cases.
 */
@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
@Text("Re-Use your fixtures between your classes: fixtures helpers")
public class UsingFixturesHelper {

    @FixturesHelper
    private JUnitHtmlHelper jUnitHtmlHelper = new JUnitHtmlHelper();
    @Displayable
    private String helperClassSource;

    @Test
    public void usingFixturesHelper() throws IOException {
        toShareFixturesBetweenYourClassesWithoutUsingInheritanceYouHaveToWriteItsInAFixturesHelperClass();
        inTheUseCaseInstanciateThisFixturesHelperWithAnnotation();
        thenAllFixturesOfThisFixturesHelperClassAreSeenAsFixturesOfTheUseCaseClassAndCanBeUsedAsItsOwn();

    }

    private void toShareFixturesBetweenYourClassesWithoutUsingInheritanceYouHaveToWriteItsInAFixturesHelperClass() {
        JavaResource fixtureHelperSource = new JavaResource(DelegatedFixtures.class);
        helperClassSource ="<div class=\"resources\">"+
                fixtureHelperSource.getOpenResourceHtmlLink(this.getClass(), "helper", "alert-info")+ "</div>" ;
    }

    @Text("In the use case class add non-public field instantiating this fixtures helper class annotated by @FixturesHelper.")
    private void inTheUseCaseInstanciateThisFixturesHelperWithAnnotation() throws IOException {
        jUnitHtmlHelper.run(this.getClass(),FixturesHelperUsage.class);
    }

    private void thenAllFixturesOfThisFixturesHelperClassAreSeenAsFixturesOfTheUseCaseClassAndCanBeUsedAsItsOwn() throws IOException {
        Elements  delegatedFixtureCall = jUnitHtmlHelper.html.select("div.instruction span");
        Assert.assertEquals("First fixture",delegatedFixtureCall.get(0).text());
        Assert.assertEquals("Second fixture with parameter ABCDEFGHIJKLMNOPQRSTUVWXYZ and 99",delegatedFixtureCall.get(1).text());
    }
}
