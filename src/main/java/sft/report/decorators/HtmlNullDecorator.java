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

package sft.report.decorators;

import sft.decorators.Decorator;
import sft.result.FixtureCallResult;
import sft.result.UseCaseResult;

import java.io.IOException;
import java.util.List;

public class HtmlNullDecorator extends HtmlDecorator {

    public HtmlNullDecorator(Decorator decorator) {
        super(decorator);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult) {
        return getHtmlReport().generateUseCase(useCaseResult);
    }

    @Override
    public String applyOnScenario(String result) {
        return result;
    }

    @Override
    public String applyOnFixtures(List<String> fixtures, List<FixtureCallResult> fixtureCallResuts) {
        String result ="";
        for (String fixture : fixtures) {
            result+=fixture;
        }
        return result;
    }
}
