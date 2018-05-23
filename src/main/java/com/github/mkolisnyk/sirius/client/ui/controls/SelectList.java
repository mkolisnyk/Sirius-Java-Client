package com.github.mkolisnyk.sirius.client.ui.controls;

import static com.github.mkolisnyk.sirius.client.ui.predicates.States.exists;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.github.mkolisnyk.sirius.client.ui.Page;

/**
 * Control extension which wraps Select instance and corresponds to the list objects
 * (e.g. drop-down lists, select lists, multi-select lists).
 * @author Mykola Kolisnyk
 */
public class SelectList extends Editable {

    /**
     * Default constructor which binds page the control belongs to and the locator to
     * find the element on page.
     * @param parentValue the instance of the page class containing current control.
     * @param locatorValue the string used to locate current control.
     */
    public SelectList(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }

    /**
     * Initialises and returns core Select object from WebDriver client library.
     * @return reference to core Select object.
     */
    public Select getSelect() {
        return new Select(super.element());
    }
    /**
     * Performs list item selection by the text specified.
     * @param value the value of item to select.
     * @return current object for chain operations.
     */
    public SelectList selectByText(String value) {
        this.verify(exists());
        this.getSelect().selectByVisibleText(value);
        return this;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.sirius.client.ui.controls.Control#setValue(java.lang.String)
     */
    @Override
    public Editable setValue(String value) {
        return this.selectByText(value);
    }
}
