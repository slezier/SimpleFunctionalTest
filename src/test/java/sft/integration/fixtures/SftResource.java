package sft.integration.fixtures;

import sft.report.RelativeHtmlPathResolver;

public class SftResource {
    protected final Class targetClass;
    protected final String extension;
    private final RelativeHtmlPathResolver relativeHtmlPathResolver = new RelativeHtmlPathResolver();

    public SftResource(Class targetClass) {
        this(targetClass, ".html");
    }

    protected SftResource(Class targetClass, String extension) {
        this.targetClass = targetClass;
        this.extension = extension;
    }

    public String getHtmlPath() {
        return relativeHtmlPathResolver.getPathOf(targetClass, extension);
    }

    public String getRelativePathToFile(Class callerClass) {
        String callerClassPath = relativeHtmlPathResolver.getPathOf(callerClass, ".html");
        String targetHtmlPath = getHtmlPath();
        return relativeHtmlPathResolver.getRelativePathToFile(callerClassPath, targetHtmlPath);
    }

    public String getOpenResourceHtmlLink(Class aClass, String text, String cssClass) {
        String callerClassHtml = aClass.getSimpleName() + ".html";
        return "<a href=\"#\" onclick=\"window.open(window.location.href.replace('#','').replace('" + callerClassHtml + "','')+'" + getRelativePathToFile(aClass) + "','" + getHtmlPath() + "','width=800 , menubar=no , status=no , toolbar=no , location=no , resizable=yes , scrollbars=yes',false)\" class=\"badge " + cssClass + "\">" + text + "</a>";
    }

}
