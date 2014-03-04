package sft.decorators;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sft.UseCase;
import sft.report.RelativeHtmlPathResolver;

public class Breadcrumb implements Decorator {

    @Override
    public Decorator withParameters(String... parameters) {
        return this;
    }

    @Override
    public String applyOnUseCase(UseCase useCase, String result) {
        final Document parse = Jsoup.parse(result);
        parse.select(".page-header").append("<ol class=\"breadcrumb\">" + printFirstUseCase(useCase,useCase) + "</ol>");
        return parse.toString();
    }

    private String printFirstUseCase(UseCase initialUseCase,UseCase useCaseToBreadcrumb) {
        String result = "";
        if (useCaseToBreadcrumb.parent != null) {
            result = printFirstUseCase(initialUseCase,useCaseToBreadcrumb.parent);
        }
        if(initialUseCase == useCaseToBreadcrumb){
            return result + "<li class=\"active\">" + useCaseToBreadcrumb.getName() + "</li>";
        }else{
            final RelativeHtmlPathResolver relativeHtmlPathResolver = new RelativeHtmlPathResolver();
            final String origin = relativeHtmlPathResolver.getPathOf(initialUseCase.classUnderTest, ".html");
            final String target = relativeHtmlPathResolver.getPathOf(useCaseToBreadcrumb.classUnderTest, ".html");
            final String pathToUseCaseToBreadcrumb = relativeHtmlPathResolver.getRelativePathToFile(origin, target);
            return result + "<li><a href=\"" +pathToUseCaseToBreadcrumb+ "\">" + useCaseToBreadcrumb.getName() + "</a></li>";
        }
    }

    @Override
    public String applyOnScenario(String result) {
        throw new RuntimeException("Breadcrumb can't be apply on scenario");
    }

    @Override
    public String applyOnFixture(String result) {
        throw new RuntimeException("Breadcrumb can't be apply on scenario");
    }
}
