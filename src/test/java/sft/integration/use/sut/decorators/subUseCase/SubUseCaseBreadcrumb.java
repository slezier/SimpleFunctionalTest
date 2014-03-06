package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Test;
import sft.Decorate;
import sft.decorators.Breadcrumb;

@Decorate(decorator = Breadcrumb.class)
public class SubUseCaseBreadcrumb {

    public SubSubUseCaseBreadcrumb subSubUseCaseBreadcrumb= new SubSubUseCaseBreadcrumb();

    @Test
    public void test(){
        doStuff();
    }

    private void doStuff() {
    }
}
