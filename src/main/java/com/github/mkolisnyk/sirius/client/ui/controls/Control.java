package com.github.mkolisnyk.sirius.client.ui.controls;

import java.awt.Rectangle;
import java.util.HashMap;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.mkolisnyk.sirius.client.Configuration;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.PageFactory;
import com.github.mkolisnyk.sirius.client.ui.ScrollTo;
import com.github.mkolisnyk.sirius.client.ui.SubItem;

import io.appium.java_client.MobileElement;

/**
 * 
 * @author Mykola Kolisnyk
 *
 */
public class Control {
    protected static final long TIMEOUT = Configuration.timeout();
    private Page parent;
    private By locator;
    private String locatorText = "";
    private String itemLocatorText = "";
    private HashMap<String, SubItem> subItemsMap;
    private String scrollTo;
    private ScrollTo scrollDirection;
    private String format;
    private boolean excludeFromSearch = false;

    /**
     * 
     * @param parentValue
     * @param locatorValue
     */
    public Control(Page parentValue, By locatorValue) {
        this.parent = parentValue;
        this.locator = locatorValue;
        this.locatorText = this.locator.toString().replaceFirst("^By\\.(\\S+): ", "");
        subItemsMap = new HashMap<String, SubItem>();
    }

    /**
     * 
     * @return
     */
    public WebDriver getDriver() {
        return parent.getDriver();
    }

    /**
     * 
     * @return
     */
    public Page getParent() {
        return parent;
    }

    /**
     * 
     * @return
     */
    public By getLocator() {
        return locator;
    }

    /**
     * 
     * @return
     */
    public String getLocatorText() {
        return locatorText;
    }

    /**
     * 
     * @return
     */
    public String getItemLocatorText() {
        return itemLocatorText;
    }

    /**
     * 
     * @param subItemLocatorText
     */
    public void setItemLocatorText(String subItemLocatorText) {
        this.itemLocatorText = subItemLocatorText;
    }

    /**
     * 
     * @return
     */
    public String getScrollTo() {
        return scrollTo;
    }

    /**
     * 
     * @param scrollToValue
     */
    public void setScrollTo(String scrollToValue) {
        this.scrollTo = scrollToValue;
    }

    /**
     * 
     * @return
     */
    public ScrollTo getScrollDirection() {
        return scrollDirection;
    }

    /**
     * 
     * @param scrollDirectionValue
     */
    public void setScrollDirection(ScrollTo scrollDirectionValue) {
        this.scrollDirection = scrollDirectionValue;
    }

    /**
     * 
     * @return
     */
    public String getFormat() {
        return format;
    }

    /**
     * 
     * @param formatValue
     */
    public void setFormat(String formatValue) {
        this.format = formatValue;
    }

    /**
     * 
     * @return
     */
    public boolean isExcludeFromSearch() {
        return excludeFromSearch;
    }

    /**
     * 
     * @param excludeFromSearchValue
     */
    public void setExcludeFromSearch(boolean excludeFromSearchValue) {
        this.excludeFromSearch = excludeFromSearchValue;
    }

    /**
     * 
     * @param items
     */
    public void addSubItems(SubItem[] items) {
        for (SubItem item : items) {
            this.subItemsMap.put(item.name(), item);
        }
    }

    /**
     * 
     * @return
     */
    public HashMap<String, SubItem> getSubItemsMap() {
        return subItemsMap;
    }

    /**
     * 
     * @return
     */
    public WebElement element() {
        return getDriver().findElement(locator);
    }

    /**
     * 
     * @param index
     * @return
     */
    public WebElement element(int index) {
        return getDriver().findElements(locator).get(index);
    }

    /**
     * 
     * @param condition
     * @param timeout
     * @return
     */
    public boolean waitUntil(ExpectedCondition<?> condition, long timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), timeout);
        try {
            wait.until(condition);
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @param timeout
     * @return
     */
    public boolean exists(long timeout) {
        this.scrollTo();
        return waitUntil(ExpectedConditions.presenceOfElementLocated(locator), timeout);
    }

    /**
     * 
     * @return
     */
    public boolean exists() {
        return exists(TIMEOUT);
    }

    /**
     * 
     * @param timeout
     * @return
     */
    public boolean disappears(long timeout) {
        return waitUntil(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(locator)), timeout);
    }

    /**
     * 
     * @return
     */
    public boolean disappears() {
        return disappears(TIMEOUT);
    }

    /**
     * 
     * @param timeout
     * @return
     */
    public boolean visible(long timeout) {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(locator), timeout);
    }

    /**
     * 
     * @return
     */
    public boolean visible() {
        Assert.assertTrue("Unable to find element: " + this.locator.toString(), exists());
        return visible(TIMEOUT);
    }

    /**
     * 
     * @param timeout
     * @return
     */
    public boolean invisible(long timeout) {
        return waitUntil(ExpectedConditions.invisibilityOfElementLocated(locator), timeout);
    }

    /**
     * 
     * @return
     */
    public boolean invisible() {
        Assert.assertTrue("Unable to find element: " + this.locator.toString(), exists());
        return invisible(TIMEOUT);
    }

    /**
     * 
     * @param timeout
     * @return
     */
    public boolean enabled(long timeout) {
        return waitUntil(ExpectedConditions.elementToBeClickable(locator), timeout);
    }

    /**
     * 
     * @return
     */
    public boolean enabled() {
        return enabled(TIMEOUT);
    }

    /**
     * 
     * @param timeout
     * @return
     */
    public boolean disabled(long timeout) {
        return waitUntil(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(locator)), timeout);
    }

    /**
     * 
     * @return
     */
    public boolean disabled() {
        return enabled(TIMEOUT);
    }

    /**
     * 
     */
    public void click() {
        Assert.assertTrue("Unable to find element: " + this.locator.toString(), exists());
        this.element().click();
    }

    /**
     * 
     * @param pageClass
     * @return
     * @throws Exception
     */
    public <T extends Page> T click(Class<T> pageClass) throws Exception {
        this.click();
        T page = PageFactory.init(this.getDriver(), pageClass);
        Assert.assertTrue(
                String.format("The page of %s class didn't appear during specified timeout", pageClass.getName()),
                page.isCurrent());
        return page;
    }

    /**
     * 
     * @return
     */
    public String getText() {
        Assert.assertTrue("Unable to find element with locator: " + this.getLocator(), this.exists());
        return this.element().getText();
    }

    /**
     * 
     * @return
     */
    public String getValue() {
        return this.getText();
    }

    /**
     * 
     * @return
     */
    public Rectangle getRect() {
        this.exists();
        Rectangle rect = new Rectangle();
        Point location = ((MobileElement) this.element()).getCoordinates().onPage();
        Dimension size = this.element().getSize();
        rect.x = location.x;
        rect.y = location.y;
        rect.width = size.width;
        rect.height = size.height;
        return rect;
    }

    /**
     * 
     */
    public void scrollTo() {
        if (this.getScrollTo() != null && !this.getScrollTo().trim().equals("")) {
            this.getParent().scrollTo(this.getScrollTo(), this.getScrollDirection());
        }
    }
}
