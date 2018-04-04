package com.github.mkolisnyk.sirius.client.ui.predicates;

import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import com.github.mkolisnyk.sirius.client.ui.controls.Edit;

public class Setters {
    public static Operation<Control, Control> value(final String text) {
        return new Operation<Control, Control>() {

            @Override
            public Control apply(Control item) {
                return item.setValue(text);
            }

            @Override
            public String description(Control item) {
                return null;
            }
        };
    }
}
