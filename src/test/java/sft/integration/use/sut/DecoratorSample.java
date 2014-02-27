package sft.integration.use.sut;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.SimpleFunctionalTest;
import sft.decorators.Style;

@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator=Style.class,parameter="specificStyle")
public class DecoratorSample {

    @Test
    public void firstScenario(){
        call1();
    }

    private void call1() {
    }


}
