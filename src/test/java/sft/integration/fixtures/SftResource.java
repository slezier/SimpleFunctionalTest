package sft.integration.fixtures;

public class SftResource {
    protected final Class targetClass;
    protected final sft.report.FileSystem fileSystem = new  sft.report.FileSystem();

    public SftResource(Class targetClass) {
        this.targetClass= targetClass;

    }

    public String getHtmlPath() {
        return fileSystem.getPathOf(targetClass, ".html");
    }


    public String getRelativePathToFile(Class callerClass) {
        String callerClassPath=fileSystem.getPathOf(callerClass, ".html");
        return fileSystem.getRelativePathToFile(callerClassPath, getHtmlPath() );
    }


    public String getOpenResourceHtmlLink(Class aClass, String text, String cssClass) {
        String callerClassHtml = aClass.getSimpleName() + ".html";
        return "<a href=\"#\" onclick=\"window.open(window.location.href.replace('#','').replace('" + callerClassHtml + "','')+'" + getRelativePathToFile(aClass) + "','" + getHtmlPath() + "','width=800 , menubar=no , status=no , toolbar=no , location=no , resizable=yes , scrollbars=yes',false)\" class=\"badge " + cssClass + "\">" + text + "</a>";
    }
}
