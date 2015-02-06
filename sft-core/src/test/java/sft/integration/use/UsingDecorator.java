package sft.integration.use;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.decorators.*;
import sft.integration.use.sut.decorators.subUseCase.SubSubUseCaseBreadcrumb;
import sft.integration.use.sut.decorators.subUseCase.SubUseCaseBreadcrumb;

import java.io.IOException;
import java.util.ArrayList;


/*
 The annotation @Decorate allow to prettify your html report.<br/><br/>

 Usage:
 <pre>@Decorate(decorator=MyDecorator.class ) </pre><br/>
 or with a parameter <br/>
 <pre>@Decorate(decorator=MyDecorator.class , parameters = "value" )</pre><br/>
 or with parameters <br/>
 <pre>@Decorate(decorator=MyDecorator.class , parameters = {"value1","value2","value3"} )</pre><br/>
 */
@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
public class UsingDecorator {

    @FixturesHelper
    private JUnitHtmlHelper jUnitHtmlHelper = new JUnitHtmlHelper();
    @Displayable
    private DisplayableResources displayableResources;

    @Test
    public void addStyle() throws Exception {
        byAddingStyleDecoratorWithParameterOnAElement("style");
        theElementWillHaveTheCssClass("style");
        severalStylesCanBeSpecified("style1", "style2", "style3");

        theStyleDecoratorCanBeApplyOnUseCaseScenarioFixtureAndSubUseCase();
    }

    @Test
    public void addBreadcrumb() throws Exception {
        byAddingBreadcrumbDecoratorOnUseCase();
        aBreadcrumbsIsAddedAfterTitle();
        byClickingOnTheDifferentBreadcrumbWeAccessTheGivenUseStory();
    }

    @Test
    public void addTableOfContent() throws Exception {
        byAddingTableOfContentDecoratorOnUseCase();
        aTableOfContentIsAddedAfterTitleShowingTestsIssueAndAllowAccessToTheGivenStories();
    }

    @Test
    public void addTestsSynthesis() throws Exception {
        byAddingSynthesisDecoratorOnUseCase();
        allScenarioRanAreListedDependingTheirsIssue();
        failedScenariosAreNumberedAndDisplayedWithDirectAccess();
        ignoredScenariosAreNumberedAndDisplayedWithDirectAccess();
        succeededScenariosAreNumberedAndCanBeDisplayedWithDirectAccess();
    }


    @Test
    public void groupScenarios() throws Exception {
        byAddingAGroupDecoratorWithParameterOnScenarios("alternate scenarios");
        scenariosWithTheSameGroupNameAreShownInASpecificParagraphWithThisNameAsTitle("alternate scenarios");
        scenariosWithoutDecoratorAreShownAreNotGrouped();
    }

    @Test
    public void groupFixtures() throws Exception {
        byAddingAGroupDecoratorWithParameterOnFixtures("When");
        fixturesWithTheSameGroupNameAreShownInASpecificParagraphWithThisNameAsTitle("When");
    }

    @Test
    public void groupRelatedUseCases() throws Exception {
        byAddingAGroupDecoratorWithParameterOnUseCase("Error cases");
        relatedUseCaseWithTheSameGroupNameAreShownInASpecificParagraphWithThisNameAsTitle("Error cases");
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

    private void relatedUseCaseWithTheSameGroupNameAreShownInASpecificParagraphWithThisNameAsTitle(String name) throws IOException {
        final Elements useCaseGroup = jUnitHtmlHelper.html.select("*.relatedUseCases");
        Assert.assertEquals(2, useCaseGroup.size());
        Assert.assertEquals(name, useCaseGroup.get(0).select("h3").text());
        Assert.assertEquals(2, useCaseGroup.get(0).select("*.relatedUseCase").size());
        Assert.assertEquals(0, useCaseGroup.get(1).select("h3").size());
    }

    @Text("By adding a group decorator with parameter ${name}  on field implementing related use case: @Decorate(decorator = Group.class, parameters =\"${name}\") ")
    private void byAddingAGroupDecoratorWithParameterOnUseCase(String name) throws IOException {
        jUnitHtmlHelper.run(this.getClass(), UseCaseGroupDecoratorSample.class);
    }

    private void theIssueOfEachFixtureCallsIsAddedAtEachRow() throws IOException {
        Elements raws = jUnitHtmlHelper.html.select("div.panel-body > table > tbody > tr ");
        Assert.assertTrue(raws.get(0).select("td").get(3).hasClass("succeeded"));
        Assert.assertTrue(raws.get(1).select("td").get(3).hasClass("succeeded"));
        Assert.assertTrue(raws.get(2).select("td").get(3).hasClass("failed"));
        Assert.assertTrue(raws.get(3).select("td").get(3).hasClass("ignored"));
    }

    private void columnHeaderAreFieldNames() throws IOException {
        Elements columnHeaders = jUnitHtmlHelper.html.select("div.panel-body > table > thead > tr > th");
        Assert.assertEquals(4, columnHeaders.size());
        Assert.assertEquals("first", columnHeaders.get(0).text());
        Assert.assertEquals("second", columnHeaders.get(1).text());
        Assert.assertEquals("sum", columnHeaders.get(2).text());
        Assert.assertEquals("", columnHeaders.get(3).text());
    }

    private void eachValuesAreDisplayed() throws IOException {
        Elements raws = jUnitHtmlHelper.html.select("div.panel-body > table > tbody > tr ");
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
        Elements title = jUnitHtmlHelper.html.select("div.panel-body > table > caption");
        Assert.assertEquals(tableTitle,title.text());
    }

    private void fixturesCallAreAggregatedInATable() throws IOException {
        Elements table = jUnitHtmlHelper.html.select("div.panel-body > table");
        Assert.assertEquals(1, table.size());
        Elements rows = table.select(" tbody > tr");
        Assert.assertEquals(4, rows.size());
    }

    @Text("By adding a table decorator with parameter ${tableTitle} on a fixture: @Decorate(decorator = Table.class, parameters =\"${tableTitle}\") ")
    private void byAddingATableDecoratorOnFixture(String tableTitle) throws IOException {
        jUnitHtmlHelper.run(this.getClass(), TableDecoratorSample.class);
    }

    @Text("By adding a group decorator with parameter ${name}  on scenarios: @Decorate(decorator = Group.class, parameters =\"${name}\") ")
    private void byAddingAGroupDecoratorWithParameterOnScenarios(String name) throws Exception {
        jUnitHtmlHelper.run(this.getClass(), ScenarioDecoratorSample.class);
    }

    @Text("By adding a group decorator with parameter ${name}  on fixtures: @Decorate(decorator = Group.class, parameters =\"${name}\") ")
    private void byAddingAGroupDecoratorWithParameterOnFixtures(String name) throws Exception {
        jUnitHtmlHelper.run(this.getClass(), FixtureGroupDecoratorSample.class);
    }

    private void scenariosWithTheSameGroupNameAreShownInASpecificParagraphWithThisNameAsTitle(String name) throws IOException {
        final Elements groups = jUnitHtmlHelper.html.select("div.container > div.scenario, div.scenarios");
        Assert.assertEquals(4,groups.size());
        Assert.assertTrue("expecting class scenarios on group #1",groups.get(1).hasClass("scenarios"));
        Assert.assertTrue("expecting class scenarios on group #2",groups.get(2).hasClass("scenarios"));
        Assert.assertEquals(name,groups.get(2).select("h2").text());
        Elements subScenarios = groups.get(2).select("div.scenario");
        Assert.assertEquals("Scenario 3", subScenarios.get(0).select("*.scenarioName").text());
        Assert.assertEquals("Scenario 4", subScenarios.get(1).select("*.scenarioName").text());
    }

    private void scenariosWithoutDecoratorAreShownAreNotGrouped() throws IOException {
        final Elements groups = jUnitHtmlHelper.html.select("div.container > div.scenario, div.scenarios");

        Assert.assertEquals("Scenario 1",groups.get(0).select("h3").text());
        Assert.assertTrue("expecting class scenario on group #0",groups.get(0).hasClass("scenario"));
        Assert.assertEquals("Scenario 5",groups.get(3).select("h3").text());
        Assert.assertTrue("expecting class scenario on group #4",groups.get(3).hasClass("scenario"));
    }

    private void fixturesWithTheSameGroupNameAreShownInASpecificParagraphWithThisNameAsTitle(String name) throws IOException {
        final Elements groups = jUnitHtmlHelper.html.select("div.panel-body > div");
        Assert.assertEquals(3,groups.size());
        Assert.assertEquals(name,groups.get(1).select("h4").text());
        Assert.assertEquals("I ask something",groups.get(1).select("*.instruction").get(0).text());
        Assert.assertEquals("And required another action",groups.get(1).select("*.instruction").get(1).text());
    }

    @Text("By adding a style decorator with parameter ${style}  on a element: @Decorate(decorator = Style.class, parameters =\"${style}\") ")
    private void byAddingStyleDecoratorWithParameterOnAElement(String style) throws Exception {
        jUnitHtmlHelper.run(this.getClass(), StyleDecoratorSample.class);
    }

    @Text("The element targeted of the html report will have the css class ${style}")
    private void theElementWillHaveTheCssClass(String style) throws Exception {
        Assert.assertTrue("Expecting class " + style + " in body", jUnitHtmlHelper.html.select("body.useCase").hasClass(style));
    }

    @Text("Several style can be used together @Decorate(decorator = Style.class, parameters ={\"${style1}\",\"${style2}\",\"${style3}\"}) ")
    private void severalStylesCanBeSpecified(String style1, String style2, String style3) throws Exception {
        final Elements select = jUnitHtmlHelper.html.select("div.scenario");
        Assert.assertTrue("Expecting class " + style1 + " in scenario div", select.hasClass(style1));
        Assert.assertTrue("Expecting class " + style2 + " in scenario div", select.hasClass(style2));
        Assert.assertTrue("Expecting class " + style3 + " in scenario div", select.hasClass(style3));
    }

    @Text("The style decorator can be apply on use case, scenario, fixture and sub use case")
    private void theStyleDecoratorCanBeApplyOnUseCaseScenarioFixtureAndSubUseCase() throws Exception {
        final Document htmlReport = jUnitHtmlHelper.html;
        Assert.assertTrue("Expecting class style in body", htmlReport.select("body.useCase").hasClass("style"));
        Assert.assertTrue("Expecting class style1 in scenario div", htmlReport.select("div.scenario").hasClass("style1"));
        Assert.assertTrue("Expecting class style4 in instruction div", htmlReport.select("div.instruction").hasClass("style4"));
        Assert.assertTrue("Expecting class style5 in relatedUseCase li", htmlReport.select("li.relatedUseCase").hasClass("style5"));
    }

    private void byAddingBreadcrumbDecoratorOnUseCase() throws Exception {
        jUnitHtmlHelper.run(this.getClass(), BreadcrumbDecoratorSample.class);
        displayableResources = new DisplayableResources("parent user story", jUnitHtmlHelper.displayResources);
    }

    private void aBreadcrumbsIsAddedAfterTitle() throws Exception {
        Elements breadcrumbs = jUnitHtmlHelper.html.select("ol.breadcrumb");
        Assert.assertEquals(1, breadcrumbs.select("li").size());
        Assert.assertEquals("Breadcrumb decorator sample", breadcrumbs.select("li").get(0).text());

        jUnitHtmlHelper.run(this.getClass(), SubUseCaseBreadcrumb.class);
        displayableResources.add("child user story", jUnitHtmlHelper.displayResources);
        breadcrumbs = jUnitHtmlHelper.html.select("ol.breadcrumb");
        Assert.assertEquals(2, breadcrumbs.select("li").size());
        Assert.assertEquals("Breadcrumb decorator sample", breadcrumbs.select("li").get(0).text());
        Assert.assertEquals("Sub use case breadcrumb", breadcrumbs.select("li").get(1).text());

        jUnitHtmlHelper.run(this.getClass(), SubSubUseCaseBreadcrumb.class);
        displayableResources.add("little child user story", jUnitHtmlHelper.displayResources);
        breadcrumbs = jUnitHtmlHelper.html.select("ol.breadcrumb");
        Assert.assertEquals(3, breadcrumbs.select("li").size());
        Assert.assertEquals("Breadcrumb decorator sample", breadcrumbs.select("li").get(0).text());
        Assert.assertEquals("Sub use case breadcrumb", breadcrumbs.select("li").get(1).text());
        Assert.assertEquals("Sub sub use case breadcrumb", breadcrumbs.select("li").get(2).text());
    }

    private void byClickingOnTheDifferentBreadcrumbWeAccessTheGivenUseStory() throws Exception {
        final Elements breadcrumbs = jUnitHtmlHelper.html.select("ol.breadcrumb");
        Assert.assertEquals(3, breadcrumbs.select("li").size());
        Assert.assertEquals("../BreadcrumbDecoratorSample.html", breadcrumbs.select("li").get(0).select("a").attr("href"));
        Assert.assertEquals("SubUseCaseBreadcrumb.html", breadcrumbs.select("li").get(1).select("a").attr("href"));
        Assert.assertTrue(breadcrumbs.select("li").get(2).classNames().contains("active"));
    }

    private void byAddingTableOfContentDecoratorOnUseCase() throws IOException {
        jUnitHtmlHelper.run(this.getClass(), TocDecoratorSample.class);
    }

    private void aTableOfContentIsAddedAfterTitleShowingTestsIssueAndAllowAccessToTheGivenStories() throws Exception {
        Elements elementsLevel1 = jUnitHtmlHelper.html.select("div.toc > ol > li");
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

    private void byAddingSynthesisDecoratorOnUseCase() throws IOException {
        jUnitHtmlHelper.run(this.getClass(), SynthesisDecoratorSample.class);
    }

    private void allScenarioRanAreListedDependingTheirsIssue() {
        Elements digestTitles = jUnitHtmlHelper.html.select("div.synthesis > div.digest > span.title");
        Assert.assertEquals("2 scenarios failed",digestTitles.get(0).text());
        Assert.assertEquals("2 scenarios ignored",digestTitles.get(1).text());
        Assert.assertEquals("8 scenarios succeeded",digestTitles.get(2).text());
    }


    private void succeededScenariosAreNumberedAndCanBeDisplayedWithDirectAccess() {
        Elements digestTitles = jUnitHtmlHelper.html.select(".synthesis .digest.succeeded .title");
        Assert.assertEquals("8 scenarios succeeded",digestTitles.get(0).text());

        Assert.assertFalse(jUnitHtmlHelper.html.select("#succeeded_fold").get(0).hasAttr("style"));
        Assert.assertEquals("display: none;", jUnitHtmlHelper.html.select("#succeeded_unfold").get(0).attr("style"));

        Elements scenarios = jUnitHtmlHelper.html.select(".synthesis .digest.succeeded .relatedUseCase");
        Assert.assertEquals("Sub use case toc 1 : Scenario 11",scenarios.get(0).text());
        Assert.assertEquals("Sub use case toc 1 : Scenario 12",scenarios.get(1).text());
        Assert.assertEquals("Sub use case toc 1 : Scenario 13",scenarios.get(2).text());
        Assert.assertEquals("Sub use case toc 2 : Scenario 21",scenarios.get(3).text());
        Assert.assertEquals("Sub use case toc 2 : Scenario 22",scenarios.get(4).text());
        Assert.assertEquals("Sub use case toc 2 : Scenario 23",scenarios.get(5).text());
        Assert.assertEquals("Sub use case toc 2 : Sub use case toc 2a : Scenario 2a 1",scenarios.get(6).text());
        Assert.assertEquals("Sub use case toc 3 : Scenario 31",scenarios.get(7).text());

    }

    private void ignoredScenariosAreNumberedAndDisplayedWithDirectAccess() {
        Elements digestTitles = jUnitHtmlHelper.html.select(".synthesis .digest.ignored .title");
        Assert.assertEquals("2 scenarios ignored",digestTitles.get(0).text());

        Assert.assertEquals("display: none;", jUnitHtmlHelper.html.select("#ignored_fold").get(0).attr("style"));
        Assert.assertFalse(jUnitHtmlHelper.html.select("#ignored_unfold").get(0).hasAttr("style"));

        Elements scenarios = jUnitHtmlHelper.html.select(".synthesis .digest.ignored .relatedUseCase");
        Assert.assertEquals("Sub use case toc 2 : Sub use case toc 2b : Scenario 2b 1",scenarios.get(0).text());
        Assert.assertEquals("Sub use case toc 3 : Scenario 32",scenarios.get(1).text());

    }

    private void failedScenariosAreNumberedAndDisplayedWithDirectAccess() {
        Elements digestTitles = jUnitHtmlHelper.html.select(".synthesis .digest.failed .title");
        Assert.assertEquals("2 scenarios failed",digestTitles.get(0).text());

        Assert.assertEquals("display: none;", jUnitHtmlHelper.html.select("#failed_fold").get(0).attr("style"));
        Assert.assertFalse(jUnitHtmlHelper.html.select("#failed_unfold").get(0).hasAttr("style"));

        Elements scenarios = jUnitHtmlHelper.html.select(".synthesis .digest.failed .relatedUseCase");
        Assert.assertEquals("Sub use case toc 2 : Sub use case toc 2c : Scenario 2c 1",scenarios.get(0).text());
        Assert.assertEquals("Sub use case toc 3 : Scenario 33",scenarios.get(1).text());
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
