package com.vaadin.testUI;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.data.ListDataProvider;
import com.vaadin.tests.AbstractTestUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;

public class ListSelectOptionClick extends AbstractTestUI {

    private Label counterLbl = new Label();
    private int counter = 0;

    @Override
    protected void setup(VaadinRequest request) {
        ListSelect<String> multiSelect = new ListSelect<String>();
        counterLbl.setValue("0");
        List<String> options = new ArrayList<String>();
        options.add("item1");
        options.add("item2");
        options.add("item3");
        multiSelect.setDataProvider(new ListDataProvider<String>(options));
        multiSelect.select("item1");
        multiSelect.addSelectionListener(event -> {
            counter++;
            counterLbl.setValue("" + counter + ": " + event.getValue());
        });
        addComponent(multiSelect);
        counterLbl.setId("multiCounterLbl");
        addComponent(counterLbl);
    }

    @Override
    protected String getTestDescription() {
        return "Test that user can pick option from ListSelectElement by call click() method";
    }

    @Override
    protected Integer getTicketNumber() {
        return null;
    }

}
