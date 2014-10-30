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

import java.util.ArrayList;

public class SoutReport {

    public void generate(UseCaseResult useCaseResult) {

        ResultDigest resultDigest = new ResultDigest(useCaseResult);

        System.out.println( "Scenario succeeded: "+resultDigest.nbScenariosOk);
        if( ! resultDigest.scenariosFailed.isEmpty()){
            System.out.println("Scenario failed: "+resultDigest.scenariosFailed.size());
            for (ScenarioResult scenarioResult : resultDigest.scenariosFailed) {
                System.out.println(" - " + scenarioResult.scenario.getName() );
            }
        }
        if( ! resultDigest.scenariosIgnored.isEmpty()){
            System.out.println("Scenario ignored: "+resultDigest.scenariosIgnored.size());
            for (ScenarioResult scenarioResult : resultDigest.scenariosIgnored) {
                System.out.println(" - " + scenarioResult.scenario.getName() );
            }
        }

    }

}
