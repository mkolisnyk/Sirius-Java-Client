package com.github.mkolisnyk.sirius.client.ui.controls;

import org.openqa.selenium.By;

import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.SubItem;

/**
 * Major class corresponding to compound elements processing. In addition to
 * base operations inherited from {@link Control} class, this class also contains
 * functionality to operate with sub-items and rows.
 * @author Mykola Kolisnyk
 *
 */
public class TableView extends Control {

    /**
     * Default constructor which binds page the control belongs to and the locator to
     * find the element on page.
     * @param parentValue the instance of the page class containing current control.
     * @param locatorValue the string used to locate current control.
     */
    public TableView(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }

    protected String getFullItemLocator() {
        return String.format("%s%s", this.getLocatorText(), this.getItemLocatorText());
    }

    /**
     * Calculates and returns the available items count. For tables it usually corresponds
     * to the number of rows.
     * @return available items count.
     */
    public int getItemsCount() {
        return this.getDriver().findElements(By.xpath(getFullItemLocator())).size();
    }

    /**
     * Generates and returns control object which corresponds to the compound object
     * item specified by index.
     * @param index the item order number.
     * @return control object corresponding to item.
     */
    public Control getItem(int index) {
        String locator = String.format("(%s)[%d]", this.getFullItemLocator(), index + 1);
        return new Control(this.getParent(), By.xpath(locator));
    }

    /**
     * Checks if current compound element contains any items.
     * @param timeout time limit to wait.
     * @return true - element contains items, false - otherwise.
     */
    public boolean isNotEmpty(long timeout) {
        return this.getItem(0).exists(timeout);
    }

    /**
     * Checks if current compound element contains any items. This is
     * overloaded version which waits for the default timeout.
     * @return true - element contains items, false - otherwise.
     */
    public boolean isNotEmpty() {
        return isNotEmpty(TIMEOUT);
    }
    /**
     * Checks if current compound element doesn't contains any items.
     * @param timeout time limit to wait.
     * @return true - element doesn't contain items, false - otherwise.
     */
    public boolean isEmpty(long timeout) {
        return this.getItem(0).disappears(timeout);
    }

    /**
     * Checks if current compound element doesn't contains any items. This is
     * overloaded version which waits for the default timeout.
     * @return true - element doesn't contain items, false - otherwise.
     */
    public boolean isEmpty() {
        return isEmpty(TIMEOUT);
    }

    /**
     * Provides fully qualified locator for sub-item for specified index and name.
     * @param name the sub-item name.
     * @param index the item index.
     * @return sub-item locator.
     */
    public By getSubItemLocator(String name, int index) {
        SubItem item = this.getSubItemsMap().get(name);
        String locator = String.format("(%s)[%d]%s", this.getFullItemLocator(), index + 1, item.locator());
        return By.xpath(locator);
    }

    /**
     * Common method which generates control object for specific sub-item of specific type.
     * @param name the sub-item name.
     * @param index the item index.
     * @param itemType the control type to cast sub-item instance to.
     * @param <T> the control class to cast sub-item control to.
     * @return control object for specific sub-item.
     * @throws Exception class cast exceptions.
     */
    public <T extends Control> T getSubItem(String name, int index, Class<T> itemType) throws Exception {
        T element = itemType.getConstructor(Page.class, By.class).newInstance(this.getParent(),
                getSubItemLocator(name, index));
        return element;
    }

    /**
     * Overloaded {@link TableView#getSubItem(String, int, Class)} which retrieves returning
     * control type from the sub-item data.
     * @param name sub-item name.
     * @param index item index.
     * @return control object for specific sub-item.
     * @throws Exception class cast exceptions.
     */
    public Control getSubItem(String name, int index) throws Exception {
        SubItem item = this.getSubItemsMap().get(name);
        return getSubItem(name, index, item.controlType());
    }
}
