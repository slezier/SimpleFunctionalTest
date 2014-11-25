/*******************************************************************************
 * Copyright (c) 2013, 2014 Sylvain Lézier.
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
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class TargetFolder extends ResourceFolder {
    public TargetFolder(String toProjectPath, String path) {
        super(toProjectPath, path);
    }

    public File createFileFromClass(Class<?> aClass, String extension) {
        String filePath = getFilePath(aClass, extension);
        makeDir(filePath);
        return getFile(filePath);
    }

    public List<String> copyFromResources(String fileName) throws IOException {
        try {
            final File targetDirectory = ensureTargetDirectoryExists();
            final URL resource = this .getClass().getClassLoader().getResource(fileName);
            final List<String> paths ;
            if (resource.getProtocol().equals("jar")) {
                paths = new FromJar(resource).copy(targetDirectory);
            } else {
                paths = new FromDirectory(resource).copy(targetDirectory);
            }
            Collections.sort(paths);
            return paths;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeDir(String path) {
        File parentDirectory = ensureTargetDirectoryExists();
        for (String file : path.split("/")) {
            if (!file.contains(".")) {
                parentDirectory = new File(parentDirectory, file);
                if (!parentDirectory.exists()) {
                    parentDirectory.mkdir();
                }
            }
        }
    }

    private File ensureTargetDirectoryExists() {
        final File targetDirectory = new File(getResourceFolder() + path);
        if (!targetDirectory.exists()) {
            targetDirectory.mkdir();
        }
        return targetDirectory;
    }
}
