package com.github.mkolisnyk.sirius.client.ui.controls;

import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.click;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.checked;

import org.openqa.selenium.By;

import com.github.mkolisnyk.sirius.client.ui.Page;

/**
 * Wrapper class which is abstraction over the radio buttons or any other similar
 * items which can be checked.
 * @author Mykola Kolisnyk
 *
 */
public class RadioButton extends Editable {

    /**
     * Default constructor which binds page the control belongs to and the locator to
     * find the element on page.
     * @param parentValue the instance of the page class containing current control.
     * @param locatorValue the string used to locate current control.
     */
    public RadioButton(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }

    /**
     * Sets check mark on the object if it's not already checked.
     * @return current element for further chain operations.
     */
    public Editable check() {
        if (!is(checked())) {
            click();
        }
        return this;
    }

    @Override
    public Editable setValue(String value) {
        if (value.equalsIgnoreCase("y")
                || value.equalsIgnoreCase("true")) {
            this.click();
        }
        return this;
    }
}
