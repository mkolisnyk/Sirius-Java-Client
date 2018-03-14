package com.github.mkolisnyk.sirius.cucumber.steps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;

import com.github.mkolisnyk.sirius.client.Context;
import com.github.mkolisnyk.sirius.client.ui.controls.TableView;
import com.udojava.evalex.Expression;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Groups steps interacting with compound elements (like tables) and their
 * sub-elements.
 * @author Mykola Kolisnyk
 */
public class TableSteps {
    private ControlSteps steps = new ControlSteps();
    /**
     * Verifies whether table is empty or not.
     * @param list the name of the table to check.
     * @param emptyState the fragment flagging whether we expect empty or non-empty state.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @Then("^(?:I should see |)the \"(.*)\" (?:list|table) is (|not )empty$")
    public void verifyListEmptyState(String list, String emptyState) throws Exception {
        boolean empty = emptyState.trim().equals("");
        TableView control = (TableView) steps.verifyElementExists(list);
        if (empty) {
            Assert.assertTrue("The '" + list + "' element is not empty", control.isEmpty());
        } else {
            Assert.assertTrue("The '" + list + "' element is empty", control.isNotEmpty());
        }
    }
    /**
     * Checks the values of first or last row of the table.
     * @param firstLast switch indicating whether we check first or last item.
     * @param list the name of the table element.
     * @param data the data table containing pairs of sub-item name and expected value.
     * @throws Throwable either reflection problems (like access) or missing attributes.
     */
    @Then("^(?:I should see |)the (first|last) (?:row|item) of "
            + "the \"(.*)\" (?:list|table) contains the following data:$")
    public void verifyListRowData(String firstLast, String list, DataTable data) throws Throwable {
        int index = 0;
        TableView control = (TableView) steps.verifyElementExists(list);
        if (firstLast.equals("last")) {
            index = control.getItemsCount() - 1;
        }
        List<Map<String, String>> content = data.asMaps(String.class,
                String.class);
        for (Map<String, String> row : content) {
            for (Entry<String, String> entry : row.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Assert.assertEquals(String.format("The %s row element '%s' has unexpected value", firstLast, key),
                        value, control.getSubItem(key, index).getText());
            }
        }
    }
    /**
     * Clicks on specific sub-item at the first/last row of the table specified by name.
     * @param firstLast switch indicating whether we check first or last item.
     * @param item the sub-item name.
     * @param list the name of the table element.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @When("^(?:I |)(?:click|tap) on the (first|last) \"(.*)\" element of the \"(.*)\" (?:list|table)$")
    public void clickOnSubItem(String firstLast, String item, String list) throws Exception {
        int index = 0;
        TableView control = (TableView) steps.verifyElementExists(list);
        if (firstLast.equals("last")) {
            index = control.getItemsCount() - 1;
        }
        control.getSubItem(item, index).click();
    }
    /**
     * Stores the table row count in the context variable.
     * @param list the name of the data table.
     * @param varName the name of context variable to store value in.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @When("^(?:I |)note the \"(.*)\" (?:table|list) (?:row|item) count as \"(.*)\"")
    public void noteRowCountAs(String list, String varName) throws Exception {
        TableView control = (TableView) steps.verifyElementExists(list);
        Context.put(varName, control.getItemsCount());
    }
    /**
     * Compares expected and actual row counts for the table specified by name.
     * @param list the name of the table.
     * @param countValue expected count.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @Then("^(?:I should see |)the \"(.*)\" (?:table|list) has \"(.*)\" (?:items|rows)$")
    public void verifyTableRowCount(String list, String countValue) throws Exception {
        TableView control = (TableView) steps.verifyElementExists(list);
        BigDecimal actualCount = new BigDecimal(control.getItemsCount());
        String expectedCountValue = countValue;
        for (String key : Context.variables()) {
            expectedCountValue = expectedCountValue.replaceAll(key, Context.get(key).toString());
        }
        Expression expression = new Expression(expectedCountValue);
        BigDecimal expectedCount = expression.setPrecision(0).eval();
        Assert.assertEquals("Unexpected row count for the '" + list + "' table", expectedCount, actualCount);
    }
}
