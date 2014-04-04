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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceFolder {

    private final String toProjectPath;
    public final String path;

    public ResourceFolder(String toProjectPath, String path) {
        this.toProjectPath = toProjectPath;
        this.path = path;

    }

    public File getFileFromClass(Class<?> aClass, String extension) {
        String filePath = getFilePath(aClass, extension);
        return getFile(filePath);
    }

    protected String getFilePath(Class<?> aClass, String extension) {
        return aClass.getCanonicalName().replaceAll("\\.", "/") + extension;
    }

    protected String getResourceFolder() {
        return this.getClass().getClassLoader().getResource("").getPath() + toProjectPath;
    }

    protected File getFile(String path) {
        return new File(getPath(path));
    }

    private String getPath(String relativePath) {
        return getResourceFolder() + path + relativePath;
    }
}
