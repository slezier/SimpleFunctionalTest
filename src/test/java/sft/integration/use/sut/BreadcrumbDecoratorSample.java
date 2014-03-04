package sft.integration.use.sut;


import org.junit.runner.RunWith;
import sft.Decorate;
import sft.SimpleFunctionalTest;
import sft.decorators.Breadcrumb;
import sft.integration.use.sut.subUseCase.SubUseCaseBreadcrumb;

@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
public class BreadcrumbDecoratorSample {

    public SubUseCaseBreadcrumb subUseCaseBreadcrumb = new SubUseCaseBreadcrumb();

}
