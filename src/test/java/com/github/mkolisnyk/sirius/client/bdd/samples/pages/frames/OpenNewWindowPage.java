package com.github.mkolisnyk.sirius.client.bdd.samples.pages.frames;

import org.openqa.selenium.WebDriver;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.current;
import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.click;

import com.github.mkolisnyk.sirius.client.ui.FindBy;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

public class OpenNewWindowPage extends FramesAndWindowsCommonPage {
    @FindBy(locator = "//*[contains(text(), 'open a new window now')]")
    public Control labelPrompt;
    @FindBy(locator = "link=Click Here")
    public Control buttonClickHere;
    public OpenNewWindowPage(WebDriver driverValue) {
        super(driverValue);
    }

    @Override
    public Page navigate() throws Exception {
        super.navigate();
        this.tabOpenNewWindow.perform(click()).getParent().verify(current());
        return this;
    }
}
