package sft.report;


import org.junit.Assert;
import org.junit.Test;

public class RelativeHtmlPathResolverTest {

    @Test
    public void testGetRelativePathFromRoot() {
        sft.report.RelativeHtmlPathResolver tested = new sft.report.RelativeHtmlPathResolver();

        String relativePathToFile = tested.getRelativePathToFile("file1.bin", "first/third/file2.bin");

        Assert.assertEquals("first/third/file2.bin", relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromInnerDirectory() {
        sft.report.RelativeHtmlPathResolver tested = new sft.report.RelativeHtmlPathResolver();

        String relativePathToFile = tested.getRelativePathToFile("first/file1.bin", "first/third/file2.bin");

        Assert.assertEquals("third/file2.bin", relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromOtherDirectory() {
        sft.report.RelativeHtmlPathResolver tested = new sft.report.RelativeHtmlPathResolver();

        String relativePathToFile = tested.getRelativePathToFile("first/second/file1.bin", "first/third/file2.bin");

        Assert.assertEquals("../third/file2.bin", relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromOuterDirectory() {
        sft.report.RelativeHtmlPathResolver tested = new sft.report.RelativeHtmlPathResolver();

        String relativePathToFile = tested.getRelativePathToFile("first/second/third/file1.bin", "first/second/file2.bin");

        Assert.assertEquals("../file2.bin", relativePathToFile);
    }
}
