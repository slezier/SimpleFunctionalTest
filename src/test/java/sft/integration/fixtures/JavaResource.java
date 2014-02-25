package sft.integration.fixtures;


import sft.integration.SftDocumentationConfiguration;
import sft.report.HtmlResources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class JavaResource extends SftResource {

    private final HtmlResources htmlResources = new HtmlResources();
    private final SftDocumentationConfiguration configuration;

    public JavaResource(Class javaClass) {
        super(javaClass, ".java.html");
        configuration = new SftDocumentationConfiguration();
        createJavaHtml();
    }

    private static void copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[1024];
        while (-1 != input.read(buffer)) {
            String read = new String(buffer).replace("<","&lt;").replace(">","&gt;");
            char[] newBuffer = read.toCharArray();
            output.write(newBuffer, 0, newBuffer.length);
            buffer = new char[1024];
        }
    }

    private void createJavaHtml() {
        try {
            File htmlJavaFile = configuration.getTargetFolder().createFileFromClass(targetClass, extension);

            Writer html = new OutputStreamWriter(new FileOutputStream(htmlJavaFile));
            html.write("<html><head><title>\n");
            html.write(targetClass.getCanonicalName() + "\n");
            html.write("</title>\n");
            html.write(htmlResources.getIncludeCssDirectives(targetClass));
            html.write("</head>\n");
            html.write("<body><div class='panel panel-default'><div class='panel-heading'><h3 class='panel-title'>Source file</h3></div><div class='panel-body'><pre>\n");

            File javaFile = configuration.getSourceFolder().getFileFromClass(targetClass, ".java");

            InputStream javaIn = new FileInputStream(javaFile);
            Reader reader = new InputStreamReader(javaIn, "UTF-8");
            copy(reader, html);

            html.write("</pre></div></div></body></html>");
            html.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
