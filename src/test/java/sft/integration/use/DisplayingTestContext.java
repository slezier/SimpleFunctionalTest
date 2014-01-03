package sft.integration.use;

import org.junit.Test;

public class DisplayingTestContext {
    /*
    @Display(AS_STRING)
    private Object displayContext1;

    @Display(AS_PROPERTY)
    private Object displayContext2;
     */


    @Test
    public void displayTestContextWithAnObjectAnnotatedByDisplayable(){
        allPrivateObjectsAnnotatedByDisplayable();
        areDisplayedAfterEachFixtureCall();
    }

    private void areDisplayedAfterEachFixtureCall() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void allPrivateObjectsAnnotatedByDisplayable() {
        //To change body of created methods use File | Settings | File Templates.
    }

}
