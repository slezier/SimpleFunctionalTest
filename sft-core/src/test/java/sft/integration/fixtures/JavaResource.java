package sft.integration.fixtures;

import sft.integration.SftDocumentationConfiguration;
import sft.report.HtmlReport;
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
        String read = getStringBuffer(input);
        while (read != null) {
            output.write(read.replace("<", "&lt;").replace(">", "&gt;"));
            read = getStringBuffer(input);
        }
    }

    private static String getStringBuffer(Reader input) throws IOException {
        char[] buffer = new char[1024];
        int bufferSize = input.read(buffer);
        if (-1 == bufferSize) {
            return null;
        } else {
            return new String(buffer, 0, bufferSize);
        }
    }

    private void createJavaHtml() {
        try {
            File htmlJavaFile = configuration.getReport(HtmlReport.class).getReportFolder().createFileFromClass(targetClass, extension);

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
