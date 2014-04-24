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

import sft.result.FixtureCallResult;
import sft.result.ScenarioResult;
import sft.result.UseCaseResult;

import java.io.IOException;
import java.util.List;

public interface DecoratorReportImplementation {

    String applyOnUseCase(UseCaseResult useCaseResult, String... parameters);

    String applyOnScenario(ScenarioResult scenarioResult, String... parameters);

    String applyOnFixtures(List<FixtureCallResult> fixtureCallResuts, String... parameters);

}
