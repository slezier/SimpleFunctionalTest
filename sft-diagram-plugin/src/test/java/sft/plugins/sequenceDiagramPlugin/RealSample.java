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
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.Using;

@RunWith(SimpleFunctionalTest.class)
@Using(SequenceDiagramConfiguration.class)
public class RealSample {

    @Test
    public void classicCryptoExchange(){
        authenticationRequest();
        authenticationResponse();

        anotherAuthenticationRequest("bad authentication");
        authenticationCheck();
        anotherAuthenticationResponse();
    }

    @Decorate(decorator = SequenceDiagram.class, parameters = "Alice -> Bob")
    private void authenticationRequest(){
    }

    @Decorate(decorator = SequenceDiagram.class, parameters = "Bob --> Alice")
    private void authenticationResponse(){
    }

    @Decorate(decorator = SequenceDiagram.class, parameters = "Alice -> Bob")
    @Text("another authentication request with ${error}")
    private void anotherAuthenticationRequest(String error){
        throw new RuntimeException("Boom");
    }

    @Decorate(decorator = SequenceDiagram.class, parameters = "Bob -> Bob")
    private void authenticationCheck() {
    }

    @Decorate(decorator = SequenceDiagram.class, parameters = "Bob --> Alice")
    private void anotherAuthenticationResponse(){
    }
}
