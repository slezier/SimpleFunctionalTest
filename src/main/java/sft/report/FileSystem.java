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

public class FileSystem {

    private static final String TARGET_SFT_RESULT = "target/sft-result/";
    private static final String TO_TEST_SOURCE_DIR = "src/test/java/";
    public final ResourceFolder sourceFolder = new ResourceFolder(TO_TEST_SOURCE_DIR);
    public final ResourceFolder targetFolder = new ResourceFolder(TARGET_SFT_RESULT);

    public class ResourceFolder {

        private final String path;

        public ResourceFolder(String path) {
            this.path = path;
        }

        public File ensureExists() {
            File targetDirectory = new File(getResourceFolder() + path);
            if (!targetDirectory.exists()) {
                targetDirectory.mkdir();
            }
            return targetDirectory;
        }

        private String getResourceFolder() {
            return this.getClass().getClassLoader().getResource(".").getPath() + "../../";
        }

        private String getPath(String relativePath) {
            return getResourceFolder() + path + relativePath;
        }

        public File getFile(String path) {
            return new File(getPath(path));
        }

        public File getFileFromClass(Class<?> aClass, String extension) {
            String javaPath = aClass.getCanonicalName().replaceAll("\\.", "/") + extension;
            return getFile(javaPath);
        }

        public File createFileFromClass(Class<?> aClass, String extension) {

            String htmlPath = aClass.getCanonicalName().replaceAll("\\.", "/") + extension;
            makeDir(htmlPath);
            return targetFolder.getFileFromClass(aClass, extension);
        }

        public void makeDir(String path) {
            File parentDirectory = targetFolder.ensureExists();
            for (String file : path.split("/")) {
                if (!file.contains(".")) {
                    parentDirectory = new File(parentDirectory, file);
                    if (!parentDirectory.exists()) {
                        parentDirectory.mkdir();
                    }
                }
            }
        }

        public void copyFromResources(String fileName) throws IOException {
            try {
                File file = new File(this.getClass().getClassLoader().getResource(fileName).toURI());
                copyFromResources("", file);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        private void copyFromResources(String directory, File file) throws IOException {
            if(file.isDirectory()){
                for(File innerFile :  file.listFiles()){
                    copyFromResources(directory+"/"+file.getName(), innerFile);
                }
            } else{
                copyFileFromResources(directory,file);
            }
        }

        private void copyFileFromResources(String directory , File file) throws IOException {
            InputStream resourceAsStream = null;
            OutputStream resStreamOut = null;
            try {
                resourceAsStream = new FileInputStream(file);
                String pathname = TARGET_SFT_RESULT + directory + file.getName();
                System.out.println("copy "+file.getName()+" to "+pathname);
                File file1 = new File(pathname);
                resStreamOut = new FileOutputStream(file1);
                int readBytes;
                byte[] buffer = new byte[4096];
                while ((readBytes = resourceAsStream.read(buffer)) > 0) {
                    resStreamOut.write(buffer, 0, readBytes);
                }
            }finally {
                if (resourceAsStream != null) {
                    resourceAsStream.close();
                }
                if (resStreamOut != null) {
                    resStreamOut.close();
                }
            }
        }

    }
}
