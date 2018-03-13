package com.github.mkolisnyk.sirius.client.ui.controls;

import org.openqa.selenium.By;

import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.SubItem;

/**
 * 
 * @author Mykola Kolisnyk
 *
 */
public class TableView extends Control {

    /**
     * 
     * @param parentValue
     * @param locatorValue
     */
    public TableView(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }

    protected String getFullItemLocator() {
        return String.format("%s%s", this.getLocatorText(), this.getItemLocatorText());
    }

    /**
     * 
     * @return
     */
    public int getItemsCount() {
        return this.getDriver().findElements(By.xpath(getFullItemLocator())).size();
    }

    /**
     * 
     * @param index
     * @return
     */
    public Control getItem(int index) {
        String locator = String.format("(%s)[%d]", this.getFullItemLocator(), index + 1);
        return new Control(this.getParent(), By.xpath(locator));
    }

    /**
     * 
     * @param timeout
     * @return
     */
    public boolean isNotEmpty(long timeout) {
        return this.getItem(0).exists(timeout);
    }

    /**
     * 
     * @return
     */
    public boolean isNotEmpty() {
        return isNotEmpty(TIMEOUT);
    }
    /**
     * 
     * @param timeout
     * @return
     */
    public boolean isEmpty(long timeout) {
        return this.getItem(0).disappears(timeout);
    }

    /**
     * 
     * @return
     */
    public boolean isEmpty() {
        return isEmpty(TIMEOUT);
    }

    /**
     * 
     * @param name
     * @param index
     * @return
     */
    public By getSubItemLocator(String name, int index) {
        SubItem item = this.getSubItemsMap().get(name);
        String locator = String.format("(%s)[%d]%s", this.getFullItemLocator(), index + 1, item.locator());
        return By.xpath(locator);
    }

    /**
     * 
     * @param name
     * @param index
     * @param itemType
     * @return
     * @throws Exception
     */
    public <T extends Control> T getSubItem(String name, int index, Class<T> itemType) throws Exception {
        T element = itemType.getConstructor(Page.class, By.class).newInstance(this.getParent(),
                getSubItemLocator(name, index));
        return element;
    }

    /**
     * 
     * @param name
     * @param index
     * @return
     * @throws Exception
     */
    public Control getSubItem(String name, int index) throws Exception {
        SubItem item = this.getSubItemsMap().get(name);
        return getSubItem(name, index, item.controlType());
    }
}
