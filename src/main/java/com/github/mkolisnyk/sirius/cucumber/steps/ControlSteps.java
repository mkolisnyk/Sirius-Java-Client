package com.github.mkolisnyk.sirius.cucumber.steps;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.github.mkolisnyk.sirius.client.Context;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import com.github.mkolisnyk.sirius.client.ui.controls.Edit;
import com.udojava.evalex.Expression;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ControlSteps {
    @When("^(?:I |)(?:click|tap) on the \"(.*)\" (?:button|element|control)$")
    public void clickOnTheButton(String name) throws Exception {
        Control control = Page.getCurrent().onPage(name);
        Assert.assertNotNull("Unable to find the '" + name + "' element on current page.", control);
        control.click();
    }
    @Then("^(?:I should see |)the \"(.*)\" field is available$")
    public Control verifyElementExists(String fieldName) throws Exception {
        Control control = Page.getCurrent().onPage(fieldName);
        Assert.assertNotNull("Unable to find the '" + fieldName + "' element on current page.", control);
        return control;
    }
    @When("^(?:I |)enter \"(.*)\" text into the \"(.*)\" field$")
    public void enterValue(String text, String fieldName) throws Exception {
        Edit control = (Edit) verifyElementExists(fieldName);
        control.setText(text);
    }
    @Then("^(?:I should see |)the \"(.*)\" field contains the \"(.*)\" text$")
    public void verifyFieldText(String fieldName, String text) throws Exception {
        Control control = (Control) verifyElementExists(fieldName);
        String actualText = control.getText();
        Assert.assertTrue(
            String.format("The '%s' field has unexpected text. Expected: '%s', Actual: '%s'",
                fieldName,
                text,
                actualText
            ),
            text.equals(actualText) || actualText.contains(text));
    }
    @Then("^(?:I should see |)the following fields are shown:$")
    public void verifyMultipleFieldsAvailability(List<String> elements) throws Exception {
        for (String element : elements) {
            verifyElementExists(element);
        }
    }
    @Then("^(?:I should see |)the (?:elements|buttons|controls) with the following visibility:$")
    public void verifyElementsWithVisibility(DataTable criteria)
            throws Throwable {

        List<Map<String, String>> content = criteria.asMaps(String.class,
                String.class);
        for (Map<String, String> row : content) {

            String element = row.get("Element");
            String shown = row.get("Shown");
            Control control = Page.getCurrent().onPage(element);
            Assert.assertNotNull("Element with the '" + element + "' alias wasn't declared on current page",
                    control);
            if (shown.equals("Y")) {
                Assert.assertTrue(String.format("Element \"%s\" isn't visible", element),
                        control.visible());
            } else {
                Assert.assertTrue(String.format("Element \"%s\" is unexpectly visible", element),
                        control.invisible());
            }
        }
    }
    @Then("^(?:I should see |)at least one of the following elements is shown:$")
    public void atLeastOneElementIsShown(List<String> elements)
            throws Exception {
        Control[] controls = new Control[elements.size()];
        for (int i = 0; i < controls.length; i++) {
            controls[i] = Page.getCurrent().onPage(elements.get(i));
        }
        Control control = Page.getFirstAvailableControlFromList(controls, (int) Page.getTimeout());
        Assert.assertNotNull("None of the expected elements list was found",
                control);
    }
    @When("^(?:I |)note the \"(.*)\" field text as \"(.*)\"")
    public void noteControlTextAs(String list, String varName) throws Exception {
        Control control = verifyElementExists(list);
        Context.put(varName, control.getText());
    }
    @When("^(?:I |)note following fields values:$")
    public void noteTheFollowingFields(DataTable criteria) throws Exception {
        List<Map<String, String>> content = criteria.asMaps(String.class,
                String.class);
        for (Map<String, String> row : content) {
            String field = row.get("Field");
            String as = row.get("As");
            String value = Page.getCurrent().onPage(field).getText();
            Context.put(as, value);
        }
    }
    @Then("^(?:I should see |)the \"(.*?)\" field value is calculated using the following formula:$")
    public void fieldValueIsCalculatedByFormula(
            String field, String formula) throws Throwable {
        final double precision = 0.0099;
        final int precisionNumbers = 6;
        double pageVal = Double.parseDouble(Page.getCurrent().onPage(field)
                .getText());
        for (String key : Context.variables()) {
            formula = formula.replaceAll(key, Context.get(key).toString());
        }
        Expression expression = new Expression(formula);
        double calcVal = expression
            .setRoundingMode(RoundingMode.HALF_EVEN).setPrecision(precisionNumbers).eval().doubleValue();

        Assert.assertEquals("Wrong " + field + "! on page (" + pageVal
                + ") vs calulated (" + calcVal + ")", pageVal, calcVal, precision);
    }
}
