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
import sft.DefaultConfiguration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public class DecoratorExtractor {
    public static Decorator getDecorator(DefaultConfiguration configuration, Annotation[] declaredAnnotations) throws Exception {
        for (Annotation annotation : declaredAnnotations) {
            if (annotation instanceof Decorate) {
                return createDecorator((Decorate) annotation,configuration);
            }
        }
        return new NullDecorator(configuration,null);
    }

    private static Decorator createDecorator(Decorate decorate,DefaultConfiguration configuration) throws Exception {
        Class<? extends Decorator> decorator = decorate.decorator();
        Constructor<? extends Decorator> constructor = decorator.getConstructor(DefaultConfiguration.class, String[].class);
        return constructor.newInstance(configuration,decorate.parameters());
    }

}
