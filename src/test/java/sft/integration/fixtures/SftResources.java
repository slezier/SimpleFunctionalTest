package sft.integration.fixtures;


import sft.report.Css;
import sft.report.FileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

public class SftResources {

    private final FileSystem fileSystem = new FileSystem();
    private final Css css = new Css();
    private final String pathToClass;
    private final String pathToHtml;
    private final String callerClassFile;
    private final Class callerClass;

    public SftResources(Class callerClass, Class functionalTestClass) {
        this.callerClass = callerClass;
        callerClassFile = callerClass.getSimpleName()+".html";
        String callerClassPath = fileSystem.getPathOf(callerClass, ".html");
        pathToClass = fileSystem.getRelativePathToFile(callerClassPath, createJavaHtml(functionalTestClass));
        pathToHtml = fileSystem.getRelativePathToFile(callerClassPath, fileSystem.getPathOf(functionalTestClass, ".html"));
    }

    private static int copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[1024];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static String getOpenResourceHtmlLink(Class aClass, String text, String path, String cssClass) {
        String callerClassHtml = "";
        if( aClass != null ){
            callerClassHtml = aClass.getSimpleName()+".html";
        }
        return "<a href=\"#\" onclick=\"window.open(window.location.href.replace('#','').replace('"+callerClassHtml+"','')+'" + path + "','" + path + "','width=800 , menubar=no , status=no , toolbar=no , location=no , resizable=yes , scrollbars=yes',false)\" class=\"badge " + cssClass + "\">" + text + "</a>";
    }

    private String createJavaHtml(Class functionalTestClass) {
        try {
            String htmlPath = functionalTestClass.getCanonicalName().replaceAll("\\.", "/") + ".java.html";
            Writer html = fileSystem.createTargetFileWriter(htmlPath);
            html.write("<html><head><title>\n");
            html.write(functionalTestClass.getCanonicalName() + "\n");
            html.write("</title>\n");
            html.write("<link rel=\"stylesheet\" href=\"http://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css\" />\n");
            html.write("<link rel=\"stylesheet\" href=\"http://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap-theme.min.css\" />\n");
            html.write("<link rel=\"stylesheet\" href=\"" + css.getRelativePath(functionalTestClass) + "\" />\n");
            html.write("</head>\n");
            html.write("<body><div class='panel panel-default'><div class='panel-heading'><h3 class='panel-title'>Source file</h3></div><div class='panel-body'><pre>\n");

            File javaFile = fileSystem.getSourceFile(functionalTestClass);

            InputStream javaIn = new FileInputStream(javaFile);
            Reader reader = new InputStreamReader(javaIn, "UTF-8");
            copy(reader, html);

            html.write("</pre></div></div></body></html>");
            html.close();
            return htmlPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "<div class='resources'>" + getOpenResourceHtmlLink(callerClass,"java", pathToClass, "alert-info") + getOpenResourceHtmlLink(callerClass,"html", pathToHtml, "alert-info") + "</div>";
    }

    public String getLinkToSource() {
        return pathToClass;
    }

}
