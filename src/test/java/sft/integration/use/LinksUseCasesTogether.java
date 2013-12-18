package sft.integration.use;

import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.CssParser;
import sft.integration.fixtures.FileSystem;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.use.sut.UseCaseLinks;

import java.io.IOException;

/*
    Use cases can be link together by using public field.

    <div>
        <table class="table">
            <tr>
                <th>Java/JUnit item</th>
                <th>Functional test item</th>
                <th>HTML item</th>
            <tr/>
            <tr>
                <td>Public field</td>
                <td>Related useCase</td>
                <td>HTML link</td>
            <tr/>
        </table>
    </div>
 */
@RunWith(SimpleFunctionalTest.class)
public class LinksUseCasesTogether {

    private CssParser sftCss;

    private JUnitHelper functionalTest;
    private Elements relatedUseCases;

    @Test
    public void publicFieldAreEvaluateAsRelatedUseCase() throws IOException {
        byAddingPublicFieldsInYourFunctionalTestClass();

        whenInvokingJUnit();

        thenHtmlFilePresentRelatedUseCasesAsListItemInTheLastSection();

        successfullRelatedUseCaseAreDisplayedWithGreenCheckMark();
        failedRelatedUseCaseAreDisplayedWithRedCrossMark();
        ignoredRelatedUseCaseAreDisplayedWithYellowInterrogationMark();

    }

    @Text("By adding public fields of related test class " +
            "(<a href=\"../../../../../src/test/java/sft/integration/use/sut/subUseCase/SubUseCaseSucceeded.java\">succeeded</a>," +
            "<a href=\"../../../../../src/test/java/sft/integration/use/sut/subUseCase/SubUseCaseFailed.java\">failed</a>," +
            "<a href=\"../../../../../src/test/java/sft/integration/use/sut/subUseCase/SubUseCaseIgnored.java\">ignored</a>) " +
            "in your functional <a href=\"../../../../../src/test/java/sft/integration/use/sut/UseCaseLinks.java\">test class</a> ")
    private void byAddingPublicFieldsInYourFunctionalTestClass() {
        functionalTest = new JUnitHelper(UseCaseLinks.class, "target/sft-result/sft/integration/use/sut/UseCaseLinks.html");
    }

    @Text("When invoking JUnit")
    private void whenInvokingJUnit() {
        functionalTest.run();
        sftCss = new CssParser();
    }

    @Text("Then the html file present related <a href=\"../../../../../target/sft-result/sft/integration/use/sut/UseCaseLinks.html\">use cases</a> as list item in the last section")
    private void thenHtmlFilePresentRelatedUseCasesAsListItemInTheLastSection() throws IOException {
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
