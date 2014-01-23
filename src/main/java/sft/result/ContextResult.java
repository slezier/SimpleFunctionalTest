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

import sft.ContextHandler;
import sft.report.Issue;

import static sft.report.Issue.FAILED;
import static sft.report.Issue.IGNORED;
import static sft.report.Issue.SUCCEEDED;

public class ContextResult {

    public final ContextHandler contextHandler;
    public final Throwable exception;
    public final Issue issue;

    private ContextResult(ContextHandler contextHandler,Issue issue, Throwable throwable) {
        this.contextHandler = contextHandler;
        this.issue = issue;
        this.exception = throwable;
    }

    public boolean isSuccessful(){
        return issue == SUCCEEDED;
    }

    public static ContextResult success(ContextHandler contextHandler) {
        return new ContextResult(contextHandler, SUCCEEDED,null);
    }

    public static ContextResult failed(ContextHandler contextHandler, Throwable e) {
        return new ContextResult(contextHandler,FAILED,e);
    }

    public static ContextResult ignored(ContextHandler contextHandler) {
        return new ContextResult(contextHandler,IGNORED,null);
    }
}
