package com.github.mkolisnyk.sirius.client.bdd.samples.steps;

import com.github.mkolisnyk.sirius.client.Driver;
import com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking.HomePage;
import com.github.mkolisnyk.sirius.client.ui.Page;

import cucumber.api.java.en.Given;

public class BankingSteps {

    public BankingSteps() {
        // TODO Auto-generated constructor stub
    }

    @Given("^the banking application has been started$")
    public void startBankingApplication() {
        Driver.current().get("http://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");
    }
    @Given("^I am logged as the \"(.*)\" customer$")
    public void loginAsCustomer(String name) throws Exception {
        ((HomePage) Page.forName("Banking Home")).loginAsCustomer(name);
    }
}
