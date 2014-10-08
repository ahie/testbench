/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.testUI;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.tests.AbstractTestUI;
import com.vaadin.ui.Button;

public class ElementExists extends AbstractTestUI {

    @WebServlet(value = { "/VAADIN/*", "/ElementExists/*" }, asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = ElementExists.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void setup(VaadinRequest request) {
        Button button = new Button("b");
        addComponent(button);
    }

    @Override
    protected String getTestDescription() {
        return "The TestBench method exists() should not throw an exception even when the UI "
                + "has not been initialized.";
    }

    @Override
    protected Integer getTicketNumber() {
        return 14808;
    }

}
