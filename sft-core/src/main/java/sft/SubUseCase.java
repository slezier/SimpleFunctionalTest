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
package sft;

import sft.decorators.Decorator;

public class SubUseCase {
    public final UseCase parentUseCase;
    public final UseCase subUseCase;
    public final Decorator decorator;

    public SubUseCase(UseCase parent, Object objectUnderTest, DefaultConfiguration configuration, Decorator decorator) throws Exception {
        parentUseCase = parent;
        subUseCase = new UseCase(parent, objectUnderTest, configuration);
        this.decorator=decorator;
    }
}
