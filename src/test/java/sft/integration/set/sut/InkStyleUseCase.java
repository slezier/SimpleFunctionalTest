package sft.integration.set.sut;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Using;
import sft.integration.use.sut.subUseCase.SubUseCaseFailed;
import sft.integration.use.sut.subUseCase.SubUseCaseIgnored;
import sft.integration.use.sut.subUseCase.SubUseCaseSucceeded;

import static org.junit.Assert.assertTrue;

/*
Test Use Case comment
 */
@RunWith(SimpleFunctionalTest.class)
@Using(InkConfiguration.class)
public class InkStyleUseCase {

    public SubUseCaseFailed subUseCaseFailed = new SubUseCaseFailed();
    public SubUseCaseIgnored subUseCaseIgnored = new SubUseCaseIgnored();
    public SubUseCaseSucceeded subUseCaseSucceeded = new SubUseCaseSucceeded();

    @BeforeClass
    public static void setUpClass(){
        setUpUseCase();
    }

    @AfterClass
    public static void tearDownClass(){
        tearDownUseCase();
    }

    @Before
    public void setUpTest(){
        setUpScenario();
    }

    @After
    public void tearDownTest(){
        tearDownScenario();
    }

    /*
    Test scenario comment
     */
    @Test
    public void successfulScenario() {
        successfulCondition();
    }

    @Test
    public void unsuccessfulScenario() {
        successfulCondition();
        conditionThatFailed();
        successfulCondition();
    }

    @Test
    @Ignore
    public void ignoredScenario() {
        successfulCondition();
        conditionThatFailed();
        successfulCondition();
    }

    private void successfulCondition() {
        assertTrue(true);
    }

    private void conditionThatFailed() {
        assertTrue("Condition failed", false);
    }

    private static void setUpUseCase() {
    }

    private static void tearDownUseCase() {
    }

    private void setUpScenario() {
    }

    private void tearDownScenario() {
    }

}
