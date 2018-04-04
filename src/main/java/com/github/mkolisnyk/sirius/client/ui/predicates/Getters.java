package com.github.mkolisnyk.sirius.client.ui.predicates;

import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.exists;

public class Getters {
    public static Operation<Page, Control> parent() {
        return new Operation<Page, Control>() {

            @Override
            public Page apply(Control item) {
                return item.getParent();
            }

            @Override
            public String description(Control item) {
                return null;
            }
        };
    }
    public static Operation<String, Control> text() {
        return new Operation<String, Control>() {

            @Override
            public String apply(Control item) {
                return item.getText();
            }

            @Override
            public String description(Control item) {
                return null;
            }
        };
    }
    public static Operation<String, Control> value() {
        return new Operation<String, Control>() {

            @Override
            public String apply(Control item) {
                return item.getValue();
            }

            @Override
            public String description(Control item) {
                return null;
            }
        };
    }
    public static Operation<String, Control> attribute(final String name) {
        return new Operation<String, Control>() {

            @Override
            public String apply(Control item) {
                item.verify(exists());
                return item.element().getAttribute(name);
            }

            @Override
            public String description(Control item) {
                return null;
            }
        };
    }
}
