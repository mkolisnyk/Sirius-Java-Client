package com.github.mkolisnyk.sirius.client.ui.controls;

import java.lang.reflect.Field;

import org.openqa.selenium.support.ui.ExpectedConditions;

import com.github.mkolisnyk.sirius.client.ui.FindBy;
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
     * .
     * @return .
     */
    public static ExpectedState<Boolean, Control> exists() {
        return exists(Page.getTimeout());
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
     * .
     * @return .
     */
    public static ExpectedState<Boolean, Control> disappears() {
        return disappears(Page.getTimeout());
    }
    /**
     * Checks if control is visible.
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static ExpectedState<Boolean, Control> visible(final int timeout) {
        return new ExpectedState<Boolean, Control>() {
            @Override
            public Boolean apply(Control element) {
                element.scrollTo();
                return element.waitUntil(
                        ExpectedConditions.visibilityOfElementLocated(
                                element.getLocator()),
                        timeout);
            }

            @Override
            public String description(Control parameter) {
                return String.format("Element with locator '%s' is visible.",
                        parameter.getLocatorText());
            }
        };
    }
    /**
     * .
     * @return .
     */
    public static ExpectedState<Boolean, Control> visible() {
        return visible(Page.getTimeout());
    }
    /**
     * Checks if control is invisible.
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static ExpectedState<Boolean, Control> invisible(final int timeout) {
        return new ExpectedState<Boolean, Control>() {
            @Override
            public Boolean apply(Control element) {
                return element.waitUntil(
                        ExpectedConditions.invisibilityOfElementLocated(
                                element.getLocator()),
                        timeout);
            }

            @Override
            public String description(Control parameter) {
                return String.format("Element with locator '%s' is invisible.",
                        parameter.getLocatorText());
            }
        };
    }
    /**
     * .
     * @return .
     */
    public static ExpectedState<Boolean, Control> invisible() {
        return invisible(Page.getTimeout());
    }
    /**
     * Checks if control enabled.
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static ExpectedState<Boolean, Control> enabled(final int timeout) {
        return new ExpectedState<Boolean, Control>() {
            @Override
            public Boolean apply(Control element) {
                element.scrollTo();
                return element.waitUntil(
                        ExpectedConditions.elementToBeClickable(
                                element.getLocator()),
                        timeout);
            }

            @Override
            public String description(Control parameter) {
                return String.format("Element with locator '%s' is enabled.",
                        parameter.getLocatorText());
            }
        };
    }
    /**
     * .
     * @return .
     */
    public static ExpectedState<Boolean, Control> enabled() {
        return enabled(Page.getTimeout());
    }
    /**
     * Checks if control is disabled.
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static ExpectedState<Boolean, Control> disabled(final int timeout) {
        return new ExpectedState<Boolean, Control>() {
            @Override
            public Boolean apply(Control element) {
                element.scrollTo();
                return element.waitUntil(
                        ExpectedConditions.not(
                                ExpectedConditions.elementToBeClickable(element.getLocator())),
                        timeout);
            }

            @Override
            public String description(Control parameter) {
                return String.format("Element with locator '%s' disabled.",
                        parameter.getLocatorText());
            }
        };
    }
    /**
     * .
     * @return .
     */
    public static ExpectedState<Boolean, Control> disabled() {
        return disabled(Page.getTimeout());
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
     * <p>
     * Checks if the actual page observed from application under test corresponds
     * to current page class instance. Mainly, the method waits for all controls
     * declared in current page class to appear on the current page.
     * </p>
     * <p>
     * In some cases there can be elements which are declared on the page class but may not
     * be available on screen immediately. It can be related to dynamic elements which
     * appear after some event on the page or simply by the fact that actual object isn't visible
     * on screen as it happens for Android. In order to handle such situation the <b>isCurrent</b>
     * method also checks if {@link FindBy#excludeFromSearch()} flag for each specific element
     * is set to <b>true</b>. If so, the corresponding control is not participating in check.
     * </p>
     * @param timeout the timeout to wait for each element to appear.
     * @return true if all searched control on current page object are actually present.
     * @see {@link FindBy#excludeFromSearch()}
     */
    public static ExpectedState<Boolean, Page> current(final int timeout) {
        return new ExpectedState<Boolean, Page>() {
            @Override
            public Boolean apply(Page page) {
                Field[] fields = this.getClass().getFields();
                for (Field field : fields) {
                    if (Control.class.isAssignableFrom(field.getType())) {
                        Control control = null;
                        try {
                            control = (Control) field.get(this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (control == null
                            || (!control.isExcludeFromSearch() && !control.is(exists(timeout)))) {
                            return false;
                        }
                    }
                }
                return true;
            }

            @Override
            public String description(Page parameter) {
                return String.format("the page is current.");
            }
        };
    }
    /**
     * Overloaded version of {@link ExpectedStates#current(int)} which waits for page during default timeout.
     * @return true if all searched control on current page object are actually present.
     */
    public static ExpectedState<Boolean, Page> current() {
        return current(Page.getTimeout());
    }
}
