package sft.integration.fixtures;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.runner.JUnitCore;
import sft.Displayable;

import java.io.File;
import java.io.IOException;

public class JUnitHtmlHelper {

    public static final TestFileSystem TEST_FILE_SYSTEM = new TestFileSystem("sft-core/");
    public Document html;

    @Displayable
    public SftResources displayResources;

    public void run(Class caller, Class functionalTestClass) throws IOException {
        new JUnitCore().run(functionalTestClass);
        final String pathFromClass = TEST_FILE_SYSTEM.createFilePathFromClassAndEnsureItExists(functionalTestClass, "html");
        html = Jsoup.parse(new File(pathFromClass), "UTF-8", "http://example.com/");
        displayResources = new SftResources(caller, functionalTestClass);
    }
}
