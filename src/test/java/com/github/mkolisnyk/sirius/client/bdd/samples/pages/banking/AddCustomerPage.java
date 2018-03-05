package com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking;

import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.Alias;
import com.github.mkolisnyk.sirius.client.ui.FindBy;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import com.github.mkolisnyk.sirius.client.ui.controls.Edit;

@Alias("Add Customer")
public class AddCustomerPage extends BankManagerCommonPage {

    public AddCustomerPage(WebDriver driverValue) {
        super(driverValue);
        // TODO Auto-generated constructor stub
    }

    @Alias("First Name")
    @FindBy(locator = "//input[@type='text']")
    public Edit editFirstName;
    @Alias("Last Name")
    @FindBy(locator = "xpath=(//input[@type='text'])[2]")
    public Edit editLastName;

    @Alias("Post Code")
    @FindBy(locator = "xpath=(//input[@type='text'])[3]")
    public Edit editPostCode;
    
    @Alias("Submit")
    @FindBy(locator = "//button[text() = 'Add Customer']")
    public Control buttonSubmit;
    
    @Override
    public Page navigate() throws Exception {
        return this.buttonAddCustomer.click(this.getClass());
    }
}
