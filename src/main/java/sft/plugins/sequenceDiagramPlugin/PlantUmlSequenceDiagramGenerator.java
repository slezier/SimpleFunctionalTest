/*******************************************************************************
 * Copyright (c) 2014 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft.plugins.sequenceDiagramPlugin;

import sft.FixtureCall;
import sft.result.FixtureCallResult;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class PlantUmlSequenceDiagramGenerator {

    public String style =
            "skinparam sequence {\n" +
                    "\tActorBorderColor black\n" +
                    "\tLifeLineBorderColor black\n" +
                    "\t\n" +
                    "\tParticipantBackgroundColor white\n" +
                    "\tParticipantBorderColor black\n" +
                    "\tParticipantFontColor black\n" +
                    "\t\n" +
                    "\tActorFontColor black\n" +
                    "\tActorFontSize 17\n" +
                    "}";
    public String succeededColor  = "[#green]";
    public String failedColor  = "[#red]";
    public String ignoredColor  = "[#yellow]";

    public String getPlantUmlScript(List<FixtureCallResult> fixtureCallResults) {
        String plantUml = "@startuml\n" + style + "\n";
        for (FixtureCallResult fixtureCallResut : fixtureCallResults) {
            plantUml += generateSequence(fixtureCallResut);
        }
        plantUml += "@enduml";
        return plantUml;
    }

    private String generateSequence(FixtureCallResult fixtureCallResut) {
        String text = generateInstructionWithParameter(fixtureCallResut.fixtureCall);
        String sequence = fixtureCallResut.fixtureCall.fixture.decorator.parameters[0];
        return generateEmptyLines(fixtureCallResut) +
                generateSequenceColor(fixtureCallResut, sequence) + ":" + text + "\n";
    }

    private String generateSequenceColor(FixtureCallResult fixtureCallResut, String sequence) {
        String newSequence = sequence.replaceAll("->", "-" + getColor(fixtureCallResut) + ">")
                .replaceAll("-\\\\", "-" + getColor(fixtureCallResut) + "\\")
                .replaceAll("-/", "-" + getColor(fixtureCallResut) + "/");
        if (newSequence.equals(sequence)) {

            newSequence = sequence.replaceAll("\\\\-", "\\\\" + getColor(fixtureCallResut) + "-")
                    .replaceAll("<-", "<" + getColor(fixtureCallResut) + "-")
                    .replaceAll("/-", "/" + getColor(fixtureCallResut) + "-");
        }
        return newSequence;
    }

    private String generateEmptyLines(FixtureCallResult fixtureCallResut) {
        String result = "";
        for (int i = 0; i < fixtureCallResut.fixtureCall.emptyLine; i++) {
            result += "|||\n";
        }
        return result;
    }

    private String getColor(FixtureCallResult fixtureCallResut) {
        switch (fixtureCallResut.issue) {
            case SUCCEEDED:
                return succeededColor;
            case FAILED:
                return failedColor;
            case IGNORED:
            default:
                return ignoredColor;
        }
    }

    public String generateInstructionWithParameter(FixtureCall testFixture) {
        String instruction = testFixture.fixture.getText();
        for (Map.Entry<String, String> parameter : testFixture.getParameters().entrySet()) {
            String value = Matcher.quoteReplacement(parameter.getValue());
            instruction = instruction.replaceAll("\\$\\{" + parameter.getKey() + "\\}", "<i>" + value + "</i>");
        }
        return instruction;
    }

}
