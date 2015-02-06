package sft.integration.fixtures;

public class SftResources {

    private final Class callerClass;
    private final JavaResource javaResource;
    private final SftResource htmlResource;

    public SftResources(Class callerClass, Class functionalTestClass) {
        this.callerClass = callerClass;
        javaResource = new JavaResource(functionalTestClass);
        htmlResource = new SftResource(functionalTestClass);
    }

    @Override
    public String toString() {
        return "<div class='resources'>" + javaResource.getOpenResourceHtmlLink(callerClass, "java", "alert-info") +
                htmlResource.getOpenResourceHtmlLink(callerClass, "html", "alert-info") + "</div>";
    }
}
