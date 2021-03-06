package com.github.mkolisnyk.sirius.client.ui.predicates;

import java.lang.reflect.Field;

import org.openqa.selenium.support.ui.ExpectedConditions;

import com.github.mkolisnyk.sirius.client.Configuration;
import com.github.mkolisnyk.sirius.client.ui.Alias;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

/**
 * The container of Control state predicates.
 * @author Mykola Kolisnyk
 */
public final class States {
    private States() {
    }
    /**
     * Makes sure if element is checked.
     * @return true - element checked, false otherwise.
     */
    @Alias("Checked")
    public static Operation<Boolean, Control> checked() {
        return new Operation<Boolean, Control>() {
            @Override
            public Boolean apply(Control element) {
                element.verify(exists());
                if (Configuration.platform().isIOSNative()) {
                    String value = element.element().getAttribute("value");
                    return value.equals("1");
                } else {
                    return element.element().getAttribute("checked").equals("true")
                            || element.element().getAttribute("selected").equals("true");
                }
            }

            @Override
            public String description(Control parameter) {
                return String.format("Element with locator '%s' is checked.",
                        parameter.getLocatorText());
            }
        };
    }
    /**
     * Checks if control exists.
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static Operation<Boolean, Control> exists(final int timeout) {
        return new Operation<Boolean, Control>() {
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
    @Alias("Exists")
    public static Operation<Boolean, Control> exists() {
        return exists(Page.getTimeout());
    }
    /**
     * Checks if control doesn't exist
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static Operation<Boolean, Control> disappears(final int timeout) {
        return new Operation<Boolean, Control>() {
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
    @Alias("Disappears")
    public static Operation<Boolean, Control> disappears() {
        return disappears(Page.getTimeout());
    }
    /**
     * Checks if control is visible.
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static Operation<Boolean, Control> visible(final int timeout) {
        return new Operation<Boolean, Control>() {
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
    @Alias("Visible")
    public static Operation<Boolean, Control> visible() {
        return visible(Page.getTimeout());
    }
    /**
     * Checks if control is invisible.
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static Operation<Boolean, Control> invisible(final int timeout) {
        return new Operation<Boolean, Control>() {
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
    @Alias("Invisible")
    public static Operation<Boolean, Control> invisible() {
        return invisible(Page.getTimeout());
    }
    /**
     * Checks if control enabled.
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static Operation<Boolean, Control> enabled(final int timeout) {
        return new Operation<Boolean, Control>() {
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
    @Alias("Enabled")
    public static Operation<Boolean, Control> enabled() {
        return enabled(Page.getTimeout());
    }
    /**
     * Checks if control is disabled.
     * @param timeout waiting timeout.
     * @return true - element exists, false - otherwise.
     */
    public static Operation<Boolean, Control> disabled(final int timeout) {
        return new Operation<Boolean, Control>() {
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
    @Alias("Disabled")
    public static Operation<Boolean, Control> disabled() {
        return disabled(Page.getTimeout());
    }
    /**
     * Verifies if element has specific text.
     * @param text the text to check.
     * @return true if element has specified text. False - otherwise.
     */
    @Alias("Has Text")
    public static Operation<Boolean, Control> hasText(final String text) {
        return new Operation<Boolean, Control>() {
            @Override
            public Boolean apply(Control element) {
                return element.get(Getters.text()).equals(text);
            }

            @Override
            public String description(Control parameter) {
                return String.format("Element with locator '%s' has '%s' text.",
                        parameter.getLocatorText(), text);
            }
        };
    }
    /**
     * Checks the value of specific element for equality to the text specified as the parameter.
     * Unlike {@link States#hasText(String)} predicate this one operates with getValue method or
     * specific control and in general case it may return something different than just text.
     * @param text the text to compare value with.
     * @return true if element has specified text. False - otherwise.
     */
    @Alias("Value")
    public static Operation<Boolean, Control> valueIs(final String text) {
        return new Operation<Boolean, Control>() {
            @Override
            public Boolean apply(Control element) {
                return element.get(Getters.value()).equals(text);
            }

            @Override
            public String description(Control parameter) {
                return String.format("Element with locator '%s' has '%s' value.",
                        parameter.getLocatorText(), text);
            }
        };
    }
    /**
     * Checks if some text is available on current page.
     * In a number of cases we just need to check that some labels are available or some text is shown.
     * It's too expensive to reserve dedicated field for each of such elements. But in order to make
     * such check widely used the <b>isTextPresent</b> method gets such element dynamically and
     * waits for element to appear.
     * @param text the text to check.
     * @return predicate which verifies text presence.
     */
    public static Operation<Boolean, Page> textPresent(final String text) {
        return new Operation<Boolean, Page>() {
            @Override
            public Boolean apply(Page page) {
                Control element = page.getTextControl(text);
                return element.is(exists());
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
    public static Operation<Boolean, Page> current(final int timeout) {
        return new Operation<Boolean, Page>() {
            @Override
            public Boolean apply(Page page) {
                Field[] fields = page.getClass().getFields();
                for (Field field : fields) {
                    if (Control.class.isAssignableFrom(field.getType())) {
                        Control control = null;
                        try {
                            System.out.println("Checking: " + field.getName());
                            control = (Control) field.get(page);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (control == null
                            || !control.isExcludeFromSearch() && !control.is(exists(timeout))) {
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
     * Overloaded version of {@link States#current(int)} which waits for page during default timeout.
     * @return true if all searched control on current page object are actually present.
     */
    public static Operation<Boolean, Page> current() {
        return current(Page.getTimeout());
    }
}
