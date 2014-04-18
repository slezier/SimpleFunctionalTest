package sft.integration.fixtures;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.runner.JUnitCore;

import java.io.File;
import java.io.IOException;

public class JUnitHelper {

    private final Class functionalTestClass;
    private final Class caller;
    public final Document html;

    public JUnitHelper(Class caller, Class functionalTestClass) throws IOException {
        this.caller = caller;
        this.functionalTestClass = functionalTestClass;
        new JUnitCore().run(functionalTestClass);
        String pathFromClass = TestFileSystem.createFilePathFromClassAndEnsureItExists(functionalTestClass);
        html = Jsoup.parse(new File(pathFromClass), "UTF-8", "http://example.com/");
    }

    public SftResources displayResources() {
        return new SftResources(caller, functionalTestClass);
    }
}
