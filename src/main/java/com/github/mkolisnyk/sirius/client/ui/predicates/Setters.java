package com.github.mkolisnyk.sirius.client.ui.predicates;

import com.github.mkolisnyk.sirius.client.ui.controls.Editable;

/**
 * Collects predicates which are responsible for various set operations.
 * @author Mykola Kolisnyk
 *
 */
public final class Setters {
    private Setters() {
    }

    /**
     * Predicate for setting value for the control. It is applicable
     * for controls of {@link Editable} or any extended classes.
     * @param text the value to set.
     * @return operation object which can be applicable for Editable elements.
     */
    public static Operation<Editable, Editable> value(final String text) {
        return new Operation<Editable, Editable>() {

            @Override
            public Editable apply(Editable item) {
                return item.setValue(text);
            }

            @Override
            public String description(Editable item) {
                return null;
            }
        };
    }
}
