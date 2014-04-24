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

import sft.DefaultConfiguration;
import sft.result.FixtureCallResult;

import java.util.List;

public class Table extends Decorator {

    public Table(DefaultConfiguration configuration, String... parameters) {
        super(configuration, parameters);
    }

    @Override
    public String applyOnFixtures(List<String> instructions, List<FixtureCallResult> fixtureCallResuts) {
        return getImplementation().applyOnFixtures(instructions,fixtureCallResuts);
    }

    @Override
    public boolean comply(Decorator other) {
        return other.getClass() == Table.class;
    }
}
