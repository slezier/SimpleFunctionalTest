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


import sft.report.TargetFolder;

public class FileSystem {
    public final ResourceFolder sourceFolder ;
    public final TargetFolder targetFolder ;
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
        this.targetFolder = new TargetFolder(toProjectPath,targetFolder);
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

}
