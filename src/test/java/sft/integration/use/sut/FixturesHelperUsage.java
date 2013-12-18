package sft.integration.use.sut;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.FixturesHelper;
import sft.SimpleFunctionalTest;

@RunWith(SimpleFunctionalTest.class)
public class FixturesHelperUsage {

    @FixturesHelper
    private DelegatedFixtures delegatedFixtures = new DelegatedFixtures();

    @Test
    public void testDelegatedFixtureInFixturesHelper(){
        delegatedFixtures.firstFixture();
        delegatedFixtures.secondFixture("ABCDEFGHIJKLMNOPQRSTUVWXYZ",99);
    }


}
