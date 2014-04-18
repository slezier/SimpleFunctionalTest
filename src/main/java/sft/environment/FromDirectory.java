package sft.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FromDirectory {
    private final URL resource;

    public FromDirectory(URL resource) {
        this.resource= resource;
    }

    public List<String> copy(File targetDirectory) throws URISyntaxException, IOException {
        return copyFromResources(targetDirectory, new File(resource.toURI()),"");
    }

    private List<String> copyFromResources(File targetDirectory, File sourceFile, String relativePath) throws IOException {
        ArrayList<String> filesCopied = new ArrayList<String>();
        if (sourceFile.isDirectory()) {
            File newTargetDirectory = new File(targetDirectory, sourceFile.getName());
            if (!newTargetDirectory.exists()) {
                newTargetDirectory.mkdir();
            }
            for (File innerFile : sourceFile.listFiles()) {
                filesCopied.addAll(copyFromResources(newTargetDirectory, innerFile, relativePath + newTargetDirectory.getName() + "/"));
            }
        } else {
            String fileToInclude = copyFileFromResources(targetDirectory, sourceFile, relativePath);
            filesCopied.add(fileToInclude);
        }
        return filesCopied;
    }

    private String copyFileFromResources(File targetDirectory, File sourceFile, String relativePath) throws IOException {
        InputStream sourceAsStream = null;
        try {
            sourceAsStream = new FileInputStream(sourceFile);
            return copyFileFromStream(targetDirectory, sourceFile, relativePath, sourceAsStream);
        } finally {
            if (sourceAsStream != null) {
                sourceAsStream.close();
            }
        }
    }

    private String copyFileFromStream(File targetDirectory, File sourceFile, String relativePath, InputStream sourceAsStream) throws IOException {
        FileOutputStream targetAsStream =null;
        try {
            File targetFile = new File(targetDirectory, sourceFile.getName());
            targetAsStream = new FileOutputStream(targetFile);
            int readBytes;
            byte[] buffer = new byte[4096];

            while ((readBytes = sourceAsStream.read(buffer)) > 0) {
                targetAsStream.write(buffer, 0, readBytes);
            }
            return relativePath + targetFile.getName();
        } finally {
            if (targetAsStream != null) {
                targetAsStream.close();
            }
        }
    }


}
