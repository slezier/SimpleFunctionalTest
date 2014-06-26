package sft.integration.use.sut;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.SimpleFunctionalTest;

@RunWith(SimpleFunctionalTest.class)
public class DisplayContext {

    @Displayable
    private Object displayable1;
    @Displayable
    private Object displayable2;

    @Test
    public void annotatedObjectAreDisplayedOnlyWhenFeed(){
        doStuffWithoutDisplayingContext();
    }


    @Test
    public void displayContextWithAnnotatedObject(){
        doStuffDisplayingContext();
        doStuffDisplayingAnotherContext();
        doStuffWithError();
    }

    @Test
    public void annotatedObjectAreUnsetBetweenScenario(){
        doStuffDisplayingAnotherContext();
    }

    private void doStuffWithoutDisplayingContext() {
    }

    private void doStuffDisplayingContext() {
        displayable1 = "first context display";
    }
    private void doStuffDisplayingAnotherContext() {
        displayable2 = "second context display";
    }
    private void doStuffWithError() {
        throw new RuntimeException("Boom");
    }
}
