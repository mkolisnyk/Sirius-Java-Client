package com.github.mkolisnyk.sirius.client.ui;

import java.lang.reflect.Field;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.Configuration;
import com.github.mkolisnyk.sirius.client.Platform;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

/**
 * The global object which is responsible for proper {@link Page} instance
 * initialization.
 * @author Mykola Kolisnyk
 */
public final class PageFactory {

    private PageFactory() {
    }

    private static FindBy getLocatorForPlatform(FindBy[] locators, Platform platform) {
        for (FindBy locator : locators) {
            if (locator.platform().equals(platform)) {
                return locator;
            }
        }
        return null;
    }

    private static SubItem[] getSubItemsForPlatform(SubItem[] items, Platform platform) {
        SubItem[] result = new SubItem[] {};
        for (SubItem item : items) {
            if (item.platform().equals(platform) || item.platform().equals(Platform.ANY)) {
                result = ArrayUtils.add(result, item);
            }
        }
        return result;
    }

    /**
     * Major method which initialises page object instance based on WebDriver and page class
     * provided. It processes all page and control related annotations and initialises all
     * control fields.
     * @param driver the WebDriver instance to pass to new page object instance.
     * @param pageClass the page class which instance should be created.
     * @param <T> the class of returning instance.
     * @return initialised page class instance where all control fields are initialised and ready to use.
     * @throws Exception mainly related to missing attributes of the fields to process.
     */
    public static <T extends Page> T init(WebDriver driver, Class<T> pageClass) throws Exception {
        T page = pageClass.getConstructor(WebDriver.class).newInstance(driver);
        for (Field field : pageClass.getFields()) {
            FindBy[] locators = field.getAnnotationsByType(FindBy.class);
            if (locators != null && locators.length > 0) {
                FindBy locator = getLocatorForPlatform(locators, Configuration.platform());
                if (locator == null) {
                    locator = getLocatorForPlatform(locators, Platform.ANY);
                }
                if (locator != null) {
                    Control control = (Control) field.getType().getConstructor(Page.class, By.class).newInstance(page,
                            toLocator(locator.locator()));
                    control.setItemLocatorText(locator.itemLocator());
                    SubItem[] items = field.getAnnotationsByType(SubItem.class);
                    control.addSubItems(getSubItemsForPlatform(items, Configuration.platform()));
                    control.setScrollTo(locator.scrollTo());
                    control.setScrollDirection(locator.scrollDirection());
                    control.setFormat(locator.format());
                    control.setExcludeFromSearch(locator.excludeFromSearch());
                    field.set(page, control);
                }
            }
        }
        return page;
    }

    private static By toLocator(String input) {
        if (input.matches("^(xpath=|/)(.*)")) {
            return By.xpath(input.replaceAll("^xpath=", ""));
        } else if (input.matches("^id=(.*)")) {
            return By.id(input.substring("id=".length()));
        } else if (input.matches("^name=(.*)")) {
            return By.name(input.substring("name=".length()));
        } else if (input.matches("^css=(.*)")) {
            return By.cssSelector(input.substring("css=".length()));
        } else if (input.matches("^class=(.*)")) {
            return By.className(input.substring("class=".length()));
        } else {
            return By.id(input);
        }
    }
}
