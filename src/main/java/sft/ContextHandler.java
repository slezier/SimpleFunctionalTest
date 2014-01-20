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
package sft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ContextHandler {
    public final Method method;
    public final UseCase useCase;
    public List<MethodCall> methodCalls;

    public ContextHandler(UseCase useCase ,Method method) {
        this.useCase = useCase;
        this.method = method;
    }

    public void run() throws InvocationTargetException, IllegalAccessException {
        method.invoke(useCase.object);
    }
}
