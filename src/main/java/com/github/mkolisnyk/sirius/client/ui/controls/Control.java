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
 * Major class for all control objects. All other control classes should be extended from this class.
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
     * Default constructor which binds page the control belongs to and the locator to
     * find the element on page.
     * @param parentValue the instance of the page class containing current control.
     * @param locatorValue the string used to locate current control.
     */
    public Control(Page parentValue, By locatorValue) {
        this.parent = parentValue;
        this.locator = locatorValue;
        this.locatorText = this.locator.toString().replaceFirst("^By\\.(\\S+): ", "");
        subItemsMap = new HashMap<String, SubItem>();
    }

    /**
     * Gets the direct reference to the WebDriver object.
     * @return direct reference to the WebDriver object.
     */
    public WebDriver getDriver() {
        return parent.getDriver();
    }

    /**
     * Gets reference to the parent {@link Page} object.
     * @return parent page object instance.
     */
    public Page getParent() {
        return parent;
    }

    /**
     * Gets actual locator to be used for element location on page.
     * @return actual locator.
     */
    public By getLocator() {
        return locator;
    }

    /**
     * Gets the string representation of the locator.
     * @return string representation of the locator.
     */
    public String getLocatorText() {
        return locatorText;
    }

    /**
     * Gets string representation of item locator. It is used for compound elements
     * processing.
     * @return string representation of item locator.
     */
    public String getItemLocatorText() {
        return itemLocatorText;
    }

    /**
     * Sets item locator value.
     * @param subItemLocatorText item locator string value.
     */
    public void setItemLocatorText(String subItemLocatorText) {
        this.itemLocatorText = subItemLocatorText;
    }

    /**
     * Gets text to scroll to in case element isn't immediately available on page.
     * @return text to scroll to.
     */
    public String getScrollTo() {
        return scrollTo;
    }

    /**
     * Sets text to scroll to in case element isn't immediately available on page.
     * @param scrollToValue text to scroll to.
     */
    public void setScrollTo(String scrollToValue) {
        this.scrollTo = scrollToValue;
    }

    /**
     * Gets the direction of scrolling.
     * @return direction of scrolling.
     */
    public ScrollTo getScrollDirection() {
        return scrollDirection;
    }

    /**
     * Sets the direction of scrolling.
     * @param scrollDirectionValue direction of scrolling.
     * @see ScrollTo
     */
    public void setScrollDirection(ScrollTo scrollDirectionValue) {
        this.scrollDirection = scrollDirectionValue;
    }

    /**
     * Gets the string which can be used in custom controls when the data is to
     * be retrieved based on some specific format (e.g. date, duration).
     * @return format string.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the string which can be used in custom controls when the data is to
     * be retrieved based on some specific format (e.g. date, duration).
     * @param formatValue format string.
     */
    public void setFormat(String formatValue) {
        this.format = formatValue;
    }

    /**
     * Defines whether current control is supposed to be excluded from search while
     * using {@link Page#isCurrent(long)} method.
     * @return exclude from search flag.
     */
    public boolean isExcludeFromSearch() {
        return excludeFromSearch;
    }

    /**
     * Sets the state whether current control is supposed to be excluded from search while
     * using {@link Page#isCurrent(long)} method.
     * @param excludeFromSearchValue exclude from search flag.
     */
    public void setExcludeFromSearch(boolean excludeFromSearchValue) {
        this.excludeFromSearch = excludeFromSearchValue;
    }

    /**
     * Adds sub-items assigned using {@link SubItem} annotation.
     * @param items sub-items to add.
     */
    public void addSubItems(SubItem[] items) {
        for (SubItem item : items) {
            this.subItemsMap.put(item.name(), item);
        }
    }

    /**
     * Gets the map of sub-items associated with their name.
     * @return map of sub-items associated with their name.
     */
    public HashMap<String, SubItem> getSubItemsMap() {
        return subItemsMap;
    }

    /**
     * Gets direct reference to the WebElement instance for current control object.
     * @return direct reference to the WebElement instance.
     */
    public WebElement element() {
        return getDriver().findElement(locator);
    }

    /**
     * Gets direct reference to the WebElement instance for current control object.
     * Unlike {@link Control#element()}, this method is applicable when multiple elements
     * can match the same locator. In this case the index is used to define which element
     * from the array of possible options should be taken.
     * @param index element index.
     * @return direct reference to the WebElement instance.
     */
    public WebElement element(int index) {
        return getDriver().findElements(locator).get(index);
    }

    /**
     * Common method for various actions waiting for some element event to happen.
     * @param condition the expected condition predicate.
     * @param timeout the time limit to wait for event to happen.
     * @return true if condition is met, false - otherwise.
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
     * Checks if current control exists.
     * @param timeout time limit to wait
     * @return true - element present, false - otherwise.
     */
    public boolean exists(long timeout) {
        this.scrollTo();
        return waitUntil(ExpectedConditions.presenceOfElementLocated(locator), timeout);
    }

    /**
     * Overloaded {@link Control#exists(long)} method which waits for element to
     * appear during the default timeout.
     * @return true - element present, false - otherwise.
     */
    public boolean exists() {
        return exists(TIMEOUT);
    }

    /**
     * Checks if current control no longer exists.
     * @param timeout time limit to wait
     * @return true - element is missing, false - otherwise.
     */
    public boolean disappears(long timeout) {
        return waitUntil(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(locator)), timeout);
    }

    /**
     * Overloaded {@link Control#disappears(long)} method which waits for element to
     * disappear during the default timeout.
     * @return true - element is missing, false - otherwise.
     */
    public boolean disappears() {
        return disappears(TIMEOUT);
    }

    /**
     * Checks if current control is visible.
     * @param timeout time limit to wait
     * @return true - element is visible, false - otherwise.
     */
    public boolean visible(long timeout) {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(locator), timeout);
    }

    /**
     * Overloaded {@link Control#visible(long)} method which waits for element to
     * become visible during the default timeout.
     * @return true - element is visible, false - otherwise.
     */
    public boolean visible() {
        Assert.assertTrue("Unable to find element: " + this.locator.toString(), exists());
        return visible(TIMEOUT);
    }

    /**
     * Checks if current control is invisible.
     * @param timeout time limit to wait
     * @return true - element is invisible, false - otherwise.
     */
    public boolean invisible(long timeout) {
        return waitUntil(ExpectedConditions.invisibilityOfElementLocated(locator), timeout);
    }

    /**
     * Overloaded {@link Control#invisible(long)} method which waits for element to
     * become invisible during the default timeout.
     * @return true - element is invisible, false - otherwise.
     */
    public boolean invisible() {
        Assert.assertTrue("Unable to find element: " + this.locator.toString(), exists());
        return invisible(TIMEOUT);
    }

    /**
     * Checks if current control is enabled.
     * @param timeout time limit to wait
     * @return true - element is enabled, false - otherwise.
     */
    public boolean enabled(long timeout) {
        return waitUntil(ExpectedConditions.elementToBeClickable(locator), timeout);
    }

    /**
     * Overloaded {@link Control#enabled(long)} method which waits for element to
     * become enabled during the default timeout.
     * @return true - element is enabled, false - otherwise.
     */
    public boolean enabled() {
        return enabled(TIMEOUT);
    }

    /**
     * Checks if current control is disabled.
     * @param timeout time limit to wait
     * @return true - element is disabled, false - otherwise.
     */
    public boolean disabled(long timeout) {
        return waitUntil(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(locator)), timeout);
    }

    /**
     * Overloaded {@link Control#disabled(long)} method which waits for element to
     * become disabled during the default timeout.
     * @return true - element is disabled, false - otherwise.
     */
    public boolean disabled() {
        return enabled(TIMEOUT);
    }

    /**
     * Performs click on element.
     */
    public void click() {
        Assert.assertTrue("Unable to find element: " + this.locator.toString(), exists());
        this.element().click();
    }

    /**
     * Performs the click on element and returns the page object which corresponds to
     * the page which should appear after click operation.
     * @param pageClass the page class corresponding to the page to appear after click.
     * @param <T> the class of expected page object.
     * @return the initialized page object corresponding to the page which should appear
     * after the click on current element.
     * @throws Exception any exception when some attributes are missing.
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
     * Gets element text.
     * @return element text.
     */
    public String getText() {
        Assert.assertTrue("Unable to find element with locator: " + this.getLocator(), this.exists());
        return this.element().getText();
    }

    /**
     * Gets element value. It can be either value attribute or any other converted string
     * depending on each particular control implementation.
     * @return element value.
     */
    public String getValue() {
        return this.getText();
    }

    /**
     * Gets rectangular dimensions of current control.
     * @return current control dimantions.
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
     * <p>
     * Scrolls screen to current element.
     * </p>
     * <p>
     * <b>NOTE:</b> applicable for Android only.
     * </p>
     */
    public void scrollTo() {
        if (this.getScrollTo() != null && !this.getScrollTo().trim().equals("")) {
            this.getParent().scrollTo(this.getScrollTo(), this.getScrollDirection());
        }
    }
}
