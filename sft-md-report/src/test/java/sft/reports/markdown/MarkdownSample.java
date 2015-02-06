package sft.reports.markdown;

import org.junit.*;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.Using;


/*
Use case Comment line 1
Use case Comment line 2
 */
@RunWith(SimpleFunctionalTest.class)
@Using(MarkdownReportConfiguration.class)
public class MarkdownSample {

    @BeforeClass
    public static void beforeUseCase(){
        setUpUseCase();
    }
    @Before
    public void beforeScenario(){
        setUpScenario();
    }

    /*
    Scenario Comment line 1
    Scenario Comment line 2
     */
    @Test
    public void firstScenario(){
        doNothing();
        doNothingWithParameter("parameter1", 2);
    }

    public SubUseCaseSuccessful subUseCaseSuccessful = new SubUseCaseSuccessful();
    public SubUseCaseFailed subUseCaseFailed = new SubUseCaseFailed();
    public SubUseCaseIgnored subUseCaseIgnored = new SubUseCaseIgnored();

    @After
    public void afterScenario(){
        tearDownScenario();
    }

    @AfterClass
    public static void setupUseCase(){
        tearDownUseCase();
    }

    private static void setUpUseCase() {
    }

    private void setUpScenario() {
    }

    @Text("Do nothing with parameters ${parameter1} and ${parameter2}")
    private void doNothingWithParameter(String parameter1, int parameter2) {}

    private void doNothing() {
    }

    private void tearDownScenario() {
    }

    private static void tearDownUseCase() {
    }
}
