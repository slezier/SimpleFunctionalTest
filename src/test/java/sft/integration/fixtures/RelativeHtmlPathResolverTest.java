package sft.integration.fixtures;


import org.junit.Assert;
import org.junit.Test;

public class RelativeHtmlPathResolverTest {

    @Test
    public void testGetRelativePathFromRoot() {
        RelativeHtmlPathResolver tested = new RelativeHtmlPathResolver();

        String relativePathToFile = tested.getRelativePathToFile("file1.bin", "first/third/file2.bin");

        Assert.assertEquals("first/third/file2.bin", relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromInnerDirectory() {
        RelativeHtmlPathResolver tested = new RelativeHtmlPathResolver();

        String relativePathToFile = tested.getRelativePathToFile("first/file1.bin", "first/third/file2.bin");

        Assert.assertEquals("third/file2.bin", relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromOtherDirectory() {
        RelativeHtmlPathResolver tested = new RelativeHtmlPathResolver();

        String relativePathToFile = tested.getRelativePathToFile("first/second/file1.bin", "first/third/file2.bin");

        Assert.assertEquals("../third/file2.bin", relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromOuterDirectory() {
        RelativeHtmlPathResolver tested = new RelativeHtmlPathResolver();

        String relativePathToFile = tested.getRelativePathToFile("first/second/third/file1.bin", "first/second/file2.bin");

        Assert.assertEquals("../file2.bin", relativePathToFile);
    }
}
