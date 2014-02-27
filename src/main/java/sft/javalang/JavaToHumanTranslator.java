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
package sft.javalang;

import sft.Text;

import java.lang.reflect.Method;

public class JavaToHumanTranslator {

    public String humanize(final Method method) {
        if (method.isAnnotationPresent(Text.class)) {
            return method.getAnnotation(Text.class).value();
        } else {
            return humanize(method.getName());
        }
    }

    public String humanize(Class<?> classUnderTest) {
        if (classUnderTest.isAnnotationPresent(Text.class)) {
            return classUnderTest.getAnnotation(Text.class).value();
        } else {
            return humanize(classUnderTest.getSimpleName());
        }
    }

    public String humanize(final String javaName) {
        final String words = camelCaseToWords(javaName);
        final String wordsWithoutUnderscores = removeUnderscores(words);
        return capitalize(wordsWithoutUnderscores.trim());
    }

    private String removeUnderscores(String result) {
        return result.replaceAll(" _ ", " ").replaceAll("_", "");
    }

    private String camelCaseToWords(String javaName) {
        return javaName.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    private String capitalize(String words) {
        return Character.toUpperCase(words.charAt(0)) + words.substring(1).toLowerCase();
    }

}
