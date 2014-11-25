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
import sft.integration.fixtures.CssParser;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.JavaResource;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.UseCaseLinks;
import sft.integration.use.sut.subUseCase.SubUseCaseFailed;
import sft.integration.use.sut.subUseCase.SubUseCaseIgnored;
import sft.integration.use.sut.subUseCase.SubUseCaseSucceeded;

import java.io.IOException;

/*
    Use cases can be link together by using public field.
 */
@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
public class LinksUseCasesTogether {

    private CssParser sftCss;

    @FixturesHelper
    private JUnitHelper jUnitHelper = new JUnitHelper();

    private Elements relatedUseCases;
    @Displayable
    private String subCasesJavaSources;

    @Test
    public void publicFieldAreEvaluateAsRelatedUseCase() throws IOException {
        byAddingPublicFieldsOfRelatedTestClassInYourFunctionalTestClass();

        whenInvokingJUnit();

        thenTheHtmlFilePresentLinksToRelatedUseCasesAsListItemInTheLastSection();

        successfullRelatedUseCaseAreDisplayedWithGreenCheckMark();
        failedRelatedUseCaseAreDisplayedWithRedCrossMark();
        ignoredRelatedUseCaseAreDisplayedWithYellowInterrogationMark();

    }

    private void byAddingPublicFieldsOfRelatedTestClassInYourFunctionalTestClass() throws IOException {
        jUnitHelper.run(this.getClass(), UseCaseLinks.class);

        JavaResource subSuccessfulUseCaseSource = new JavaResource(SubUseCaseSucceeded.class);
        JavaResource subFailedUseCaseSource = new JavaResource(SubUseCaseFailed.class);
        JavaResource subIgnoredUseCaseSource = new JavaResource(SubUseCaseIgnored.class);

        subCasesJavaSources = "<div class=\"resources\">" +
                subSuccessfulUseCaseSource.getOpenResourceHtmlLink(this.getClass(), "succeeded", "alert-success") +
                subFailedUseCaseSource.getOpenResourceHtmlLink(this.getClass(), "failed", "alert-danger") +
                subIgnoredUseCaseSource.getOpenResourceHtmlLink(this.getClass(), "ignored", "alert-warning") +
                "</div>";
    }

    @Text("When invoking JUnit")
    private void whenInvokingJUnit() {
        sftCss = new CssParser();
    }

    private void thenTheHtmlFilePresentLinksToRelatedUseCasesAsListItemInTheLastSection() throws IOException {
        relatedUseCases = jUnitHelper.html.select("*.relatedUseCase");
        Assert.assertEquals(3,relatedUseCases.size());
        Assert.assertEquals("subUseCase/SubUseCaseSucceeded.html",relatedUseCases.get(0).select("a").first().attr("href"));
        Assert.assertEquals("subUseCase/SubUseCaseFailed.html",relatedUseCases.get(1).select("a").first().attr("href"));
        Assert.assertEquals("subUseCase/SubUseCaseIgnored.html",relatedUseCases.get(2).select("a").first().attr("href"));
    }

    private void successfullRelatedUseCaseAreDisplayedWithGreenCheckMark() {
        Assert.assertTrue("Related use case without class 'succeeded'", relatedUseCases.get(0).hasClass("succeeded"));
        Assert.assertEquals("url(success_16.png)", sftCss.get("*.relatedUseCase.succeeded a span").getStyle().getPropertyCSSValue("background-image").getCssText());
    }

    private void failedRelatedUseCaseAreDisplayedWithRedCrossMark() {
        Assert.assertTrue("Related use case without class 'failed'", relatedUseCases.get(1).hasClass("failed"));
        Assert.assertEquals("url(failed_16.png)", sftCss.get("*.relatedUseCase.failed a span").getStyle().getPropertyCSSValue("background-image").getCssText());
    }

    private void ignoredRelatedUseCaseAreDisplayedWithYellowInterrogationMark() {
        Assert.assertTrue("Related use case without class 'ignored'", relatedUseCases.get(2).hasClass("ignored"));
        Assert.assertEquals("url(ignored_16.png)", sftCss.get("*.relatedUseCase.ignored a span").getStyle().getPropertyCSSValue("background-image").getCssText());
    }

}
