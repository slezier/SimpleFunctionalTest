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
package sft.decorators;

import sft.Decorate;

import java.lang.annotation.Annotation;

public class DecoratorExtractor {
    public static Decorator getDecorator(Annotation[] declaredAnnotations) throws Exception {
        for (Annotation annotation : declaredAnnotations) {
            if (annotation instanceof Decorate) {
                return createDecorator((Decorate) annotation);
            }
        }
        return null;
    }

    private static Decorator createDecorator(Decorate decorate) throws Exception {
        return decorate.decorator().newInstance().withParameters(decorate.parameters());
    }


}
