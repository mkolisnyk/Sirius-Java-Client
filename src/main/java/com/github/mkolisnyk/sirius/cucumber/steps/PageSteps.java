package com.github.mkolisnyk.sirius.cucumber.steps;

import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.click;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.current;

import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.github.mkolisnyk.sirius.client.Driver;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Edit;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Groups functionality targeted to pages.
 * @author Mykola Kolisnyk
 */
public class PageSteps {

    /**
     * Navigates to some specific page. Page name is defined by page alias.
     * The navigation is performed by calling {@link Page#navigate()} method
     * for specific page.
     * @param name the name of the page to navigate to.
     * @throws Exception any exception related to data conversion or null value
     */
    @Given("^I am on the \"(.*)\" (?:page|screen)$")
    @When("^(?:I |)go to the \"(.*)\" (?:page|screen)$")
    public void navigateToPage(String name) throws Exception {
        Page target = Page.forName(name);
        Assert.assertNotNull("Unable to find the '" + name + "' page.", target);
        target.navigate();
        verifyCurrentPage(name);
    }
    /**
     * Verified that the page specified by the name is current. Actually, it is
     * wrapper of {@link Page#isCurrent()} method call.
     * @param name the page name.
     * @throws Exception any exception related to data conversion or null value.
     */
    @Then("^I should see the \"(.*)\" (?:page|screen)$")
    public void verifyCurrentPage(String name) throws Exception {
        Page target = Page.forName(name);
        Assert.assertTrue("The '" + name + "' screen is not current",
                target.is(current()));
        Page.setCurrent(target);
    }
    /**
     * Closes the alert message.
     */
    @When("^(?:I |)accept the alert message$")
    public void acceptAlert() {
        Driver.current().switchTo().alert().accept();
    }
    /**
     * Verifies whether some text is shown on screen.
     * @param text the text to check.
     */
    @Then("^(?:I should see |)the \"(.*)\" (?:text|label) is shown$")
    public void verifyTextPresent(String text) {
        Assert.assertTrue("Unable to find text: '" + text + "'",
                Page.getCurrent().isTextPresent(text));
    }
    /**
     * Verifies multiple labels presence. It will throw an assertion error
     * as soon as at least one of the labels wasn't found during default timeout.
     * @param elements the list of labels to check presence of.
     */
    @Then("^(?:I should see |)the following labels are shown:$")
    public void verifyMultipleLabelsAvailability(List<String> elements) {
        for (String element : elements) {
            verifyTextPresent(element);
        }
    }
    /**
     * Populates current page with the data provided. All specified fields should have
     * {@link Edit#setText(String)} method implemented.
     * @param data the table containing field to fill and value.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @When("^(?:I |)populate current page with the following data:$")
    public void populatePageWithData(DataTable data) throws Exception {
        List<Map<String, String>> content = data.asMaps(String.class,
                String.class);
        for (Map<String, String> row : content) {
            ControlSteps steps = new ControlSteps();
            steps.enterValue(row.get("Value"), row.get("Field"));
        }
    }
    /**
     * Verifies that page contains some data. Each field has expected value
     * defined which is compared to the actual field text.
     * @param data the data table containing fields and expected values.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @Then("^(?:I should see |)the page contains the following data:$")
    public void pageContainsData(DataTable data) throws Exception {
        List<Map<String, String>> content = data.asMaps(String.class,
                String.class);
        for (Map<String, String> row : content) {
            ControlSteps steps = new ControlSteps();
            steps.verifyFieldText(row.get("Field"), row.get("Value"));
        }
    }
    /**
     * Perform click on some specific label.
     * @param message the text of the label to click on.
     */
    @When("^(?:I |)click on the \"([^\"]*)\" (?:text|label)$")
    public void clickOnText(String message) {
        Page.getCurrent().getTextControl(message).perform(click());
    }

}
