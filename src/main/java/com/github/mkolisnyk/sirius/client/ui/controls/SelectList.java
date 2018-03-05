package com.github.mkolisnyk.sirius.client.ui.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.github.mkolisnyk.sirius.client.ui.Page;

public class SelectList extends Control {

    public SelectList(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }

    public Select getSelect() {
        return new Select(super.element());
    }
    public void selectByText(String value) {
        this.exists();
        this.getSelect().selectByVisibleText(value);
    }
}
