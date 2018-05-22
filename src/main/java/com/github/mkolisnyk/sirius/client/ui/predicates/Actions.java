package com.github.mkolisnyk.sirius.client.ui.predicates;

import static com.github.mkolisnyk.sirius.client.ui.predicates.States.current;

import org.junit.Assert;

import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.PageFactory;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import com.github.mkolisnyk.sirius.client.ui.controls.Editable;

/**
 * Collects predicates responsible for various actions on elements.
 * @author Mykola Kolisnyk
 *
 */
public final class Actions {
    private Actions() {
    }
    /**
     * Predicate for click operation.
     * @return the object with {@link Operation} interface
     *      which is applicable for Control and returns the Control.
     */
    public static Operation<Control, Control> click() {
        return new NonDescriptive<Control, Control>() {

            @Override
            public Control apply(Control item) {
                item.verify(States.exists(Page.getTimeout()));
                item.element().click();
                return item;
            }
        };
    }
    /**
     * Predicate for sending sequence of keys to specific element.
     * @param keys the string containing keys to enter.
     * @return the object with {@link Operation} interface
     *      which is applicable for Control and returns the Control.
     */
    public static Operation<Control, Control> sendKeys(final String keys) {
        return new NonDescriptive<Control, Control>() {

            @Override
            public Control apply(Control item) {
                item.verify(States.exists(Page.getTimeout()));
                item.element().sendKeys(keys);
                return item;
            }
        };
    }
    /**
     * Predicate which clears field content.
     * @return the object with {@link Operation} interface
     *      which is applicable for Control and returns the Control.
     */
    public static Operation<Editable, Control> clear() {
        return new NonDescriptive<Editable, Control>() {

            @Override
            public Editable apply(Control item) {
                item.verify(States.exists(Page.getTimeout()));
                item.element().clear();
                return (Editable) item;
            }
        };
    }
    /**
     * Waits for specific page to appear.
     * @param <T> the class of the page to wait for.
     * @param pageClass the class of the page to wait for.
     * @return the operation object which is applicable for control and
     *      returns the instance of the expected page class.
     */
    public static <T extends Page > Operation<T, Control> waitFor(final Class<T> pageClass) {
        return new NonDescriptive<T, Control>() {

            @Override
            public T apply(Control item) {
                click();
                T page = null;
                try {
                    page = PageFactory.init(item.getDriver(), pageClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Assert.assertTrue(
                        String.format("The page of %s class didn't appear during specified timeout",
                                pageClass.getName()),
                        page.is(current()));
                return page;
            }
        };
    }
}
