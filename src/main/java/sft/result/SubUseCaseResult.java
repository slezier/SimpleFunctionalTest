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
package sft.result;

import sft.SubUseCase;

public class SubUseCaseResult {
    public final SubUseCase subUseCase;
    public final UseCaseResult useCaseResult ;

    public SubUseCaseResult(SubUseCase subUseCase ,UseCaseResult useCaseResult ){
        this.subUseCase = subUseCase;
        this.useCaseResult = useCaseResult;
    }
}
