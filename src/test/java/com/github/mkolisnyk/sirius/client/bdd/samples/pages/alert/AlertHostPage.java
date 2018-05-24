package com.github.mkolisnyk.sirius.client.bdd.samples.pages.alert;

import java.io.File;

import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.FindBy;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

public class AlertHostPage extends Page {

    @FindBy(locator = "//button[text()='Alert']")
    public Control buttonAlert;
    @FindBy(locator = "//button[text()='Confirmation']")
    public Control buttonConfirmation;
    @FindBy(locator = "sample_prompt")
    public Control buttonPrompt;

    public AlertHostPage(WebDriver driverValue) {
        super(driverValue);
    }

    @Override
    public Page navigate() throws Exception {
        this.getDriver().get(new File("src/test/resources/alert_sample.html").toURI().toURL().toExternalForm());
        return this;
    }
}
