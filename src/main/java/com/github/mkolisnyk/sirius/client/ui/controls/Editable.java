package com.github.mkolisnyk.sirius.client.ui.controls;

import org.openqa.selenium.By;

import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.predicates.Operation;

/**
 * Base class for all controls which have modifiable states
 * like edit fields, check boxes etc.
 * @author Mykola Kolisnyk
 */
public abstract class Editable extends Control {

    /**
     * Default constructor which binds page the control belongs to and the locator to
     * find the element on page.
     * @param parentValue the instance of the page class containing current control.
     * @param locatorValue the string used to locate current control.
     */
    public Editable(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }
    /**
     * .
     * @param predicate .
     * @return .
     */
    public Editable set(Operation<? extends Editable, Editable> predicate) {
        return predicate.apply(this);
    }
    /**
     * Common method for setting values. For extended classes
     * it is supposed to set the value for the writable elements
     * like edit boxes, check boxes, radio buttons. For others it shouldn't
     * do anything.
     * @param value the value to set.
     * @return current control. It is needed for operation chains.
     */
    public abstract Editable setValue(String value);
}
