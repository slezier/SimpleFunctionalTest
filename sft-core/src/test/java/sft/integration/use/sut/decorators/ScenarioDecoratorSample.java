package sft.integration.use.sut.decorators;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.SimpleFunctionalTest;
import sft.decorators.Group;

@RunWith(SimpleFunctionalTest.class)
public class ScenarioDecoratorSample {

    private static final String ALTERNATE_SCENARIO = "alternate scenarios";

    @Test
    public void scenario1(){
        doStuff();
        doStuff();
    }
    @Test
    @Decorate(decorator = Group.class, parameters = "nominal scenario")
    public void scenario2(){
        doStuff();
        doStuff();
    }

    @Decorate(decorator = Group.class, parameters = ALTERNATE_SCENARIO)
    @Test
    public void scenario3(){
        doStuff();
        doStuff();
    }

    @Decorate(decorator = Group.class, parameters = ALTERNATE_SCENARIO)
    @Test
    public void scenario4(){
        doStuff();
        doStuff();
    }

    @Test
    public void scenario5(){
        doStuff();
        doStuff();
    }

    private void doStuff() {
    }
}
