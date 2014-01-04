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


import org.junit.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileSystem {

    private static final String TARGET_SFT_RESULT = "target/sft-result/";
    private static final String TO_TEST_SOURCE_DIR="src/test/java/";


    public File ensureTargetDirExists(){
        File targetDirectory = new File(getTargetFolder());
        if (!targetDirectory.exists()) {
            targetDirectory.mkdir();
        }
        return targetDirectory;
    }

    public void makeTargetDirs(String path) {
        File parentDirectory = ensureTargetDirExists();
        for (String file : path.split("/")) {
            if (!file.contains(".")) {
                parentDirectory = new File(parentDirectory, file);
                if (!parentDirectory.exists()) {
                    parentDirectory.mkdir();
                }
            }
        }
    }

    public boolean targetFileExists(String path) {
        File cssFile = new File(getTargetFolder()+path);
        return cssFile.exists();
    }


    public static void assertSourceFilesExists(String... filesToCheck) {
        for (String fileToCheck : filesToCheck) {
            Assert.assertTrue("Missing file " + fileToCheck, new File(fileToCheck).exists());
        }
    }

    public void copyFileToTargetDir(String fileName) throws IOException {
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
            if(resourceAsStream!= null){
                resourceAsStream.close();
            }
            if(resStreamOut!= null){
                resStreamOut.close();
            }
        }
    }

    private File createTargetFile(String htmlPath) {
        return new File(getTargetFolder()+htmlPath);
    }

    public File getSourceFile(Class<?> javaClass) {
        String fileName = javaClass.getCanonicalName().replaceAll("\\.", "/") + ".java";
        return getSourceFile(fileName);
    }

    private File getSourceFile(String fileName) {
        return new File(getResourceFolder() + TO_TEST_SOURCE_DIR+fileName);
    }

    private String getResourceFolder() {
        return this.getClass().getClassLoader().getResource(".").getPath()+ "../../";
    }
    private String getTargetFolder() {
        return getResourceFolder()+TARGET_SFT_RESULT;
    }

    private OutputStream createTargetFileStream(String htmlPath) throws FileNotFoundException {
        File htmlFile= createTargetFile(htmlPath);
        OutputStream htmlOut = new FileOutputStream(htmlFile);
        return htmlOut;
    }

    public Writer createTargetFileWriter(String htmlPath) throws FileNotFoundException {
        makeTargetDirs(htmlPath);
        return new OutputStreamWriter(createTargetFileStream(htmlPath));
    }
}
