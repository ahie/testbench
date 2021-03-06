package com.vaadin.testUI;

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.data.DataProvider;
import com.vaadin.server.data.ListDataProvider;
import com.vaadin.tests.AbstractTestUI;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;

public class GridUI extends AbstractTestUI {

    @Override
    protected void setup(VaadinRequest request) {
        int rowCount = 100;
        if (request.getParameter("rowCount") != null) {
            rowCount = Integer.parseInt(request.getParameter("rowCount"));
        }


        DataProvider<Item> ds = getMockData(rowCount);
        final Grid<Item> grid = new Grid<Item>();
        grid.setDataProvider(ds);
        grid.addColumn("foo", Item::getFoo);
        grid.addColumn("bar", Item::getBar);

        grid.setDetailsGenerator(item -> {
            return new Label("Foo = " + item.getFoo() + " Bar = "
                    + item.getBar());
        });
        grid.addItemClickListener(event -> {
            if (event.getMouseEventDetails().isDoubleClick()) {
                grid.setDetailsVisible(event.getItem(),
                        !grid.isDetailsVisible(event.getItem()));
            }
        });

        addComponent(grid);
    }

    private DataProvider<Item> getMockData(int rowCount) {
        Collection<Item> data = new ArrayList<Item>();
        for (int i = 0; i < rowCount; i++) {
            Item item = new Item("foo " + i, "bar " + i);
            data.add(item);
        }
        return new ListDataProvider<Item>(data);
    }

    @Override
    protected String getTestDescription() {
        return "Test UI for Grid element API";
    }

    @Override
    protected Integer getTicketNumber() {
        return null;
    }

    private class Item {
        private String foo;
        private String bar;

        public Item(String foo, String bar) {
            this.foo = foo;
            this.bar = bar;
        }

        public String getFoo() {
            return foo;
        }

        public String getBar() {
            return bar;
        }
    }
}
