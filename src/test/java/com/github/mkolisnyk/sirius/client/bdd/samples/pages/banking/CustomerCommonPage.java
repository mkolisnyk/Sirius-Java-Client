package com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking;

import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.Alias;
import com.github.mkolisnyk.sirius.client.ui.FindBy;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import com.github.mkolisnyk.sirius.client.ui.controls.SelectList;

@Alias("Customer Home")
public class CustomerCommonPage extends Page {

    public CustomerCommonPage(WebDriver driverValue) {
        super(driverValue);
        // TODO Auto-generated constructor stub
    }

    @Alias("Balance")
    @FindBy(locator = "xpath=//div[@class='center']/strong[2]")
    public Control labelBalance;
    
    @Alias("Account Select")
    @FindBy(locator = "accountSelect")
    public SelectList selectAccount;
    
    @Alias("Deposit")
    @FindBy(locator = "//button[contains(text(), 'Deposit')]")
    public Control buttonDeposit;
    
    @Alias("Withdraw")
    @FindBy(locator = "//button[contains(text(), 'Withdraw')]")
    public Control buttonWithdraw;
}
