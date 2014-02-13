package sft.integration.fixtures;


import sft.environment.FileSystem;
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
    private FileSystem fileSystem;

    public JavaResource(Class javaClass) {
        super(javaClass, ".java.html");
        fileSystem = new SftDocumentationConfiguration().getFileSystem();
        createJavaHtml();
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

    private void createJavaHtml() {
        try {
            File htmlJavaFile = fileSystem.targetFolder.createFileFromClass(targetClass, extension);

            Writer html = new OutputStreamWriter(new FileOutputStream(htmlJavaFile));
            html.write("<html><head><title>\n");
            html.write(targetClass.getCanonicalName() + "\n");
            html.write("</title>\n");
            html.write(htmlResources.getIncludeCssDirectives(targetClass));
            html.write("</head>\n");
            html.write("<body><div class='panel panel-default'><div class='panel-heading'><h3 class='panel-title'>Source file</h3></div><div class='panel-body'><pre>\n");

            File javaFile = fileSystem.sourceFolder.getFileFromClass(targetClass, ".java");

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
