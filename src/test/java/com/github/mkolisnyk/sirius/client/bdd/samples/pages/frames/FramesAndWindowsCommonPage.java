package com.github.mkolisnyk.sirius.client.bdd.samples.pages.frames;

import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.FindBy;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

public class FramesAndWindowsCommonPage extends Page {

    @FindBy(locator = "Open New Tab")
    public Control tabOpenNewTab;

    @FindBy(locator = "Open New Window")
    public Control tabOpenNewWindow;

    @FindBy(locator = "iFrame")
    public Control tabIFrame;

    public FramesAndWindowsCommonPage(WebDriver driverValue) {
        super(driverValue);
    }

    @Override
    public Page navigate() throws Exception {
        this.getDriver().get("http://www.globalsqa.com/demo-site/frames-and-windows/");
        return this;
    }
}
