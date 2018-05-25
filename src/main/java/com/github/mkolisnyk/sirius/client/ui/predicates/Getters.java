package com.github.mkolisnyk.sirius.client.ui.predicates;

import static com.github.mkolisnyk.sirius.client.ui.predicates.States.current;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.exists;

import java.awt.Rectangle;
import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import com.github.mkolisnyk.sirius.client.Driver;
import com.github.mkolisnyk.sirius.client.ui.AlertPage;
import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.PageFactory;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

/**
 * Collects predicates responsible for various get operations.
 * @author Mykola Kolisnyk
 *
 */
public final class Getters {
    private Getters() {
    }
    /**
     * Gets alert page instance.
     * @return the alert page.
     */
    public static Operation<AlertPage, Page> alert() {
        return new NonDescriptive<AlertPage, Page>() {

            @Override
            public AlertPage apply(Page page) {
                AlertPage alert = new AlertPage(page);
                return alert;
            }
        };
    }
    /**
     * Gets the page source code. Usually it is page HTML (for web) or at least some
     * XML representation (for WebDriver implementation for other platforms).
     * @return current page source.
     */
    public static Operation<String, Page> source() {
        return new NonDescriptive<String, Page>() {

            @Override
            public String apply(Page page) {
                return page.getDriver().getPageSource();
            }
        };
    }
    /**
     * <p>
     * Checks multiple page classes in order to identify which of the classes proposed
     * fit the current page. For each page class the {@link Page#isCurrent()} method is called.
     * If for any page class the return value is true the instance of this class is created and
     * returned.
     * </p>
     * <p>
     * If none of proposed classes matches current page state the new iteration starts.
     * </p>
     * <p>
     * If nothing is found after specified iteration limit the null value is returned.
     * </p>
     * @param pageClasses the list of page classes to look in.
     * @param tries the number of iterations.
     * @param useCache (not in use at the moment) the flag identifying whether method should use
     *      cached source for verifications prior to applying to application under test directly.
     * @return the Page instance for the current page found or null if none
     *      of proposed page classes fits the current state.
     */
    public static Operation<Page, Page> first(
            Class<? extends Page>[] pageClasses, int tries, boolean useCache) {
        return new NonDescriptive<Page, Page>() {

            @Override
            public Page apply(Page page) {
                Page[] pages = new Page[pageClasses.length];
                for (int i = 0; i < pageClasses.length; i++) {
                    try {
                        pages[i] = PageFactory.init(Driver.current(), pageClasses[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < tries; i++) {
                    for (Page item : pages) {
                        if (item.is(current(1))) {
                            return page;
                        }
                    }
                }
                return null;
            }
        };
    }
    /**
     * <p>
     * Checks multiple page classes in order to identify which of the classes proposed
     * fit the current page. For each page class the {@link Page#isCurrent()} method is called.
     * If for any page class the return value is true the instance of this class is created and
     * returned.
     * </p>
     * <p>
     * If none of proposed classes matches current page state the new iteration starts.
     * </p>
     * <p>
     * If nothing is found after specified iteration limit the null value is returned.
     * </p>
     * @param pageClasses the list of page classes to look in.
     * @param tries the number of iterations
     * @return the Page instance for the current page found or null if none
     *     of proposed page classes fits the current state.
     */
    public static Operation<Page, Page> first(
            Class<? extends Page>[] pageClasses, int tries) {
        return first(pageClasses, tries, false);
    }
    /**
     * Goes through the list of proposed controls and returns the first one which appears.
     * @param controls the list of controls to look for first available in.
     * @param tries the limit of tries (similar to the timeout).
     * @return the first control object which appears to be existing.
     */
    public static Operation<Control, Page> first(Control[] controls, int tries) {
        return new NonDescriptive<Control, Page>() {

            @Override
            public Control apply(Page page) {
                for (int i = 0; i < tries; i++) {
                    for (Control control : controls) {
                        if (control.is(exists(1))) {
                            return control;
                        }
                    }
                }
                return null;
            }
        };
    }
    /**
     * Goes through the list of proposed controls and returns the first one which appears.
     * In this implementation controls are defined by their names and those elements are searched
     * on current page object.
     * @param controlNames the list of controls to look for first available in.
     * @param tries the limit of tries (similar to the timeout).
     * @return the first control object which appears to be existing.
     */
    public static Operation<Control, Page> first(String[] controlNames, int tries) {
        return new NonDescriptive<Control, Page>() {

            @Override
            public Control apply(Page page) {
                Control[] controls = new Control[controlNames.length];
                for (int i = 0; i < controlNames.length; i++) {
                    try {
                        controls[i] = page.field(controlNames[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return page.get(first(controls, tries));
            }
        };
    }
    /**
     * Goes through the list of proposed controls and returns the first one which appears.
     * In this implementation controls are defined by their names and those elements are searched
     * on current page object.
     * @param controlNames the list of controls to look for first available in.
     * @param tries the limit of tries (similar to the timeout).
     * @return the first control object which appears to be existing.
     */
    public static Operation<Control, Page> first(List<String> controlNames, int tries) {
        return new NonDescriptive<Control, Page>() {

            @Override
            public Control apply(Page page) {
                String[] controlArray = new String[controlNames.size()];
                controlArray = controlNames.toArray(controlArray);
                return page.get(first(controlArray, tries));
            }
        };
    }
    /**
     * Predicate for retrieving parent object of the control.
     * @return operation object which is applicable for Control instance.
     */
    public static Operation<Page, Control> parent() {
        return new NonDescriptive<Page, Control>() {

            @Override
            public Page apply(Control item) {
                return item.getParent();
            }
        };
    }
    /**
     * Predicate for control text retrieval.
     * @return operation object which is applicable for Control instance.
     */
    public static Operation<String, Control> text() {
        return new NonDescriptive<String, Control>() {

            @Override
            public String apply(Control item) {
                item.verify(exists());
                return item.getText();
            }
        };
    }
    /**
     * Predicate for control value retrieval.
     * @return operation object which is applicable for Control instance.
     */
    public static Operation<String, Control> value() {
        return new NonDescriptive<String, Control>() {

            @Override
            public String apply(Control item) {
                return item.getValue();
            }
        };
    }
    /**
     * Predicate for control attribute retrieval.
     * @param name the name of the attribute to get value of.
     * @return operation object which is applicable for Control instance.
     */
    public static Operation<String, Control> attribute(final String name) {
        return new NonDescriptive<String, Control>() {

            @Override
            public String apply(Control item) {
                item.verify(exists());
                return item.element().getAttribute(name);
            }
        };
    }
    /**
     * Getter for element rectangular area.
     * @return operation object which returns rectangular object with element dimensions.
     */
    public static Operation<Rectangle, Control> rectangle() {
        return new NonDescriptive<Rectangle, Control>() {

            @Override
            public Rectangle apply(Control item) {
                item.verify(exists());
                Rectangle rect = new Rectangle();
                //Point location = ((MobileElement) item.element()).getCoordinates().onPage();
                Point location = item.element().getLocation();
                Dimension size = item.element().getSize();
                rect.x = location.x;
                rect.y = location.y;
                rect.width = size.width;
                rect.height = size.height;
                return rect;
            }
        };
    }
}
