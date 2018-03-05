package com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking;

import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.Alias;
import com.github.mkolisnyk.sirius.client.ui.FindBy;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import com.github.mkolisnyk.sirius.client.ui.controls.Edit;

@Alias("Deposit")
public class CustomerDepositPage extends CustomerCommonPage {

    public CustomerDepositPage(WebDriver driverValue) {
        super(driverValue);
    }

    @Alias("Deposit Amount")
    @FindBy(locator = "//input[@type='number']")
    public Edit editDepositAmount;
    
    @Alias("Submit Deposit")
    @FindBy(locator = "//button[text() = 'Deposit' and @type = 'submit']")
    public Control buttonSubmitDeposit;

    @Override
    public Page navigate() throws Exception {
        this.buttonDeposit.click();
        return this;
    }
}
