package com.vaadin.tests.testbenchapi.components.button;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.testUI.ButtonUI;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.tests.testbenchapi.MultiBrowserTest;

public class ButtonUIIT extends MultiBrowserTest {
    @Before
    public void init() {
        openTestURL();
    }

    @Test
    public void testButtonWithQUIETStyle() {
        ButtonElement button = $(ButtonElement.class).id(ButtonUI.QUITE_BUTTON_ID);
        TextFieldElement field = $(TextFieldElement.class).first();
        button.click();
        Assert.assertEquals("Clicked",field.getValue());
    }


    @Test
    public void testButtonWithQUIETStyleNoCaption() {
        ButtonElement button = $(ButtonElement.class).id(ButtonUI.QUITE_BUTTON_NO_CAPTION_ID);
        TextFieldElement field = $(TextFieldElement.class).first();
        button.click();
        Assert.assertEquals("Clicked",field.getValue());
    }


    @Test
    public void testButton_clickButtonWithSleep_TextFieldWorkAsExpected() {
        openTestURL();
        ButtonElement button = $(ButtonElement.class).id(ButtonUI.NORMAL_BUTTON_ID);
        TextFieldElement field = $(TextFieldElement.class).id(ButtonUI.TEXT_FIELD_ID);
        button.click();
        Assert.assertEquals("Clicked", field.getValue());
    }

    @Test
    public void testButton_clickButtonWithSleep_LabelWorkAsExpected() {
        openTestURL();
        ButtonElement button = $(ButtonElement.class).id(ButtonUI.NORMAL_BUTTON_ID);
        LabelElement label = $(LabelElement.class).id(ButtonUI.LABEL_ID);
        button.click();

        Assert.assertEquals("Clicked", label.getText());
    }
}

