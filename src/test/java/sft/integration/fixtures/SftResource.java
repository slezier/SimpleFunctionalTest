package sft.integration.fixtures;

public class SftResource {
    protected final Class targetClass;
    protected final String extension;
    protected final sft.report.FileSystem fileSystem = new  sft.report.FileSystem();

    public SftResource(Class targetClass) {
        this(targetClass,".html");
    }

    protected SftResource(Class targetClass, String extension) {
        this.targetClass= targetClass;
        this.extension = extension;
    }

    public String getHtmlPath() {
        return getPathOf(targetClass, extension);
    }

    public String getRelativePathToFile(Class callerClass) {
        String callerClassPath=getPathOf(callerClass, ".html");
        String targetHtmlPath = getHtmlPath();

        return new RelativeHtmlPathResolver().getRelativePathToFile(callerClassPath, targetHtmlPath);
    }


    public String getOpenResourceHtmlLink(Class aClass, String text, String cssClass) {
        String callerClassHtml = aClass.getSimpleName() + ".html";
        return "<a href=\"#\" onclick=\"window.open(window.location.href.replace('#','').replace('" + callerClassHtml + "','')+'" + getRelativePathToFile(aClass) + "','" + getHtmlPath() + "','width=800 , menubar=no , status=no , toolbar=no , location=no , resizable=yes , scrollbars=yes',false)\" class=\"badge " + cssClass + "\">" + text + "</a>";
    }

    private String getPathOf(Class aClass, String extension) {
        return aClass.getCanonicalName().replaceAll("\\.", "/") + extension;
    }

}
