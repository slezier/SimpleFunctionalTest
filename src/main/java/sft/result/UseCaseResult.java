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
package sft.result;

import sft.UseCase;

import java.util.ArrayList;

import static sft.result.Issue.FAILED;

public class UseCaseResult {

    public final UseCase useCase;
    public ArrayList<UseCaseResult> subUseCaseResults = new ArrayList<UseCaseResult>();
    public ArrayList<ScenarioResult> scenarioResults = new ArrayList<ScenarioResult>();
    public ContextResult beforeResult;
    public ContextResult afterResult;

    public UseCaseResult(UseCase useCase) {
        this.useCase = useCase;
    }

    public Issue getIssue() {
        if (useCase.shouldBeIgnored()) {
            return Issue.IGNORED;
        }
        if(beforeResult.issue == FAILED){
            return FAILED;
        }
        if(afterResult.issue == FAILED){
            return FAILED;
        }

        Issue result = Issue.IGNORED;
        for (ScenarioResult scenarioResult : scenarioResults) {
            switch (scenarioResult.issue) {
                case FAILED:
                    return Issue.FAILED;
                case SUCCEEDED:
                    result = Issue.SUCCEEDED;
            }
        }
        for (UseCaseResult subUseCaseResult : subUseCaseResults) {
            switch (subUseCaseResult.getIssue()) {
                case FAILED:
                    return Issue.FAILED;
                case SUCCEEDED:
                    result = Issue.SUCCEEDED;
            }
        }
        return result;
    }

}
