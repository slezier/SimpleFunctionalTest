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
package sft.javalang;

import sft.Parameter;
import sft.Text;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class JavaToHumanTranslator {

    public String humanize(final Method method){
        return humanize(method,new ArrayList<String>());
    }

    public String humanize(final Method method, final ArrayList<String> parameters) {
        if(method.isAnnotationPresent(Text.class)){

            String text = method.getAnnotation(Text.class).value();
            if(!parameters.isEmpty()){

                Annotation[][] parameterAnnotations = method.getParameterAnnotations();

                for(int i = 0; i < method.getParameterTypes().length; i++){
                    Parameter parameter = getParameterAnnotation(parameterAnnotations[i]);

                    final String replacementPatern;
                    if(parameter != null){
                        replacementPatern = "\\$\\{"+ escapeRegexpMetaCharacter(parameter) +"\\}";
                    }else{
                        replacementPatern = "\\$\\{"+(i+1)+"\\}";
                    }

                    if(parameters.size() > i){
                        text=text.replaceFirst(replacementPatern,parameters.get(i)+" ");
                    }else{
                        text=text.replaceFirst(replacementPatern,"? ");
                    }
                }
            }
            return text;
        } else {
            return humanize(method.getName());
        }
    }

    private String escapeRegexpMetaCharacter(Parameter parameter) {
        return Pattern.quote(parameter.value());
    }

    private Parameter getParameterAnnotation(Annotation[] parameterAnnotations) {
        for (Annotation parameterAnnotation : parameterAnnotations) {
            if(parameterAnnotation instanceof Parameter){
                return (Parameter) parameterAnnotation;
            }
        }
        return null;
    }


    public String humanize(Class<?> classUnderTest) {
        if(classUnderTest.isAnnotationPresent(Text.class)){
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

    private String capitalize( String words ) {
        return Character.toUpperCase(words.charAt(0)) + words.substring(1).toLowerCase();
    }

}
