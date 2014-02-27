package sft.integration.use.sut;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.SimpleFunctionalTest;
import sft.decorators.Style;

@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Style.class, parameters ="specificStyle")
public class StyleDecoratorSample {

    @Test
    @Decorate(decorator = Style.class, parameter ="myScenario" )
    public void firstScenario() {
        call1();
    }

    @Decorate(decorator = Style.class, parameter ="myFixture" )
    private void call1() {
    }

}
