package com.github.mkolisnyk.sirius.client.bdd.samples.tests;

import static com.github.mkolisnyk.sirius.client.ui.predicates.States.*;
import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.*;
import static com.github.mkolisnyk.sirius.client.ui.predicates.Getters.*;
import static com.github.mkolisnyk.sirius.client.ui.predicates.Setters.*;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.mkolisnyk.sirius.client.Configuration;
import com.github.mkolisnyk.sirius.client.Context;
import com.github.mkolisnyk.sirius.client.Driver;
import com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking.CustomerDepositPage;
import com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking.HomePage;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.PageFactory;
import com.github.mkolisnyk.sirius.client.ui.controls.Edit;


public class PlainBankingTest {
    @Before
    public void beforeScenario() throws Exception {
        Context.clearCurrent();
        Configuration.load("src/test/resources/config.properties");
        Configuration.print();
        System.setProperty("webdriver.gecko.driver", new File("drivers/geckodriver").getAbsolutePath());
        System.setProperty("webdriver.chrome.driver", new File("drivers/chromedriver").getAbsolutePath());
        Assert.assertTrue("Only web platforms are supported by this test", Configuration.platform().isWeb());
        DesiredCapabilities cap = new DesiredCapabilities();
        Driver.init("", Configuration.platform(), cap);
        Page.setTimeout(Configuration.timeout());
        Page.setDefaultPagesPackage(Configuration.pagesPackage());
    }
    @After
    public void afterScenario() throws Exception {
        Driver.current().quit();
    }
    @Test
    public void testSampleBankingImperative() throws Exception {
        HomePage home = PageFactory.init(Driver.current(), HomePage.class);
        home.navigate();
        CustomerDepositPage depositPage = home.loginAsCustomer("Harry Potter")
                .buttonDeposit
                .perform(click())
                .perform(waitFor(CustomerDepositPage.class));
        depositPage.navigate();
        depositPage.editDepositAmount.setValue("100");
        depositPage.buttonSubmitDeposit
            .perform(click())
            .perform(waitFor(CustomerDepositPage.class));
        Assert.assertTrue(depositPage.is(textPresent("Deposit Successful")));
        Assert.assertEquals("100", depositPage.labelBalance.getText());
    }
    @Test
    public void testSampleBankingUsingNames() throws Exception {
        HomePage home = (HomePage) Page.forName("Banking Home").navigate();
        CustomerDepositPage depositPage = home.loginAsCustomer("Harry Potter")
                .field("Deposit")
                .perform(click())
                .perform(waitFor(CustomerDepositPage.class))
                .navigate()
                .field("Deposit Amount", Edit.class).setValue("100")
                .getParent().field("Submit Deposit")
                .perform(click())
                .perform(waitFor(CustomerDepositPage.class));
        Assert.assertTrue(depositPage.is(textPresent("Deposit Successful")));
        Assert.assertEquals("100", depositPage.labelBalance.getText());
    }
    @Test
    public void testSampleBankingUsingPredicates() throws Exception {
        HomePage home = PageFactory.init(Driver.current(), HomePage.class);
        home.navigate()
            .verify(current()).get(source());
        CustomerDepositPage depositPage = home.loginAsCustomer("Harry Potter")
                .buttonDeposit
                .verify(exists(1))
                .perform(click())
                .perform(waitFor(CustomerDepositPage.class));
        depositPage.navigate()
            .verify(current());
        depositPage.editDepositAmount.setValue("100")
            .verify(hasText("100"), valueIs("100"));
        depositPage.buttonSubmitDeposit
            .verify(exists(1))
            .perform(click())
            .perform(waitFor(CustomerDepositPage.class));
        depositPage
            .verify(current())
            .verify(textPresent("Deposit Successful"));
        depositPage.labelBalance.verify(hasText("100"));
    }
    @Test
    public void testSampleBankingUsingNamesAndPredicates() throws Exception {
        ((HomePage) Page.forName("Banking Home")
            .navigate()
            .verify(
                    current(),
                    textPresent("Customer Login"),
                    textPresent("Bank Manager Login")))
            .loginAsCustomer("Harry Potter")
            .field("Deposit")
                .verify(
                        exists(5),
                        visible(),
                        enabled())
                .perform(click())
                .perform(waitFor(CustomerDepositPage.class))
            .navigate()
                .verify(current())
            .field("Deposit Amount", Edit.class)
                .perform(clear())
                .set(value("100"))
                .verify(hasText("100"))
            .get(parent()).field("Submit Deposit")
                .verify(exists(1))
                .perform(click())
                .perform(waitFor(CustomerDepositPage.class))
            .verify(textPresent("Deposit Successful"))
            .field("Balance")
                .verify(hasText("100"));
    }
}
