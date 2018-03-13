package com.github.mkolisnyk.sirius.client.ui.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.github.mkolisnyk.sirius.client.ui.Page;

/**
 * 
 * @author Mykola Kolisnyk
 */
public class SelectList extends Control {

    /**
     * 
     * @param parentValue
     * @param locatorValue
     */
    public SelectList(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }

    /**
     * 
     * @return
     */
    public Select getSelect() {
        return new Select(super.element());
    }
    /**
     * 
     * @param value
     */
    public void selectByText(String value) {
        this.exists();
        this.getSelect().selectByVisibleText(value);
    }
}
