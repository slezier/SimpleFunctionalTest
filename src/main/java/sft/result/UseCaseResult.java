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
import sft.report.Issue;

import java.util.ArrayList;

public class UseCaseResult {

    public final UseCase useCase ;

    public ArrayList<UseCaseResult> subUseCaseResults=new ArrayList<UseCaseResult>();
    public ArrayList<ScenarioResult> scenarioResults = new ArrayList<ScenarioResult>();
    public ContextResult beforeResult;
    public ContextResult afterResult;

    public UseCaseResult(UseCase useCase ){
        this.useCase = useCase;
    }

    public Issue getIssue(){
        if(useCase.shouldBeIgnored()){
            return Issue.IGNORED;
        }
        Issue result = Issue.IGNORED;
        for (ScenarioResult scenarioResult : scenarioResults) {
            switch (scenarioResult.issue){
                case FAILED:
                    return Issue.FAILED;
                case SUCCEEDED:
                    result= Issue.SUCCEEDED;
            }
        }
        for (UseCaseResult subUseCaseResult : subUseCaseResults) {
            switch (subUseCaseResult.getIssue()){
                case FAILED:
                    return Issue.FAILED;
                case SUCCEEDED:
                    result= Issue.SUCCEEDED;
            }
        }
        return result;
    }

    private static int nbSp=0;
    @Override
    public String toString(){
        String result = space(nbSp)+":UseCase{\n" ;
        nbSp++;
        result +=  space(nbSp)+"name:"+useCase.getName()+" "+getIssue()+"\n";

        if(!scenarioResults.isEmpty()){
            result += space(nbSp)+"scenarios[\n";
            nbSp++;
            for (ScenarioResult scenarioResult : scenarioResults) {
                result += space(nbSp)+scenarioResult.scenario.getName()+" "+scenarioResult.issue+"\n";
            }
            nbSp--;
            result += space(nbSp)+"]\n";
        }

        if(!subUseCaseResults.isEmpty()){
            result += space(nbSp)+"subUseCase[\n";
            nbSp++;
            for (UseCaseResult useCaseResult : subUseCaseResults) {
                result += useCaseResult.toString();
            }
            nbSp--;
            result += space(nbSp)+"]\n";
        }
        nbSp--;
        result += space(nbSp)+ "}\n";

        return result;
    }

    public String space(int nbSp){
        String result ="";
        for(int i= 0; i<nbSp;i++){
            result +=" ";
        }
        return result;
    }

}
