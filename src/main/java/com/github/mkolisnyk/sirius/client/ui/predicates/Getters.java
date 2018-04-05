package com.github.mkolisnyk.sirius.client.ui.predicates;

import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.exists;

/**
 * Collects predicates responsible for various get operations.
 * @author Mykola Kolisnyk
 *
 */
public final class Getters {
    private Getters() {
    }

    /**
     * Predicate for retrieving parent object of the control.
     * @return operation object which is applicable for Control instance.
     */
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
    /**
     * Predicate for control text retrieval.
     * @return operation object which is applicable for Control instance.
     */
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
    /**
     * Predicate for control value retrieval.
     * @return operation object which is applicable for Control instance.
     */
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
    /**
     * Predicate for control attribute retrieval.
     * @param name the name of the attribute to get value of.
     * @return operation object which is applicable for Control instance.
     */
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
