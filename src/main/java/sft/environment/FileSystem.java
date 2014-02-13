/*******************************************************************************
 * Copyright (c) 2013 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft.environment;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileSystem {
    public final ResourceFolder sourceFolder ;
    public final ResourceFolder targetFolder ;
    public final String classFolder;


    public FileSystem(String classFolder,String sourceFolder, String targetFolder) {
        this.classFolder = classFolder;

        String toProjectPath= "";
        for (String folder : classFolder.split("/")) {
            if (!folder.equals(".") && !folder.equals("") ) {
                toProjectPath+= "../";
            }
        }


        this.sourceFolder = new ResourceFolder(toProjectPath,sourceFolder);
        this.targetFolder = new ResourceFolder(toProjectPath,targetFolder);
    }

    public FileSystem changeSourcePath(String sourcePath) {
        return new FileSystem(classFolder,sourcePath,targetFolder.path);
    }

    public FileSystem changeClassFolder(String classFolder) {
        return new FileSystem(classFolder,sourceFolder.path,targetFolder.path);
    }

    public FileSystem changeTargetPath(String reportPath) {
        return new FileSystem(classFolder,sourceFolder.path,reportPath);
    }

    public class ResourceFolder {

        private final String toProjectPath;
        public final String path;

        private ResourceFolder(String toProjectPath,String path) {
            this.toProjectPath = toProjectPath;
            this.path = path;

        }

        public File getFileFromClass(Class<?> aClass, String extension) {
            String filePath = getFilePath(aClass, extension);
            return getFile(filePath);
        }

        public File createFileFromClass(Class<?> aClass, String extension) {
            String filePath = getFilePath(aClass, extension);
            makeDir(filePath);
            return getFile(filePath);
        }

        public List<String> copyFromResources(String fileName) throws IOException {
            try {
                File resourceFile = new File(this.getClass().getClassLoader().getResource(fileName).toURI());
                File targetDirectory = ensureExists();
                List<String> paths = copyFromResources(targetDirectory, resourceFile, "");
                Collections.sort(paths);
                return paths;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        private String getFilePath(Class<?> aClass, String extension) {
            return aClass.getCanonicalName().replaceAll("\\.", "/") + extension;
        }

        private void makeDir(String path) {
            File parentDirectory = ensureExists();
            for (String file : path.split("/")) {
                if (!file.contains(".")) {
                    parentDirectory = new File(parentDirectory, file);
                    if (!parentDirectory.exists()) {
                        parentDirectory.mkdir();
                    }
                }
            }
        }

        private String getResourceFolder() {
            return this.getClass().getClassLoader().getResource(".").getPath() + toProjectPath;
        }

        private String getPath(String relativePath) {
            return getResourceFolder() + path + relativePath;
        }

        private File ensureExists() {
            File targetDirectory = new File(getResourceFolder() + path);
            if (!targetDirectory.exists()) {
                targetDirectory.mkdir();
            }
            return targetDirectory;
        }

        private File getFile(String path) {
            return new File(getPath(path));
        }

        private List<String> copyFromResources(File targetDirectory, File sourceFile,String relativePath) throws IOException {
            ArrayList<String> filesCopied = new ArrayList<String>();
            if (sourceFile.isDirectory()) {
                File newTargetDirectory = new File(targetDirectory, sourceFile.getName());
                if (!newTargetDirectory.exists()) {
                    newTargetDirectory.mkdir();
                }
                for (File innerFile : sourceFile.listFiles()) {
                    filesCopied.addAll(copyFromResources(newTargetDirectory, innerFile,relativePath+newTargetDirectory.getName()+"/"));
                }
            } else {
                String fileToInclude = copyFileFromResources(targetDirectory, sourceFile,relativePath);
                filesCopied.add(fileToInclude);
            }
            return filesCopied;
        }

        private String copyFileFromResources(File targetDirectory, File sourceFile,String relativePath) throws IOException {
            InputStream sourceAsStream = null;
            OutputStream targetAsStream = null;
            try {
                sourceAsStream = new FileInputStream(sourceFile);
                File targetFile = new File(targetDirectory, sourceFile.getName());
                targetAsStream = new FileOutputStream(targetFile);
                int readBytes;
                byte[] buffer = new byte[4096];
                while ((readBytes = sourceAsStream.read(buffer)) > 0) {
                    targetAsStream.write(buffer, 0, readBytes);
                }
                return relativePath + targetFile.getName();
            } finally {
                if (sourceAsStream != null) {
                    sourceAsStream.close();
                }
                if (targetAsStream != null) {
                    targetAsStream.close();
                }
            }
        }
    }
}
