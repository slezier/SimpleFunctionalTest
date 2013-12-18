package sft.integration.fixtures;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.File;
import java.io.IOException;

public class JUnitHelper {

    private final Class functionalTestClass;
    private final String expectedPathToHtmlResultFile;
    private Document html;

    public JUnitHelper(Class functionalTestClass, String expectedPathToHtmlResultFile) {
        this.functionalTestClass = functionalTestClass;
        this.expectedPathToHtmlResultFile = expectedPathToHtmlResultFile;
    }

    public Result run() {
        JUnitCore core = new JUnitCore();
        return core.run(functionalTestClass);
    }

    public Document getHtmlReport() throws IOException {
        if(html == null){
            FileSystem.filesExists(expectedPathToHtmlResultFile);

            html = Jsoup.parse(new File(expectedPathToHtmlResultFile), "UTF-8", "http://example.com/");
        }
        return html;
    }

}
