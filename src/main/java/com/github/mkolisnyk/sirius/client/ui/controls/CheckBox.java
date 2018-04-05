package com.github.mkolisnyk.sirius.client.ui.controls;
import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.click;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.checked;

import org.openqa.selenium.By;

import com.github.mkolisnyk.sirius.client.ui.Page;

/**
 * Wrapper class which is abstraction over the check box elements
 * of any similar looking elements which have checked/unchecked state.
 * @author Mykola Kolisnyk
 *
 */
public class CheckBox extends RadioButton {

    /**
     * Default constructor which binds page the control belongs to and the locator to
     * find the element on page.
     * @param parentValue the instance of the page class containing current control.
     * @param locatorValue the string used to locate current control.
     */
    public CheckBox(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }

    /**
     * Resets check mark from the field.
     * @return current control for further chain operations.
     */
    public CheckBox uncheck() {
        if (is(checked())) {
            perform(click());
        }
        return this;
    }
    /**
     * Sets checked/unchecked state depending on input parameter.
     * @param checked check state to achieve.
     * @return current control for further chain operations.
     */
    public Editable setState(boolean checked) {
        if (checked) {
            check();
        } else {
            uncheck();
        }
        return this;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.sirius.client.ui.controls.RadioButton#setValue(java.lang.String)
     */
    @Override
    public Editable setValue(String value) {
        return setState(value.equalsIgnoreCase("y")
                || value.equalsIgnoreCase("true"));
    }
}
