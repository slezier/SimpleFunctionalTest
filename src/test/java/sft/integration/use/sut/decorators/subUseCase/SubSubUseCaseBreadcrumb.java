package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Test;
import sft.Decorate;
import sft.FixturesHelper;
import sft.decorators.Breadcrumb;

@Decorate(decorator = Breadcrumb.class)
public class SubSubUseCaseBreadcrumb {
    @FixturesHelper
    private SftFixturesHelper sftFixturesHelper = new SftFixturesHelper();

    @Test
    public void test(){
        sftFixturesHelper.doStuff();
    }
}
