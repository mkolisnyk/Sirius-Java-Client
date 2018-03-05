package com.github.mkolisnyk.sirius.client.ui.controls;

import org.openqa.selenium.By;

import com.github.mkolisnyk.sirius.client.Configuration;
import com.github.mkolisnyk.sirius.client.ui.Page;

public class Edit extends Control {

    public Edit(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }

    public void setText(String value) throws Exception {
        if (Configuration.platform().isAndroidNative()) {
            this.getParent().hideKeyboard();
        }
        this.click();
        this.element().clear();
        this.element().sendKeys(value);
        if (Configuration.platform().isAndroidNative()) {
            this.getParent().hideKeyboard();
        }
    }

    @Override
    public String getText() {
        return super.element().getAttribute("value");
    }
}
