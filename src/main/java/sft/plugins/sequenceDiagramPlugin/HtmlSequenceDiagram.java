/*******************************************************************************
 * Copyright (c) 2014 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft.plugins.sequenceDiagramPlugin;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import sft.DefaultConfiguration;
import sft.report.decorators.HtmlDecorator;
import sft.result.FixtureCallResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class HtmlSequenceDiagram extends HtmlDecorator {

    public final PlantUmlSequenceDiagramGenerator plantUmlSequenceDiagramGenerator = new PlantUmlSequenceDiagramGenerator();

    public HtmlSequenceDiagram(DefaultConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String applyOnFixtures(List<FixtureCallResult> fixtureCallResuts, String... parameters) {
        String plantUml = plantUmlSequenceDiagramGenerator.getPlantUmlScript(fixtureCallResuts);

        SourceStringReader reader = new SourceStringReader(plantUml);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();


        try {
            reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
            os.close();
            return new String(os.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
