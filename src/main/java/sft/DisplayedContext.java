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
package sft;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class DisplayedContext {
    private final ArrayList<Field> fields;
    private final Object object;

    public DisplayedContext(Object object, ArrayList<Field> fields) {
        this.object = object;
        this.fields = fields;
    }

    public ArrayList<String> getText() {
        ArrayList<String> results = new ArrayList<String>();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null) {
                    results.add(value.toString());
                    field.set(object,null);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }
}
