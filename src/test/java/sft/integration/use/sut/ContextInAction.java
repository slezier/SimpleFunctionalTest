package sft.integration.use.sut;

import org.junit.*;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;

import java.util.ArrayList;
import java.util.List;

@RunWith(SimpleFunctionalTest.class)
public class ContextInAction {

    private static ArrayList<String> callSequence = new ArrayList<String>();

    @BeforeClass
    public static void useCaseInitialization(){
        givenAnNewService();
    }

    @Before
    public  void scenarioInitialization(){
        givenAnNewUserLoggedToThiService();
    }

    @Test
    public void firstScenario(){
        thisUserTryTheFirstFunctionnalityOfThisService();
    }

    @Test
    public void secondScenario(){
        thisUserTryTheSameFunctionnalityOfThisServiceButHaveADifferentBehaviour();
    }

    @After
    public void scenarioFinalization(){
        theUserLogOutThisServvice();
    }

    @AfterClass
    public static void useCaseFinalization(){
        theServiceIsClosed();
    }

    private static void givenAnNewService() {
        callSequence.add("useCaseInitialization");
    }

    private void givenAnNewUserLoggedToThiService() {
        callSequence.add("scenarioInitialization");
    }

    private void thisUserTryTheFirstFunctionnalityOfThisService() {
        callSequence.add("firstScenario");
    }

    private void thisUserTryTheSameFunctionnalityOfThisServiceButHaveADifferentBehaviour() {
        callSequence.add("secondScenario");
    }

    private void theUserLogOutThisServvice() {
        callSequence.add("scenarioFinalization");
    }

    private static void theServiceIsClosed() {
        callSequence.add("useCaseFinalization");
    }

    public static List<String> getCallSequence(){
        return callSequence;
    }
}
