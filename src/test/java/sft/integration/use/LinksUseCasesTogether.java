package sft.integration.use;

import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.CssParser;
import sft.integration.fixtures.FileSystem;
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
public class LinksUseCasesTogether {

    private CssParser sftCss;

    private JUnitHelper functionalTest;
    private Elements relatedUseCases;
    @Displayable
    private String subCasesJavaSources;
    @Displayable
    private SftResources sftResources;

    @Test
    public void publicFieldAreEvaluateAsRelatedUseCase() throws IOException {
        byAddingPublicFieldsOfRelatedTestClassInYourFunctionalTestClass();

        whenInvokingJUnit();

        thenTheHtmlFilePresentRelatedUseCasesAsListItemInTheLastSection();

        successfullRelatedUseCaseAreDisplayedWithGreenCheckMark();
        failedRelatedUseCaseAreDisplayedWithRedCrossMark();
        ignoredRelatedUseCaseAreDisplayedWithYellowInterrogationMark();

    }

    private void byAddingPublicFieldsOfRelatedTestClassInYourFunctionalTestClass() {
        functionalTest = new JUnitHelper(this.getClass(),UseCaseLinks.class, "target/sft-result/sft/integration/use/sut/UseCaseLinks.html");

        JavaResource subSuccessfulUseCaseSource = new JavaResource(SubUseCaseSucceeded.class);
        JavaResource subFailedUseCaseSource = new JavaResource(SubUseCaseFailed.class);
        JavaResource subIgnoredUseCaseSource = new JavaResource(SubUseCaseIgnored.class);

        subCasesJavaSources = "<div class=\"resources\">"+
                subSuccessfulUseCaseSource.getOpenResourceHtmlLink(this.getClass(), "succeeded", "alert-success") +
                subFailedUseCaseSource.getOpenResourceHtmlLink(this.getClass(), "failed", "alert-danger") +
                subIgnoredUseCaseSource.getOpenResourceHtmlLink(this.getClass(), "ignored", "alert-warning") +
                "</div>" ;
    }

    @Text("When invoking JUnit")
    private void whenInvokingJUnit() {
        functionalTest.run();
        sftResources = functionalTest.displayResources();
        sftCss = new CssParser();
    }

    private void thenTheHtmlFilePresentRelatedUseCasesAsListItemInTheLastSection() throws IOException {
        relatedUseCases = functionalTest.getHtmlReport().select("*.relatedUseCase");
    }

    private void successfullRelatedUseCaseAreDisplayedWithGreenCheckMark() {
        Assert.assertTrue("Related use case without class 'succeeded'", relatedUseCases.get(0).hasClass("succeeded"));
        Assert.assertEquals("url(success_16.png)", sftCss.get("*.relatedUseCase.succeeded a span").getStyle().getPropertyCSSValue("background-image").getCssText());
        FileSystem.filesExists("target/sft-result/success_16.png");
    }

    private void failedRelatedUseCaseAreDisplayedWithRedCrossMark() {
        Assert.assertTrue("Related use case without class 'failed'", relatedUseCases.get(1).hasClass("failed"));
        Assert.assertEquals("url(failed_16.png)", sftCss.get("*.relatedUseCase.failed a span").getStyle().getPropertyCSSValue("background-image").getCssText());
        FileSystem.filesExists("target/sft-result/failed_16.png");
    }


    private void ignoredRelatedUseCaseAreDisplayedWithYellowInterrogationMark() {
        Assert.assertTrue("Related use case without class 'ignored'", relatedUseCases.get(2).hasClass("ignored"));
        Assert.assertEquals("url(ignored_16.png)", sftCss.get("*.relatedUseCase.ignored a span").getStyle().getPropertyCSSValue("background-image").getCssText());
        FileSystem.filesExists("target/sft-result/ignored_16.png");
    }

}
