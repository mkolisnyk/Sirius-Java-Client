package com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking;

import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.Alias;
import com.github.mkolisnyk.sirius.client.ui.FindBy;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.SubItem;
import com.github.mkolisnyk.sirius.client.ui.controls.TableView;

@Alias("Customers")
public class CustomersPage extends BankManagerCommonPage {

    public CustomersPage(WebDriver driverValue) {
        super(driverValue);
    }

    @Alias("Customer List")
    @FindBy(locator = "//table", itemLocator = "/tbody/tr")
    @SubItem(name = "First Name", locator = "/td[1]")
    @SubItem(name = "Last Name", locator = "/td[2]")
    @SubItem(name = "Post Code", locator = "/td[3]")
    @SubItem(name = "Account Number", locator = "/td[4]")
    @SubItem(name = "Delete Customer", locator = "/td[5]/button")
    public TableView tableCustomers;

    @Override
    public Page navigate() throws Exception {
        return this.buttonCustomers.click(this.getClass());
    }
}
