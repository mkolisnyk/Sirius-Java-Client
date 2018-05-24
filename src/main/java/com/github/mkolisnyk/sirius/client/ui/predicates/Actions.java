package com.github.mkolisnyk.sirius.client.ui.predicates;

import static com.github.mkolisnyk.sirius.client.ui.predicates.States.current;
import static com.github.mkolisnyk.sirius.client.ui.predicates.Getters.rectangle;

import java.awt.Rectangle;

import org.junit.Assert;

import com.github.mkolisnyk.sirius.client.ui.Direction;
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
     * Predicate wrapping the click on specific coordinates.
     * @param x horizontal coordinate.
     * @param y vertical coordinate.
     * @return the object with {@link Operation} interface
     *      which is applicable for Control and returns the Control.
     */
    public static Operation<Control, Control> click(int x, int y) {
        return new NonDescriptive<Control, Control>() {

            @Override
            public Control apply(Control item) {
                item.verify(States.exists(Page.getTimeout()));
                org.openqa.selenium.interactions.Actions builder
                    = new org.openqa.selenium.interactions.Actions(item.getDriver());
                builder.moveToElement(item.element(), x, y).click().build().perform();
                return item;
            }
        };
    }
    /**
     * Predicate wrapping the click at specific side of the control (any of left, right, top or bottom).
     * @param direction the {@link Direction} enum value indicating the part of element to click on.
     * @return the object with {@link Operation} interface
     *      which is applicable for Control and returns the Control.
     */
    public static Operation<Control, Control> click(Direction direction) {
        return new NonDescriptive<Control, Control>() {

            @Override
            public Control apply(Control item) {
                item.verify(States.exists(Page.getTimeout()));
                Rectangle rect = item.get(rectangle());
                int x = rect.width / 2;
                int y = rect.height / 2;
                if (direction.equals(Direction.LEFT)) {
                    x /= 2;
                } else if (direction.equals(Direction.RIGHT)) {
                    x += rect.width / 2 / 2;
                } else if (direction.equals(Direction.TOP)) {
                    y /= 2;
                } else {
                    y += rect.height / 2 / 2;
                }
                return click(x, y).apply(item);
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
