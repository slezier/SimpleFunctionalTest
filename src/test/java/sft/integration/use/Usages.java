package sft.integration.use;

import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;

/*


    <div>
        <table class="table">
            <caption>Cheat sheet</caption>
            <tr>
                <th>Java/JUnit item</th>
                <th>Functional test item</th>
                <th>HTML item</th>
            <tr/>
            <tr>
                <td><a href="CommonUsage.html">Test Class</a></td>
                <td>Use case</td>
                <td>HTML page</td>
            <tr/>
            <tr>
                <td><a href="CommonUsage.html">Test method</a></td>
                <td>Scenario</td>
                <td>HTML paragraph with background color depending of test issue</td>
            <tr/>
            <tr>
                <td><a href="CommonUsage.html">Other methods</a></td>
                <td>Fixture</td>
                <td>HTML line</td>
            <tr/>
            <tr>
                <td><a href="LinksUseCasesTogether.html">Public field</a></td>
                <td>Related useCase</td>
                <td>HTML link</td>
            <tr/>
            <tr>
                <td><a href="CommentUsage.html">Multi-lines comment beforeUseCase class declaration</a></td>
                <td>Use case documentation</td>
                <td>HTML div afterUseCase use case name and beforeUseCase scenarios</td>
            <tr/>
            <tr>
                <td><a href="CommentUsage.html">Multi-lines comment beforeUseCase test method declaration</a></td>
                <td>Scenario documentation</td>
                <td>HTML div afterUseCase scenario name and beforeUseCase instructions</td>
            <tr/>
            <tr>
                <td><a href="HumanizationCodeUsage.html">@Text annotation on class or method</a></td>
                <td>Force a specific use case, scenario or fixture name</td>
                <td>text</td>
            <tr/>
            <tr>
                <td><a href="DefiningTestContext.html">@BeforeClass annotated public static method</a></td>
                <td>Starting context of use case</td>
                <td>HTML paragraph beforeUseCase first scenario paragraph</td>
            <tr/>
            <tr>
                <td><a href="DefiningTestContext.html">@AfterClass annotated public static method</a></td>
                <td>Handling ending context of use case</td>
                <td>HTML paragraph afterUseCase last scenario paragraph</td>
            <tr/>
            <tr>
                <td><a href="DefiningTestContext.html">@Before annotated public method</a></td>
                <td>Starting context of scenario</td>
                <td>HTML first part of scenario paragraph</td>
            <tr/>
            <tr>
                <td><a href="DefiningTestContext.html">@After annotated public method</a></td>
                <td>Handling ending context of scenario</td>
                <td>HTML last part of scenario paragraph</td>
            <tr/>
            <tr>
                <td><a href="UsingParameterizedFixture.html">Other method with parameter and @Text annnotation using ${n} variables</a></td>
                <td>Parameterized fixture</td>
                <td>HTML line with all ${n} replaced by real value</td>
            <tr/>
            <tr>
                <td><a href="UsingFixturesHelper.html">Private or protected field with @FixturesHelper annotation</a></td>
                <td>Fixtures helper</td>
                <td>HTML line corresponding of FixturesHelper fixture name</td>
            <tr/>
        </table>
    </div>

*/
@RunWith(SimpleFunctionalTest.class)
public class Usages {

    public CommonUsage commonUsage = new CommonUsage();
    public HumanizationCodeUsage humanizationCodeUsage = new HumanizationCodeUsage();
    public LinksUseCasesTogether linksUseCase = new LinksUseCasesTogether();
    public CommentUsage commentUsage = new CommentUsage();
    public DefiningTestContext definingTestContext = new DefiningTestContext();
    public DisplayingTestContext displayingTestContext = new DisplayingTestContext();
    public UsingParameterizedFixture usingParameterizedFixture = new UsingParameterizedFixture();
    public UsingFixturesHelper usingFixtureHelper = new UsingFixturesHelper();


}
