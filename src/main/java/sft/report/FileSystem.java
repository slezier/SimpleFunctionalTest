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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
            InputStream resourceAsStream = null;
            OutputStream resStreamOut = null;
            try {
                int readBytes;
                byte[] buffer = new byte[4096];
                resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
                resStreamOut = new FileOutputStream(new File(TARGET_SFT_RESULT + fileName));
                while ((readBytes = resourceAsStream.read(buffer)) > 0) {
                    resStreamOut.write(buffer, 0, readBytes);
                }
            } finally {
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
