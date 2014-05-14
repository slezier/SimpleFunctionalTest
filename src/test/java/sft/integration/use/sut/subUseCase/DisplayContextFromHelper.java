package sft.integration.use.sut.subUseCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.FixturesHelper;
import sft.SimpleFunctionalTest;

@RunWith(SimpleFunctionalTest.class)
public class DisplayContextFromHelper {

    @FixturesHelper
    private DisplayableFixturesHelper displayableFixturesHelper = new DisplayableFixturesHelper();

    @Test
    public void test(){
        displayableFixturesHelper.makeDisplayable();
    }
}
