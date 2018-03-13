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
 * 
 * @author Mykola Kolisnyk
 */
public class TableSteps {
    private ControlSteps steps = new ControlSteps();
    /**
     * 
     * @param list
     * @param emptyState
     * @throws Throwable
     */
    @Then("^(?:I should see |)the \"(.*)\" (?:list|table) is (|not )empty$")
    public void verifyListEmptyState(String list, String emptyState) throws Throwable {
        boolean empty = emptyState.trim().equals("");
        TableView control = (TableView) steps.verifyElementExists(list);
        if (empty) {
            Assert.assertTrue("The '" + list + "' element is not empty", control.isEmpty());
        } else {
            Assert.assertTrue("The '" + list + "' element is empty", control.isNotEmpty());
        }
    }
    /**
     * 
     * @param firstLast
     * @param list
     * @param data
     * @throws Throwable
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
     * 
     * @param firstLast
     * @param item
     * @param list
     * @throws Exception
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
     * 
     * @param list
     * @param varName
     * @throws Exception
     */
    @When("^(?:I |)note the \"(.*)\" (?:table|list) (?:row|item) count as \"(.*)\"")
    public void noteRowCountAs(String list, String varName) throws Exception {
        TableView control = (TableView) steps.verifyElementExists(list);
        Context.put(varName, control.getItemsCount());
    }
    /**
     * 
     * @param list
     * @param countValue
     * @throws Exception
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
