package sft.integration.use.sut;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.SimpleFunctionalTest;

@RunWith(SimpleFunctionalTest.class)
public class DisplayContext {

    @Displayable
    private Object displayable;

    @Test
    public void displayContextWithAnnotatedObject(){
        doStuffWithoutDisplayableContext();
        doStuffWithDisplayableContext();
        doStuffWithoutDisplayableContext();
    }

    private void doStuffWithDisplayableContext() {
        displayable = "context display";
    }

    private void doStuffWithoutDisplayableContext() {
    }
}
