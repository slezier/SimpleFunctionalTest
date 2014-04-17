package sft.integration.use;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.decorators.Breadcrumb;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.decorators.*;
import sft.integration.use.sut.decorators.subUseCase.SubSubUseCaseBreadcrumb;
import sft.integration.use.sut.decorators.subUseCase.SubUseCaseBreadcrumb;

import java.io.IOException;
import java.util.ArrayList;


/*
 The annotation @Decorate allow to prettify your html report.
 */
@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
public class UsingDecorator {

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
        aBreadcrumbsIsAddedAfterTitle();
        byClickingOnTheDifferentBreacrumbWeAccessTheGivenUseStory();
    }

    @Test
    public void addTableOfContent() throws Exception {
        byAddingTableOfContentDecoratorOnUseCase();
        aTableOfContentIsAddedAfterTitleShowingTestsIssueAndAllowAccessToTheGivenStories();
    }

    @Test
    public void groupFixtures() throws Exception {
        byAddingAGroupDecoratorWithParameterOnFixtures("When");
        fixturesWithTheSameGroupNameAreShownInASpecificParagraphWithThisNameAsTitle("When");
    }

    @Test
    public void displayFixtureAsTable() throws IOException {
        byAddingATableDecoratorOnFixture("Addition sample");
        fixturesCallAreAggregatedInATable();
        tableTitleIs("Addition sample");
        columnHeaderAreFieldNames();
        eachValuesAreDisplayed();
        theIssueOfEachFixtureCallsIsAddedAtEachRow();
    }

    private void theIssueOfEachFixtureCallsIsAddedAtEachRow() throws IOException {
        Document htmlReport = jUnitHelper.getHtmlReport();
        Elements raws = htmlReport.select("div.panel-body > table > tbody > tr ");
        Assert.assertTrue(raws.get(0).select("td").get(3).hasClass("succeeded"));
        Assert.assertTrue(raws.get(1).select("td").get(3).hasClass("succeeded"));
        Assert.assertTrue(raws.get(2).select("td").get(3).hasClass("failed"));
        Assert.assertTrue(raws.get(3).select("td").get(3).hasClass("ignored"));
    }

    private void columnHeaderAreFieldNames() throws IOException {
        Document htmlReport = jUnitHelper.getHtmlReport();
        Elements columnHeaders = htmlReport.select("div.panel-body > table > thead > tr > th");
        Assert.assertEquals(4, columnHeaders.size());
        Assert.assertEquals("first", columnHeaders.get(0).text());
        Assert.assertEquals("second", columnHeaders.get(1).text());
        Assert.assertEquals("sum", columnHeaders.get(2).text());
        Assert.assertEquals("", columnHeaders.get(3).text());
    }

    private void eachValuesAreDisplayed() throws IOException {
        Document htmlReport = jUnitHelper.getHtmlReport();
        Elements raws = htmlReport.select("div.panel-body > table > tbody > tr ");
        Assert.assertEquals(4, raws.size());
        Assert.assertEquals("1", raws.get(0).select("td").get(0).text());
        Assert.assertEquals("1", raws.get(0).select("td").get(1).text());
        Assert.assertEquals("2", raws.get(0).select("td").get(2).text());

        Assert.assertEquals("8", raws.get(2).select("td").get(0).text());
        Assert.assertEquals("2", raws.get(2).select("td").get(1).text());
        Assert.assertEquals("9", raws.get(2).select("td").get(2).text());
    }

    @Text("The table title is ${tableTitle} ")
    private void tableTitleIs(String tableTitle) throws IOException {
        Document htmlReport = jUnitHelper.getHtmlReport();
        Elements title = htmlReport.select("div.panel-body > table > caption");
        Assert.assertEquals(tableTitle,title.text());
    }

    private void fixturesCallAreAggregatedInATable() throws IOException {
        Document htmlReport = jUnitHelper.getHtmlReport();
        Elements table = htmlReport.select("div.panel-body > table");
        Assert.assertEquals(1, table.size());
        Elements rows = table.select(" tbody > tr");
        Assert.assertEquals(4, rows.size());
    }

    @Text("By adding a table decorator with parameter ${tableTitle} on a fixture: @Decorate(decorator = Table.class, parameters =\"${tableTitle}\") ")
    private void byAddingATableDecoratorOnFixture(String tableTitle) {
        jUnitHelper = new JUnitHelper(this.getClass(), TableDecoratorSample.class, "target/sft-result/sft/integration/use/sut/decorators/TableDecoratorSample.html");
        jUnitHelper.run();
        displayableResources = new DisplayableResources("", jUnitHelper.displayResources());
    }

    @Text("By adding a group decorator with parameter ${name}  on fixtures: @Decorate(decorator = Group.class, parameters =\"${name}\") ")
    private void byAddingAGroupDecoratorWithParameterOnFixtures(String name) throws Exception {
        jUnitHelper = new JUnitHelper(this.getClass(), GroupDecoratorSample.class, "target/sft-result/sft/integration/use/sut/decorators/GroupDecoratorSample.html");
        jUnitHelper.run();
        displayableResources = new DisplayableResources("", jUnitHelper.displayResources());
    }

    private void fixturesWithTheSameGroupNameAreShownInASpecificParagraphWithThisNameAsTitle(String name) throws IOException {
        final Elements groups = jUnitHelper.getHtmlReport().select("div.panel-body > div");
        Assert.assertEquals(3,groups.size());
        Assert.assertEquals(name,groups.get(1).select("h4").text());
        Assert.assertEquals("I ask something",groups.get(1).select("*.instruction").get(0).text());
        Assert.assertEquals("And required another action",groups.get(1).select("*.instruction").get(1).text());
    }

    @Text("By adding a style decorator with parameter ${style}  on a element: @Decorate(decorator = Style.class, parameters =\"${style}\") ")
    private void byAddingStyleDecoratorWithParameterOnAElement(String style) throws Exception {
        jUnitHelper = new JUnitHelper(this.getClass(), StyleDecoratorSample.class, "target/sft-result/sft/integration/use/sut/decorators/StyleDecoratorSample.html");
        jUnitHelper.run();
        displayableResources = new DisplayableResources("", jUnitHelper.displayResources());
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
        jUnitHelper = new JUnitHelper(this.getClass(), BreadcrumbDecoratorSample.class, "target/sft-result/sft/integration/use/sut/decorators/BreadcrumbDecoratorSample.html");
        jUnitHelper.run();
        displayableResources = new DisplayableResources("parent user story", jUnitHelper.displayResources());
    }

    private void aBreadcrumbsIsAddedAfterTitle() throws Exception {
        Elements breadcrumbs = jUnitHelper.getHtmlReport().select("ol.breadcrumb");
        Assert.assertEquals(1, breadcrumbs.select("li").size());
        Assert.assertEquals("Breadcrumb decorator sample", breadcrumbs.select("li").get(0).text());

        jUnitHelper = new JUnitHelper(this.getClass(), SubUseCaseBreadcrumb.class, "target/sft-result/sft/integration/use/sut/decorators/subUseCase/SubUseCaseBreadcrumb.html");
        displayableResources.add("child user story", jUnitHelper.displayResources());
        breadcrumbs = jUnitHelper.getHtmlReport().select("ol.breadcrumb");
        Assert.assertEquals(2, breadcrumbs.select("li").size());
        Assert.assertEquals("Breadcrumb decorator sample", breadcrumbs.select("li").get(0).text());
        Assert.assertEquals("Sub use case breadcrumb", breadcrumbs.select("li").get(1).text());

        jUnitHelper = new JUnitHelper(this.getClass(), SubSubUseCaseBreadcrumb.class, "target/sft-result/sft/integration/use/sut/decorators/subUseCase/SubSubUseCaseBreadcrumb.html");
        displayableResources.add("little child user story", jUnitHelper.displayResources());
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

    private void byAddingTableOfContentDecoratorOnUseCase() {
        jUnitHelper = new JUnitHelper(this.getClass(), TocDecoratorSample.class, "target/sft-result/sft/integration/use/sut/decorators/TocDecoratorSample.html");
        jUnitHelper.run();
        displayableResources = new DisplayableResources("", jUnitHelper.displayResources());
    }

    private void aTableOfContentIsAddedAfterTitleShowingTestsIssueAndAllowAccessToTheGivenStories() throws Exception {
        Elements elementsLevel1 = jUnitHelper.getHtmlReport().select("div.toc > ol > li");
        Assert.assertEquals(3, elementsLevel1.size());

        assertTocElement(elementsLevel1.get(0), "succeeded", "Sub use case toc 1", "subUseCase/SubUseCaseToc1.html");
        assertScenariosAre(elementsLevel1.get(0), "succeeded", "Scenario 11", "succeeded", "Scenario 12", "succeeded", "Scenario 13");
        assertTocElement(elementsLevel1.get(1), "failed", "Sub use case toc 2", "subUseCase/SubUseCaseToc2.html");
        assertScenariosAre(elementsLevel1.get(1), "succeeded", "Scenario 21", "succeeded", "Scenario 22", "succeeded", "Scenario 23");

        Elements elementsLevel2 = elementsLevel1.get(1).select("> ol > li");
        Assert.assertEquals(3, elementsLevel2.size());

        assertTocElement(elementsLevel2.get(0), "succeeded", "Sub use case toc 2a", "subUseCase/SubUseCaseToc2a.html");
        assertScenariosAre(elementsLevel2.get(0), "succeeded", "Scenario 2a 1");
        assertTocElement(elementsLevel2.get(1), "ignored", "Sub use case toc 2b", "subUseCase/SubUseCaseToc2b.html");
        assertScenariosAre(elementsLevel2.get(1), "ignored", "Scenario 2b 1");
        assertTocElement(elementsLevel2.get(2), "failed", "Sub use case toc 2c", "subUseCase/SubUseCaseToc2c.html");
        assertScenariosAre(elementsLevel2.get(2), "failed", "Scenario 2c 1");

        assertTocElement(elementsLevel1.get(2), "failed", "Sub use case toc 3", "subUseCase/SubUseCaseToc3.html");
        assertScenariosAre(elementsLevel1.get(2), "succeeded", "Scenario 31", "ignored", "Scenario 32", "failed", "Scenario 33");


    }

    private void assertScenariosAre(Element element, String... listOfTitleThenIssues) {
        final Elements scenarios = element.select("> ul > li");
        final int nbOfScenarios = listOfTitleThenIssues.length / 2;
        Assert.assertEquals(nbOfScenarios,scenarios.size());
        for (int i = 0; i < nbOfScenarios; i++) {
            final String issue = listOfTitleThenIssues[i * 2];
            final String title = listOfTitleThenIssues[i * 2 + 1];
            final Element scenario = scenarios.get(i);

            Assert.assertTrue("expecting for scenario #" + i + " ("+title+") issue " + issue + " having " + scenario.className(), scenario.hasClass(issue));
            Assert.assertEquals(title, scenario.text());
        }

    }

    private void assertTocElement(Element tocElement, String issue, String title, String href) {
        Assert.assertTrue("expecting '" + issue + "' having '" + tocElement.className() + "'", tocElement.hasClass(issue));
        Assert.assertEquals(href, tocElement.select("span a").attr("href"));
        Assert.assertEquals(title, tocElement.select("span a").get(0).text());
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
