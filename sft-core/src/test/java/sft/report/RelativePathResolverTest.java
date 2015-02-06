package sft.report;


import org.junit.Assert;
import org.junit.Test;

public class RelativePathResolverTest {

    @Test
    public void testGetRelativePathFromRoot() {
        RelativePathResolver tested = new RelativePathResolver();

        String relativePathToFile = tested.getRelativePathToFile("file1.bin", "first/third/file2.bin");

        Assert.assertEquals("first/third/file2.bin", relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromInnerDirectory() {
        RelativePathResolver tested = new RelativePathResolver();

        String relativePathToFile = tested.getRelativePathToFile("first/file1.bin", "first/third/file2.bin");

        Assert.assertEquals("third/file2.bin", relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromOtherDirectory() {
        RelativePathResolver tested = new RelativePathResolver();

        String relativePathToFile = tested.getRelativePathToFile("first/second/file1.bin", "first/third/file2.bin");

        Assert.assertEquals("../third/file2.bin", relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromOuterDirectory() {
        RelativePathResolver tested = new RelativePathResolver();

        String relativePathToFile = tested.getRelativePathToFile("first/second/third/file1.bin", "first/second/file2.bin");

        Assert.assertEquals("../file2.bin", relativePathToFile);
    }
}
