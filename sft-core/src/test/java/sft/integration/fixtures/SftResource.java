package sft.integration.fixtures;

import sft.report.RelativeHtmlPathResolver;

public class SftResource {
    protected final Class targetClass;
    protected final String extension;
    private final RelativeHtmlPathResolver pathResolver = new RelativeHtmlPathResolver();

    public SftResource(Class targetClass) {
        this(targetClass, ".html");
    }

    protected SftResource(Class targetClass, String extension) {
        this.targetClass = targetClass;
        this.extension = extension;
    }

    public String getHtmlPath() {
        return pathResolver.getPathOf(targetClass, extension);
    }

    public String getRelativePathToFile(Class callerClass) {
        String callerClassPath = pathResolver.getPathOf(callerClass, ".html");
        String targetHtmlPath = getHtmlPath();
        return pathResolver.getRelativePathToFile(callerClassPath, targetHtmlPath);
    }

    public String getOpenResourceHtmlLink(Class aClass, String text, String cssClass) {
        String callerClassHtml = aClass.getSimpleName() + ".html";
        return "<a href=\"#\" onclick=\"window.open(window.location.href.replace('#','').replace('" + callerClassHtml + "','')+'" + getRelativePathToFile(aClass) + "','" + getHtmlPath() + "','width=800 , menubar=no , status=no , toolbar=no , location=no , resizable=yes , scrollbars=yes',false);return false;\" class=\"badge " + cssClass + "\">" + text + "</a>";
    }

}
