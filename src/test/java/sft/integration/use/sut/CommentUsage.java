package sft.integration.use.sut;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;

import static org.junit.Assert.assertTrue;

/*
use case comment
 */
@RunWith(SimpleFunctionalTest.class)
public class CommentUsage {

    /*
    scenario comment
     */
    @Test
    public void lastMultiLineCommentWroteBeforeScenarioIsDisplayedAsScenarioExplanation(){
        successfulCondition ();
    }

    private void successfulCondition() {
        assertTrue(true);
    }
}
