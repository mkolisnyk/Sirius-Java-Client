package com.github.mkolisnyk.sirius.client.ui.controls;

import org.openqa.selenium.By;

import com.github.mkolisnyk.sirius.client.Configuration;
import com.github.mkolisnyk.sirius.client.ui.Page;

/**
 * Control extension which corresponds to the edit control.
 * @author Mykola Kolisnyk
 *
 */
public class Edit extends Control {

    /**
     * Default constructor.
     * @param parentValue parent page object reference.
     * @param locatorValue locator to current element.
     */
    public Edit(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }

    /**
     * Enters the text into the field.
     * @param value the text to enter.
     * @return current control to continue chain operations.
     */
    public Control setText(String value) {
        if (Configuration.platform().isAndroidNative()) {
            this.getParent().hideKeyboard();
        }
        this.click();
        this.element().clear();
        this.element().sendKeys(value);
        if (Configuration.platform().isAndroidNative()) {
            this.getParent().hideKeyboard();
        }
        return this;
    }

    @Override
    public String getText() {
        return super.element().getAttribute("value");
    }
}
