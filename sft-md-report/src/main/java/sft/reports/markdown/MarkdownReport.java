/*******************************************************************************
 * Copyright (c) 2013, 2015 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft.reports.markdown;

import sft.DefaultConfiguration;
import sft.Report;
import sft.decorators.Decorator;
import sft.decorators.DecoratorReportImplementation;
import sft.decorators.NullDecorator;
import sft.environment.TargetFolder;
import sft.reports.markdown.decorators.MdNullDecorator;
import sft.result.UseCaseResult;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MarkdownReport extends Report {
    private Map<Class<? extends Decorator>, DecoratorReportImplementation> decorators= new HashMap<Class<? extends Decorator>, DecoratorReportImplementation>();

    private TargetFolder folder;
    private static final String TARGET_SFT_RESULT = "target/sft-result/";
    public static final String MD_DEPENDENCIES_FOLDER = "sft-md-default";


    public MarkdownReport(DefaultConfiguration configuration){
        super(configuration);
        decorators.put(NullDecorator.class,new MdNullDecorator(configuration));
        folder=configuration.getProjectFolder().getTargetFolder(TARGET_SFT_RESULT);
    }

    @Override
    public DecoratorReportImplementation getDecoratorImplementation(Decorator decorator) {
        DecoratorReportImplementation result = decorators.get(decorator.getClass());
        if( result == null ){
            result = new MdNullDecorator(configuration);
        }
        return result;
    }

    @Override
    public void addDecorator(Class<? extends Decorator> decoratorClass, DecoratorReportImplementation decoratorImplementation) {
        decorators.put(decoratorClass,decoratorImplementation);

    }

    @Override
    public void report(UseCaseResult useCaseResult) throws Exception {
        folder.copyFromResources(MD_DEPENDENCIES_FOLDER);
        final Decorator decorator = useCaseResult.useCase.useCaseDecorator;
        DecoratorReportImplementation decoratorImplementation = getDecoratorImplementation(decorator);
        String useCaseReport = decoratorImplementation.applyOnUseCase(useCaseResult, decorator.parameters);

        File mdFile = folder.createFileFromClass(useCaseResult.useCase.classUnderTest, ".md");
        Writer html = new OutputStreamWriter(new FileOutputStream(mdFile));
        html.write(useCaseReport);
        html.close();
        System.out.println("Report wrote: " + mdFile.getCanonicalPath());
    }
}
