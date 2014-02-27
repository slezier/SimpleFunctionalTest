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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sft.Report;

public class Style implements Decorator {
    private String[] style;

    @Override
    public Decorator withParameters(String... parameters) {
        if(parameters == null || parameters.length == 0 ){
            throw new RuntimeException("Style decorator need one or more parameters");
        }
        style = parameters;
        return this;
    }

    @Override
    public String applyOnUseCase(String result){
        final Document parse = Jsoup.parse(result);

        final Elements elements = parse.select(".useCase");
        addStyleTo(elements);

        return parse.toString();
    }

    private void addStyleTo(Elements elements) {
        for (String st : style) {
            elements.addClass(st);
        }
    }

    @Override
    public String applyOnScenario(String result){
//        final Document parse = Jsoup.parse(result);
//        parse.select("scenario").addClass(style);
//        return parse.toString();
        return result;
    }

    @Override
    public String applyOnFixture(String result){
//        final Document parse = Jsoup.parse(result);
//        parse.select("instruction").addClass(style);
//        return parse.toString();
        return result;
    }
}
