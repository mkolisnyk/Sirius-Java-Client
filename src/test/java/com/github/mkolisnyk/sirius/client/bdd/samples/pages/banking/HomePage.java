package com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking;

import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.click;
import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.waitFor;
import static com.github.mkolisnyk.sirius.client.ui.predicates.Setters.value;
import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.Alias;
import com.github.mkolisnyk.sirius.client.ui.FindBy;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

@Alias("Banking Home")
public class HomePage extends Page {

    public HomePage(WebDriver driverValue) {
        super(driverValue);
        // TODO Auto-generated constructor stub
    }

    @Alias("Customer Login")
    @FindBy(locator = "//button[text() = 'Customer Login']")
    public Control buttonCustomerLogin;

    @Alias("Banking Manager Login")
    @FindBy(locator = "//button[text() = 'Bank Manager Login']")
    public Control buttonBankManagerLogin;

    @Override
    public Page navigate() throws Exception {
        this.getDriver().get("http://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");
        return this;
    }

    public CustomerCommonPage loginAsCustomer(String name) throws Exception {
        CustomerLoginPage loginPage = this.buttonCustomerLogin
                .perform(click()).perform(waitFor(CustomerLoginPage.class));
        loginPage.selectUser.set(value(name));
        return loginPage.buttonLogin
                .perform(click()).perform(waitFor(CustomerCommonPage.class));
    }
}
