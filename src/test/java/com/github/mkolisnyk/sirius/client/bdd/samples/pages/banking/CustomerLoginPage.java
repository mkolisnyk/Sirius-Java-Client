package com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking;

import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.Alias;
import com.github.mkolisnyk.sirius.client.ui.FindBy;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import com.github.mkolisnyk.sirius.client.ui.controls.SelectList;

@Alias("Customer Login")
public class CustomerLoginPage extends Page {

    public CustomerLoginPage(WebDriver driverValue) {
        super(driverValue);
    }

    @Alias("Select User")
    @FindBy(locator = "userSelect")
    public SelectList selectUser;
    
    @Alias("Login")
    @FindBy(locator = "//button[text() = 'Login']", excludeFromSearch = true)
    public Control buttonLogin;
}
