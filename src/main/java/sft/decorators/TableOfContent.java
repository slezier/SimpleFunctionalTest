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

import sft.DefaultConfiguration;
import sft.result.UseCaseResult;

public class TableOfContent extends Decorator {

    public TableOfContent(DefaultConfiguration configuration, String... parameters) {
        super(configuration, parameters);
    }

    public TableOfContent(Decorator decorator) {
        super(decorator);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String result) {
        return getImplementation().applyOnUseCase(useCaseResult,result);
    }


    @Override
    public boolean comply(Decorator other) {
        return other instanceof TableOfContent;
    }
}
