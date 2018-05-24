package com.github.mkolisnyk.sirius.cucumber.steps;

import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.click;

import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

import com.github.mkolisnyk.sirius.client.Context;
import com.github.mkolisnyk.sirius.client.ui.Alias;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import com.github.mkolisnyk.sirius.client.ui.controls.Editable;
import com.github.mkolisnyk.sirius.client.ui.predicates.Operation;
import com.github.mkolisnyk.sirius.client.ui.predicates.States;
import com.udojava.evalex.Expression;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Group of Cucumber-JVM keywords which are targeted to separate controls.
 * @author Mykola Kolisnyk
 */
public class ControlSteps {
    /**
     * Performs the click on the element specified by name on current page.
     * Element name is defined using Alias.
     * @param name the element name to click on.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @When("^(?:I |)(?:click|tap) on the \"(.*)\" (?:button|element|control)$")
    public void clickOnTheButton(String name) throws Exception {
        Control control = Page.getCurrent().field(name);
        Assert.assertNotNull("Unable to find the '" + name + "' element on current page.", control);
        control.perform(click());
    }
    /**
     * Verifies if element specified by name exists on current page.
     * @param fieldName the name of the field to check existence of.
     * @return the control object (if it exists).
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @Then("^(?:I should see |)the \"(.*)\" field is available$")
    public Control verifyElementExists(String fieldName) throws Exception {
        Control control = Page.getCurrent().field(fieldName);
        Assert.assertNotNull("Unable to find the '" + fieldName + "' element on current page.", control);
        return control;
    }
    /**
     * Enters the text into the text field specified by name.
     * @param text text to enter.
     * @param fieldName the name of the field.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @When("^(?:I |)enter \"(.*)\" text into the \"(.*)\" field$")
    public void enterValue(String text, String fieldName) throws Exception {
        Editable control = (Editable) verifyElementExists(fieldName);
        control.setValue(text);
    }
    /**
     * Verifies that the field specified by the name contains some specific text.
     * @param fieldName the name of the field to check the text of.
     * @param text the expected part of the text.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @Then("^(?:I should see |)the \"(.*)\" field contains the \"(.*)\" text$")
    public void verifyFieldText(String fieldName, String text) throws Exception {
        Control control = verifyElementExists(fieldName);
        String actualText = control.getValue();
        Assert.assertTrue(
            String.format("The '%s' field has unexpected text. Expected: '%s', Actual: '%s'",
                fieldName,
                text,
                actualText
            ),
            text.equals(actualText) || actualText.contains(text));
    }
    /**
     * Verifies that all elements listed in parameters table are available on the page.
     * @param elements the list of elements to check.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @Then("^(?:I should see |)the following fields are shown:$")
    public void verifyMultipleFieldsAvailability(List<String> elements) throws Exception {
        for (String element : elements) {
            verifyElementExists(element);
        }
    }
    private Method getVerifyPredicate(String name) {
        Method[] methods = States.class.getDeclaredMethods();
        for (Method method : methods) {
            Alias alias = method.getAnnotation(Alias.class);
            if (alias != null && alias.value().equalsIgnoreCase(name)) {
                return method;
            }
        }
        return null;
    }
    /**
     * Checks multiple state of multiple elements.
     * @param criteria the data table showing multiple fields criteria.
     * @throws Throwable either reflection problems (like access) or missing attributes.
     */
    @Then("^(?:I should see |)the (?:elements|buttons|controls) with the following properties:$")
    public void verifyElementsWithVisibility(DataTable criteria)
            throws Throwable {

        List<Map<String, String>> content = criteria.asMaps(String.class,
                String.class);
        for (Map<String, String> row : content) {
            Set<String> properties = row.keySet();
            String element = row.get("Element");
            Control control = Page.getCurrent().field(element);
            for (String property : properties) {
                if (property.trim().equalsIgnoreCase("Element")) {
                    continue;
                }
                Method predicate = this.getVerifyPredicate(property);
                Assert.assertNotNull(String.format("Unable to find suitable operation for '%s'", property),
                        predicate);
                String value = row.get(property);
                if (predicate.getParameterCount() > 0 && StringUtils.isNotBlank(value)) {
                    control.verify((Operation<Boolean, Control>) predicate.invoke(null, value));
                } else {
                    if (StringUtils.isNotBlank(value) && !value.trim().equalsIgnoreCase("-")) {
                        Operation<Boolean, Control> operation = (Operation<Boolean, Control>) predicate.invoke(null);
                        boolean expectedValue = value.equalsIgnoreCase("Y");
                        Assert.assertEquals("Unable to verify that " + operation.description(control),
                                expectedValue,
                                control.is(operation));
                    }
                }
            }
        }
    }
    /**
     * Checks whether at least one of the elements is available.
     * @param elements the list of elements to check availability for.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @Then("^(?:I should see |)at least one of the following elements is shown:$")
    public void atLeastOneElementIsShown(List<String> elements)
            throws Exception {
        Control[] controls = new Control[elements.size()];
        for (int i = 0; i < controls.length; i++) {
            controls[i] = Page.getCurrent().field(elements.get(i));
        }
        Control control = Page.getFirstAvailableControlFromList(controls, (int) Page.getTimeout());
        Assert.assertNotNull("None of the expected elements list was found",
                control);
    }
    /**
     * Stores the field value in the context variable.
     * @param field the name of the field to get value from.
     * @param varName variable name to store field value to.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @When("^(?:I |)note the \"(.*)\" field text as \"(.*)\"")
    public void noteControlTextAs(String field, String varName) throws Exception {
        Control control = verifyElementExists(field);
        Context.put(varName, control.getText());
    }
    /**
     * Stores multiple fields in multiple variables.
     * @param criteria the data table containing field names and variable names.
     * @throws Exception either reflection problems (like access) or missing attributes.
     */
    @When("^(?:I |)note following fields values:$")
    public void noteTheFollowingFields(DataTable criteria) throws Exception {
        List<Map<String, String>> content = criteria.asMaps(String.class,
                String.class);
        for (Map<String, String> row : content) {
            String field = row.get("Field");
            String as = row.get("As");
            noteControlTextAs(field, as);
        }
    }
    /**
     * Calculates formula and compares value with the value of specific field.
     * Formula is calculated in numeric way with some precision. The formula may contain
     * either field names or context variables or constant numeric values combined
     * with various arithmetic operations.
     * @param field the name of the field to compare data with.
     * @param formula the formula to calculate expected value.
     * @throws Throwable either reflection problems (like access) or missing attributes.
     */
    @Then("^(?:I should see |)the \"(.*?)\" field value is calculated using the following formula:$")
    public void fieldValueIsCalculatedByFormula(
            String field, String formula) throws Throwable {
        final double precision = 0.0099;
        final int precisionNumbers = 6;
        double pageVal = Double.parseDouble(Page.getCurrent().field(field)
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
