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

import sft.DefaultConfiguration;
import sft.result.FixtureCallResult;
import sft.result.ScenarioResult;
import sft.result.SubUseCaseResult;
import sft.result.UseCaseResult;

import java.util.List;

public class HtmlNullDecorator extends HtmlDecorator {

    public HtmlNullDecorator(DefaultConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String... parameters) {
        return getHtmlReport().applyOnUseCase(useCaseResult);
    }

    @Override
    public String applyOnFixtures(List<FixtureCallResult> fixtureCallResults, String... parameters) {
        return getHtmlReport().applyOnFixtures(fixtureCallResults);
    }

    @Override
    public String applyOnSubUseCases(List<SubUseCaseResult> useCaseResult, String... parameters) {
        return getHtmlReport().applyOnSubUseCases(null, useCaseResult);
    }

    @Override
    public String applyOnScenarios(List<ScenarioResult> scenarioResults, String... parameters){
        return getHtmlReport().applyOnScenarios(scenarioResults);
    }

}
