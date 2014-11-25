package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Test;
import sft.Decorate;
import sft.FixturesHelper;
import sft.decorators.Breadcrumb;

@Decorate(decorator = Breadcrumb.class)
public class SubUseCaseBreadcrumb {
    @FixturesHelper
    private SftFixturesHelper sftFixturesHelper = new SftFixturesHelper();

    public SubSubUseCaseBreadcrumb subSubUseCaseBreadcrumb= new SubSubUseCaseBreadcrumb();

    @Test
    public void test(){
        sftFixturesHelper.doStuff();
    }
}
