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
package sft.report;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileSystem {

    private static final String TARGET_SFT_RESULT = "target/sft-result/";
    private static final String TO_TEST_SOURCE_DIR = "src/test/java/";
    public final ResourceFolder sourceFolder = new ResourceFolder(TO_TEST_SOURCE_DIR);
    public final ResourceFolder targetFolder = new ResourceFolder(TARGET_SFT_RESULT);

    public class ResourceFolder {

        private final String path;

        private ResourceFolder(String path) {
            this.path = path;
        }

        public File getFileFromClass(Class<?> aClass, String extension) {
            String javaPath = getFilePath(aClass, extension);
            return getFile(javaPath);
        }

        public File createFileFromClass(Class<?> aClass, String extension) {

            String htmlPath = getFilePath(aClass, extension);
            makeDir(htmlPath);
            return getFileFromClass(aClass, extension);
        }

        public List<String> copyFromResources(String fileName) throws IOException {
            try {
                File sourceFile = new File(this.getClass().getClassLoader().getResource(fileName).toURI());
                File targetDirectory = ensureExists();
                return copyFromResources(targetDirectory, sourceFile,"");
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
            return this.getClass().getClassLoader().getResource(".").getPath() + "../../";
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
