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


import sft.result.ScenarioResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class ScenarioHtml {

    private TemplateString stacktrace = new TemplateString("<div class=\"panel-body\"><div class=\"exception\"><a onClick=\"$(this).next().toggle()\" >@@@failure.className@@@: @@@failure.message@@@</a>" +
            "<pre class=\"stacktrace pre-scrollable\" >@@@failure.stacktrace@@@</pre></div></div>");

    public String getStacktrace(ScenarioResult scenarioResult) {
        Throwable failure = scenarioResult.failure;
        if (failure != null) {
            StringWriter stringWriter = new StringWriter();
            failure.printStackTrace(new PrintWriter(stringWriter));
            return stacktrace.replace("@@@failure.className@@@", failure.getClass().getSimpleName())
                    .replace("@@@failure.message@@@", failure.getMessage())
                    .replace("@@@failure.stacktrace@@@", stringWriter.toString()).getText();
        }
        return "";
    }


}
