package sft.integration.set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.DefaultConfiguration;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.UseCase;
import sft.decorators.Breadcrumb;
import sft.integration.fixtures.JavaResource;
import sft.integration.set.sut.CommonUseCase;
import sft.integration.set.sut.CustomConfiguration;
import sft.integration.set.sut.UseCaseWithSpecificConfiguration;
import sft.report.HtmlResources;


@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
public class Settings {

    @Displayable
    private String files;
    private UseCase useCase;

    public HtmlReportSettings htmlReportSettings = new HtmlReportSettings();
    public SourceAndClassSettings sourceAndClassSettings = new SourceAndClassSettings();



    @Test
    public void configurationMechanism() throws Exception {
        allSettingsOfAnUseCaseAreHoldByTheClassDefaultConfiguration();
        toModifySettingsYouHaveToCreateAnInheritedClassFromDefaultConfigurationAndChangeSettingInTheConstructor();
        thenYouCanInjectConfigurationByAnnotatedUseCaseJavaClassWithUsingCustomConfiguration();
        allRelatedUseCasesWillUseThisConfiguration();
    }

    private void allSettingsOfAnUseCaseAreHoldByTheClassDefaultConfiguration() throws Exception {
        useCase = new UseCase(CommonUseCase.class);
        Assert.assertEquals(DefaultConfiguration.class, useCase.configuration.getClass());
        Assert.assertEquals(DefaultConfiguration.class, useCase.subUseCases.get(0).configuration.getClass());
        Assert.assertEquals(DefaultConfiguration.class, useCase.subUseCases.get(1).configuration.getClass());
    }

    @Text("To modify settings you have to create an inherited class from DefaultConfiguration and change setting in the constructor")
    private void toModifySettingsYouHaveToCreateAnInheritedClassFromDefaultConfigurationAndChangeSettingInTheConstructor() throws Exception {
        new HtmlResources().ensureIsCreated();
        files = "<div class=\"resources\">" +
                new JavaResource(CustomConfiguration.class).getOpenResourceHtmlLink(this.getClass(), "custom configuration", "alert-info") +
                new JavaResource(UseCaseWithSpecificConfiguration.class).getOpenResourceHtmlLink(this.getClass(), "use case using custom configuration", "alert-info") + "</div>";
    }

    @Text("Then you can inject configuration by annotated UseCase java class with: @Using( CustomConfiguration.class ).")
    private void thenYouCanInjectConfigurationByAnnotatedUseCaseJavaClassWithUsingCustomConfiguration() throws Exception {

        useCase = new UseCase(UseCaseWithSpecificConfiguration.class);
        Assert.assertEquals(CustomConfiguration.class, useCase.configuration.getClass());
    }

    private void allRelatedUseCasesWillUseThisConfiguration() {
        Assert.assertEquals(CustomConfiguration.class, useCase.subUseCases.get(0).configuration.getClass());
        Assert.assertEquals(CustomConfiguration.class, useCase.subUseCases.get(1).configuration.getClass());
    }


}
