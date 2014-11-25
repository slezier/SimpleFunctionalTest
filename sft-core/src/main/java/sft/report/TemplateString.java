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
package sft.report;

import java.util.regex.Matcher;

public final class TemplateString {
    private final String template;

    public TemplateString(String template){
        this.template = template;
    }

    public TemplateString replace(String pattern, String replacement){
        final String value;
        if(replacement == null){
            value = "";
        } else {
            value = Matcher.quoteReplacement(replacement);
        }
        return new TemplateString(template.replaceAll(pattern,value));
    }

    public String getText(){
        return template;
    }
}
