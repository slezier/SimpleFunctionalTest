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

import sft.DefaultConfiguration;
import sft.decorators.Decorator;

public class SequenceDiagram extends Decorator {

    public SequenceDiagram(DefaultConfiguration configuration, String... parameters) {
        super(configuration, parameters);
    }

    @Override
    public boolean comply(Decorator decorator) {
        return decorator instanceof SequenceDiagram;
    }
}
