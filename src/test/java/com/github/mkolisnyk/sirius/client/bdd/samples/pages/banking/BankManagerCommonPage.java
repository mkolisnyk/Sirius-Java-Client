package com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking;

import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.Alias;
import com.github.mkolisnyk.sirius.client.ui.FindBy;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

@Alias("Banking Manager Home")
public class BankManagerCommonPage extends Page {

    public BankManagerCommonPage(WebDriver driverValue) {
        super(driverValue);
        // TODO Auto-generated constructor stub
    }

    @Alias("Add Customer")
    @FindBy(locator = "//button[contains(text(),'Add Customer')]")
    public Control buttonAddCustomer;
    @Alias("Open Account")
    @FindBy(locator = "//button[contains(text(),'Open Account')]")
    public Control buttonOpenAccount;
    @Alias("Customers")
    @FindBy(locator = "//button[contains(text(),'Customers')]")
    public Control buttonCustomers;
}
