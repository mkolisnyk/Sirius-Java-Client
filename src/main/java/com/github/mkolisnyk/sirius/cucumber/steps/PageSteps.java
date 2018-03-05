package com.github.mkolisnyk.sirius.cucumber.steps;

import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.github.mkolisnyk.sirius.client.Driver;
import com.github.mkolisnyk.sirius.client.ui.Page;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PageSteps {

    @Given("^I am on the \"(.*)\" (?:page|screen)$")
    @When("^(?:I |)go to the \"(.*)\" (?:page|screen)$")
    public void navigateToPage(String name) throws Exception {
        Page target = Page.forName(name);
        Assert.assertNotNull("Unable to find the '" + name + "' page.", target);
        target.navigate();
        verifyCurrentPage(name);
    }
    @Then("^I should see the \"(.*)\" (?:page|screen)$")
    public void verifyCurrentPage(String name) throws Exception {
        Page target = Page.forName(name);
        Assert.assertTrue("The '" + name + "' screen is not current", target.isCurrent());
        Page.setCurrent(target);
    }
    @When("^(?:I |)accept the alert message$")
    public void acceptAlert() {
        Driver.current().switchTo().alert().accept();
    }
    @Then("^(?:I should see |)the \"(.*)\" (?:text|label) is shown$")
    public void verifyTextPresent(String text) throws Exception {
        Assert.assertTrue("Unable to find text: '" + text + "'", Page.getCurrent().isTextPresent(text));
    }
    @Then("^(?:I should see |)the following labels are shown:$")
    public void verifyMultipleLabelsAvailability(List<String> elements) throws Exception {
        for (String element : elements) {
            verifyTextPresent(element);
        }
    }
    @When("^(?:I |)populate current page with the following data:$")
    public void populatePageWithData(DataTable data) throws Throwable {
        List<Map<String, String>> content = data.asMaps(String.class,
                String.class);
        for (Map<String, String> row : content) {
            ControlSteps steps = new ControlSteps();
            steps.enterValue(row.get("Value"), row.get("Field"));
        }
    }
    @Then("^(?:I should see |)the page contains the following data:$")
    public void pageContainsData(DataTable data) throws Throwable {
        List<Map<String, String>> content = data.asMaps(String.class,
                String.class);
        for (Map<String, String> row : content) {
            ControlSteps steps = new ControlSteps();
            steps.verifyFieldText(row.get("Field"), row.get("Value"));
        }
    }
}
