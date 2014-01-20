package sft;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import java.io.IOException;

@Ignore
public class SimpleFunctionnalTestTest {

    private SimpleFunctionalTest tested ;

    @Before
    public void setup() throws IllegalAccessException, InitializationError, InstantiationException, IOException {
        tested= new SimpleFunctionalTest(UseCase_SUT.class);
    }

    @Test
    public void test(){

    }

}
