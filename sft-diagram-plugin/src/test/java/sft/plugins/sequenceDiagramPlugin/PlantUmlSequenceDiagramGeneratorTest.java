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

import org.junit.Test;
import sft.Decorate;
import sft.Fixture;
import sft.FixtureCall;
import sft.Text;
import sft.result.FixtureCallResult;
import sft.result.Issue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class PlantUmlSequenceDiagramGeneratorTest {

    private PlantUmlSequenceDiagramGenerator tested = new PlantUmlSequenceDiagramGenerator();
    private final static String DEFAULT_STYLE = new PlantUmlSequenceDiagramGenerator().style;

    @Test
    public void should_generates_aPlantUmlScript() throws Exception {
        List<FixtureCallResult> fixtureCallResults = new ArrayList<FixtureCallResult>();

        String actualScript = tested.getPlantUmlScript(fixtureCallResults);

        Lines lines = new Lines(actualScript);
        assertEquals("@startuml", lines.firstLine());
        assertEquals("@enduml", lines.lastLine());
    }

    @Test
    public void should_addAStyleSequence_withDefaultValue() throws Exception {
        List<FixtureCallResult> fixtureCallResults = new ArrayList<FixtureCallResult>();

        String actualScript = tested.getPlantUmlScript(fixtureCallResults);

        String actual = new Text(actualScript).after("@startuml\n").until(DEFAULT_STYLE.length());
        assertEquals(DEFAULT_STYLE, actual);
    }

    @Test
    public void should_addASequence_forEachFeatureCallResults() throws Exception {
        FixtureCallResult  fixtureCallResult1 = new FixtureCallResult(new FixtureCall("name1",12,new Fixture(getMethod1(),null),new ArrayList<String>(),0), Issue.SUCCEEDED);
        FixtureCallResult  fixtureCallResult2 = new FixtureCallResult(new FixtureCall("name2",12,new Fixture(getMethod2(),null),new ArrayList<String>(),0), Issue.SUCCEEDED);

        List<FixtureCallResult> fixtureCallResults = new ArrayList<FixtureCallResult>();
        fixtureCallResults.add(fixtureCallResult1);
        fixtureCallResults.add(fixtureCallResult2);


        String actualScript = tested.getPlantUmlScript(fixtureCallResults);

        Lines lines = new Lines(actualScript);

        assertEquals("alice -[#green]> bob:Method 1", lines.get(12));
        assertEquals("bob --[#green]> alice:method2", lines.get(13));
    }

    @Test
    public void should_addASequenceColor_dependingTheIssue() throws Exception {
        FixtureCallResult  fixtureCallResult1 = new FixtureCallResult(new FixtureCall("name1",12,new Fixture(getMethod1(),null),new ArrayList<String>(),0), Issue.SUCCEEDED);
        FixtureCallResult  fixtureCallResult2 = new FixtureCallResult(new FixtureCall("name2",12,new Fixture(getMethod2(),null),new ArrayList<String>(),0), Issue.FAILED);
        FixtureCallResult  fixtureCallResult3 = new FixtureCallResult(new FixtureCall("name3",12,new Fixture(getMethod3(),null),new ArrayList<String>(),0), Issue.IGNORED);

        List<FixtureCallResult> fixtureCallResults = new ArrayList<FixtureCallResult>();
        fixtureCallResults.add(fixtureCallResult1);
        fixtureCallResults.add(fixtureCallResult2);
        fixtureCallResults.add(fixtureCallResult3);

        String actualScript = tested.getPlantUmlScript(fixtureCallResults);

        Lines lines = new Lines(actualScript);

        assertEquals("alice -"+tested.succeededColor+"> bob:Method 1", lines.get(12));
        assertEquals("bob --"+tested.failedColor+"> alice:method2", lines.get(13));
        assertEquals("bob -"+tested.ignoredColor+"> bob:Method 3", lines.get(14));
    }

    private Method getMethod1() throws NoSuchMethodException {
        return this.getClass().getMethod("method1");
    }
    private Method getMethod2() throws NoSuchMethodException {
        return this.getClass().getMethod("method2");
    }
    private Method getMethod3() throws NoSuchMethodException {
        return this.getClass().getMethod("method3");
    }

    @Decorate(decorator = SequenceDiagram.class, parameters = "alice -> bob")
    public void method1(){}

    @Decorate(decorator = SequenceDiagram.class, parameters = "bob --> alice")
    @sft.Text("method2")
    public void method2(){}

    @Decorate(decorator = SequenceDiagram.class, parameters = "bob -> bob")
    public void method3(){}

    class Lines{
        private final String[] lines;
        Lines(String text){
            lines = text.split("\\n");
        }

        public String firstLine() {
            return lines[0];
        }
        public String lastLine() {
            return lines[lines.length-1];
        }

        public String get(int i) {
            return lines[i];
        }
    }

    class Text{
        private final String text;
        Text(String text){
            this.text = text;
        }


        public Text after(String template) {
            return new Text(text.substring(text.indexOf(template)+template.length()));
        }

        public String until(int length) {
            return text.substring(0,length);
        }
    }
}