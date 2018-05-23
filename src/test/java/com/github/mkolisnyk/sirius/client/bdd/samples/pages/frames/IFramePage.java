package com.github.mkolisnyk.sirius.client.bdd.samples.pages.frames;

import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.click;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.current;

import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.Page;

public class IFramePage extends FramesAndWindowsCommonPage {

    public IFramePage(WebDriver driverValue) {
        super(driverValue);
    }

    @Override
    public Page navigate() throws Exception {
        super.navigate();
        this.tabIFrame.perform(click()).getParent().verify(current());
        return this;
    }
}
