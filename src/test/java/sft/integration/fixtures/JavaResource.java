package sft.integration.fixtures;

import sft.report.Css;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

public class JavaResource extends SftResource{

    private final Css css = new Css();

    public JavaResource(Class javaClass) {
        super(javaClass);
        createJavaHtml();
    }

    private void createJavaHtml() {
        try {
            String htmlPath = getHtmlPath();
            Writer html = fileSystem.createTargetFileWriter(htmlPath);
            html.write("<html><head><title>\n");
            html.write(targetClass.getCanonicalName() + "\n");
            html.write("</title>\n");
            html.write("<link rel=\"stylesheet\" href=\"http://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css\" />\n");
            html.write("<link rel=\"stylesheet\" href=\"http://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap-theme.min.css\" />\n");
            html.write("<link rel=\"stylesheet\" href=\"" + css.getRelativePath(targetClass) + "\" />\n");
            html.write("</head>\n");
            html.write("<body><div class='panel panel-default'><div class='panel-heading'><h3 class='panel-title'>Source file</h3></div><div class='panel-body'><pre>\n");

            File javaFile = fileSystem.getSourceFile(targetClass);

            InputStream javaIn = new FileInputStream(javaFile);
            Reader reader = new InputStreamReader(javaIn, "UTF-8");
            copy(reader, html);

            html.write("</pre></div></div></body></html>");
            html.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHtmlPath() {
        return fileSystem.getPathOf(targetClass, ".java.html");
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
}
