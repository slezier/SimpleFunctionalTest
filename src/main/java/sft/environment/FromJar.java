package sft.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FromJar {
    private final URL resource;

    public FromJar(URL resource) {
        this.resource = resource;
    }

    public List<String> copy(File targetDirectory) throws IOException {
        return copyFromJar(targetDirectory);
    }



    private List<String> copyFromJar(File targetDirectory) throws IOException {
        List<String> result = new ArrayList<String>();

        Enumeration<JarEntry> entries = getJarEntries();
        while (entries.hasMoreElements()) {
            final JarEntry jarEntry = entries.nextElement();
            final String name = jarEntry.getName();
            final String folderName = getFolderName();
            if (name.startsWith(folderName) && ! name.endsWith("/")) {
                InputStream inputStream = FromJar.class.getClassLoader().getResourceAsStream(name);
                copyFileFromStream(makeDir(targetDirectory,name), new File(targetDirectory, name), inputStream);
                result.add(name);
            }
        }
        return result;
    }

    private Enumeration<JarEntry> getJarEntries() throws IOException {
        JarFile jar = new JarFile(URLDecoder.decode(getJarName(), "UTF-8"));
        return jar.entries();
    }

    private String getFolderName() {
        return resource.getPath().substring(resource.getPath().indexOf("!/") + 2);
    }

    private String getJarName() {
        return resource.getPath().substring(5, resource.getPath().indexOf("!"));
    }

    private void copyFileFromStream(File targetDirectory, File sourceFile,  InputStream sourceAsStream) throws IOException {
        FileOutputStream targetAsStream =null;
        try {
            File targetFile = new File(targetDirectory, sourceFile.getName());
            targetAsStream = new FileOutputStream(targetFile);
            int readBytes;
            byte[] buffer = new byte[4096];

            while ((readBytes = sourceAsStream.read(buffer)) > 0) {
                targetAsStream.write(buffer, 0, readBytes);
            }
        } finally {
            if (targetAsStream != null) {
                targetAsStream.close();
            }
        }
    }



    private File makeDir(File parentDirectory, String path) {
        for (String file : path.split("/")) {
            if (!file.contains(".")) {
                parentDirectory = new File(parentDirectory, file);
                if (!parentDirectory.exists()) {
                    parentDirectory.mkdir();
                }
            }
        }
        return parentDirectory;
    }

}
