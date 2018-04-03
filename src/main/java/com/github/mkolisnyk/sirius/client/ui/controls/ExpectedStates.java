package com.github.mkolisnyk.sirius.client.ui.controls;

import org.openqa.selenium.support.ui.ExpectedConditions;

import com.github.mkolisnyk.sirius.client.ui.Page;

/**
 * The container of Control state predicates.
 * @author Mykola Kolisnyk
 */
public final class ExpectedStates {
    private ExpectedStates() {
    }
    /**
     * Checks if control exists.
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static ExpectedState<Boolean, Control> exists(final int timeout) {
        return new ExpectedState<Boolean, Control>() {
            @Override
            public Boolean apply(Control element) {
                element.scrollTo();
                return element.waitUntil(
                        ExpectedConditions.presenceOfElementLocated(
                                element.getLocator()),
                        timeout);
            }

            @Override
            public String description(Control parameter) {
                return String.format("Element with locator '%s' exists.",
                        parameter.getLocatorText());
            }
        };
    }
    /**
     * Checks if control doesn't exist
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static ExpectedState<Boolean, Control> disappears(final int timeout) {
        return new ExpectedState<Boolean, Control>() {
            @Override
            public Boolean apply(Control element) {
                return element.waitUntil(
                        ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(
                                element.getLocator())),
                        timeout);
            }

            @Override
            public String description(Control parameter) {
                return String.format("Element with locator '%s' disappeared.",
                        parameter.getLocatorText());
            }
        };
    }
    /**
     * Verifies if element has specific text.
     * @param text the text to check.
     * @return true if element has specified text. False - otherwise.
     */
    public static ExpectedState<Boolean, Control> hasText(final String text) {
        return new ExpectedState<Boolean, Control>() {
            @Override
            public Boolean apply(Control element) {
                return element.getText().equals(text);
            }

            @Override
            public String description(Control parameter) {
                return String.format("Element with locator '%s' doesn't have '%s' text.",
                        parameter.getLocatorText(), text);
            }
        };
    }
    /**
     * Checks if page contains specified text.
     * @param text the text to check.
     * @return predicate which verifies text presence.
     */
    public static ExpectedState<Boolean, Page> textPresent(final String text) {
        return new ExpectedState<Boolean, Page>() {
            @Override
            public Boolean apply(Page page) {
                return page.isTextPresent(text);
            }

            @Override
            public String description(Page parameter) {
                return String.format("the '%s' text is present on the page.",
                        text);
            }
        };
      }
    /**
     * Predicate used to check if specific page is current.
     * @return true if page is current, false - otherwise.
     */
    public static ExpectedState<Boolean, Page> current() {
        return new ExpectedState<Boolean, Page>() {
            @Override
            public Boolean apply(Page page) {
                try {
                    return page.isCurrent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public String description(Page parameter) {
                return String.format("the page is current.");
            }
        };
      }
}
