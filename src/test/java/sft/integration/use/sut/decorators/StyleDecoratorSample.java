package sft.integration.use.sut.decorators;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.SimpleFunctionalTest;
import sft.decorators.Style;
import sft.integration.use.sut.decorators.subUseCase.OtherSubUseCase;

@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Style.class, parameters ="style")
public class StyleDecoratorSample {

    @Test
    @Decorate(decorator = Style.class, parameters ={"style1","style2","style3"})
    public void firstScenario() {
        call1();
    }

    @Decorate(decorator = Style.class, parameters ="style4" )
    private void call1() {
    }

    @Decorate(decorator = Style.class, parameters ="style5" )
    public OtherSubUseCase subUseCase = new OtherSubUseCase();

}
