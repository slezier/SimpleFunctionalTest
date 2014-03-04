package sft.integration.use;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.BreadcrumbDecoratorSample;
import sft.integration.use.sut.StyleDecoratorSample;
import sft.integration.use.sut.subUseCase.SubSubUseCaseBreadcrumb;
import sft.integration.use.sut.subUseCase.SubUseCaseBreadcrumb;

import java.util.ArrayList;


/*
 The annotation @Decorate allow to prettify your html report.
 */
@RunWith(SimpleFunctionalTest.class)
public class UsingDecorator {

    private SftResources sftResources;
    private SftResources sftResources2;
    private SftResources sftResources3;
    @Displayable
    private DisplayableResources displayableResources;
    private JUnitHelper jUnitHelper;

    @Test
    public void addStyle() throws Exception {
        byAddingStyleDecoratorWithParameterOnAElement("style");
        theElementWillHaveTheCssClass("style");
        severalStylesCanBeSpecified("style1", "style2", "style3");

        theStyleDecoratorCanBeApplyOnUseCaseScenarioAndFixture();
    }

    @Test
    public void addBreadcrumb() throws Exception {
        byAddingBreadcrumbDecoratorOnUseCase();
        aBreadcrumbsIsAdded();
        byClickingOnTheDifferentBreacrumbWeAccessTheGivenUseStory();
    }

    @Text("By adding a style decorator with parameter ${style}  on a element: @Decorate(decorator = Style.class, parameters =\"${style}\") ")
    private void byAddingStyleDecoratorWithParameterOnAElement(String style) throws Exception {
        jUnitHelper = new JUnitHelper(this.getClass(), StyleDecoratorSample.class, "target/sft-result/sft/integration/use/sut/StyleDecoratorSample.html");
        jUnitHelper.run();
        sftResources = jUnitHelper.displayResources();
        displayableResources = new DisplayableResources("", sftResources);
    }

    @Text("The element targeted of the html report will have the css class ${style}")
    private void theElementWillHaveTheCssClass(String style) throws Exception {
        Assert.assertTrue("Expecting class " + style + " in body", jUnitHelper.getHtmlReport().select("body.useCase").hasClass(style));
    }

    @Text("Several style can be used together @Decorate(decorator = Style.class, parameters ={\"${style1}\",\"${style2}\",\"${style3}\"}) ")
    private void severalStylesCanBeSpecified(String style1, String style2, String style3) throws Exception {
        final Elements select = jUnitHelper.getHtmlReport().select("div.scenario");
        Assert.assertTrue("Expecting class " + style1 + " in scenario div", select.hasClass(style1));
        Assert.assertTrue("Expecting class " + style2 + " in scenario div", select.hasClass(style2));
        Assert.assertTrue("Expecting class " + style3 + " in scenario div", select.hasClass(style3));
    }

    private void theStyleDecoratorCanBeApplyOnUseCaseScenarioAndFixture() throws Exception {
        final Document htmlReport = jUnitHelper.getHtmlReport();
        Assert.assertTrue("Expecting class style in body", htmlReport.select("body.useCase").hasClass("style"));
        Assert.assertTrue("Expecting class style1 in scenario div", htmlReport.select("div.scenario").hasClass("style1"));
        Assert.assertTrue("Expecting class style4 in instruction div", htmlReport.select("div.instruction").hasClass("style4"));
    }

    private void byAddingBreadcrumbDecoratorOnUseCase() throws Exception {
        jUnitHelper = new JUnitHelper(this.getClass(), BreadcrumbDecoratorSample.class, "target/sft-result/sft/integration/use/sut/BreadcrumbDecoratorSample.html");
        jUnitHelper.run();
        sftResources = jUnitHelper.displayResources();
        displayableResources = new DisplayableResources("parent user story", sftResources);
    }

    private void aBreadcrumbsIsAdded() throws Exception {
        Elements breadcrumbs = jUnitHelper.getHtmlReport().select("ol.breadcrumb");
        Assert.assertEquals(1, breadcrumbs.select("li").size());
        Assert.assertEquals("Breadcrumb decorator sample", breadcrumbs.select("li").get(0).text());

        jUnitHelper = new JUnitHelper(this.getClass(), SubUseCaseBreadcrumb.class, "target/sft-result/sft/integration/use/sut/subUseCase/SubUseCaseBreadcrumb.html");
        sftResources2 = jUnitHelper.displayResources();
        displayableResources.add("child user story", sftResources2);
        breadcrumbs = jUnitHelper.getHtmlReport().select("ol.breadcrumb");
        Assert.assertEquals(2, breadcrumbs.select("li").size());
        Assert.assertEquals("Breadcrumb decorator sample", breadcrumbs.select("li").get(0).text());
        Assert.assertEquals("Sub use case breadcrumb", breadcrumbs.select("li").get(1).text());

        jUnitHelper = new JUnitHelper(this.getClass(), SubSubUseCaseBreadcrumb.class, "target/sft-result/sft/integration/use/sut/subUseCase/SubSubUseCaseBreadcrumb.html");
        sftResources3 = jUnitHelper.displayResources();
        displayableResources.add("little child user story", sftResources3);
        breadcrumbs = jUnitHelper.getHtmlReport().select("ol.breadcrumb");
        Assert.assertEquals(3, breadcrumbs.select("li").size());
        Assert.assertEquals("Breadcrumb decorator sample", breadcrumbs.select("li").get(0).text());
        Assert.assertEquals("Sub use case breadcrumb", breadcrumbs.select("li").get(1).text());
        Assert.assertEquals("Sub sub use case breadcrumb", breadcrumbs.select("li").get(2).text());
    }

    private void byClickingOnTheDifferentBreacrumbWeAccessTheGivenUseStory() throws Exception {
        final Elements breadcrumbs = jUnitHelper.getHtmlReport().select("ol.breadcrumb");
        Assert.assertEquals(3, breadcrumbs.select("li").size());
        Assert.assertEquals("../BreadcrumbDecoratorSample.html", breadcrumbs.select("li").get(0).select("a").attr("href"));
        Assert.assertEquals("SubUseCaseBreadcrumb.html", breadcrumbs.select("li").get(1).select("a").attr("href"));
        Assert.assertTrue(breadcrumbs.select("li").get(2).classNames().contains("active"));
    }

    private class DisplayableResources {
        private ArrayList<String> labels = new ArrayList<String>();
        private ArrayList<SftResources> resources = new ArrayList<SftResources>();

        private DisplayableResources(String label, SftResources resources) {
            add(label, resources);
        }

        @Override
        public String toString() {
            if (labels.size() == 1) {
                return resources.get(0).toString();
            } else {
                String result = "";
                for (int i = 0; i < labels.size(); i++) {
                    result += labels.get(i) + ":" + resources.get(i).toString();
                }
                return result;
            }
        }

        private void add(String label, SftResources resources) {
            this.labels.add(label);
            this.resources.add(resources);
        }
    }


}
