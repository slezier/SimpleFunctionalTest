package sft.reports.markdown;

import org.junit.runner.JUnitCore;
import sft.Displayable;
import sft.integration.fixtures.SftResources;
import sft.integration.fixtures.TestFileSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JUnitMdHelper {

    @Displayable
    public SftResources displayResources;

    public List<String> text = new ArrayList<String>();

    public void run(Class caller, Class functionalTestClass) throws IOException {
        new JUnitCore().run(functionalTestClass);
        final String pathFromClass = new TestFileSystem("sft-md-report/").createFilePathFromClassAndEnsureItExists(functionalTestClass, "md");
        text=readTextFile(pathFromClass);
        displayResources = new SftResources(caller, functionalTestClass);
    }

    public List<String> readTextFile(String pathFromClass) throws FileNotFoundException {
        List<String> text = new ArrayList<String>();
        Scanner scanner = new Scanner(new FileInputStream(pathFromClass));
        try {
            while (scanner.hasNextLine()){
                text.add(scanner.nextLine());
            }
        }
        finally{
            scanner.close();
        }
        return text;
    }
}
