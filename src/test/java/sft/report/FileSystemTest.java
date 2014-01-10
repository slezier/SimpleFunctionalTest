package sft.report;

import org.junit.Test;
import org.junit.Assert;

public class FileSystemTest {

    @Test
    public void testGetRelativePathFromRoot(){
        FileSystem tested = new FileSystem();

        String relativePathToFile = tested.getRelativePathToFile("file1.bin", "first/third/file2.bin");

        Assert.assertEquals("first/third/file2.bin",relativePathToFile);
    }
    @Test
    public void testGetRelativePathFromInnerDirectory(){
        FileSystem tested = new FileSystem();

        String relativePathToFile = tested.getRelativePathToFile("first/file1.bin", "first/third/file2.bin");

        Assert.assertEquals("third/file2.bin",relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromOtherDirectory(){
        FileSystem tested = new FileSystem();

        String relativePathToFile = tested.getRelativePathToFile("first/second/file1.bin", "first/third/file2.bin");

        Assert.assertEquals("../third/file2.bin",relativePathToFile);
    }

    @Test
    public void testGetRelativePathFromOuterDirectory(){
        FileSystem tested = new FileSystem();

        String relativePathToFile = tested.getRelativePathToFile("first/second/third/file1.bin", "first/second/file2.bin");

        Assert.assertEquals("../file2.bin",relativePathToFile);
    }

}
