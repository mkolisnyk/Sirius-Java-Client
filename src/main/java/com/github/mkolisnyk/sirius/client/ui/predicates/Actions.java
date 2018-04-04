package com.github.mkolisnyk.sirius.client.ui.predicates;

import static com.github.mkolisnyk.sirius.client.ui.predicates.States.current;

import org.junit.Assert;

import com.github.mkolisnyk.sirius.client.ui.Page;
import com.github.mkolisnyk.sirius.client.ui.PageFactory;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

/**
 * .
 * @author Mykola Kolisnyk
 *
 */
public class Actions {
    public static Operation<Control, Control> click() {
        return new Operation<Control, Control>() {

            @Override
            public Control apply(Control item) {
                item.verify(States.exists(Page.getTimeout()));
                item.element().click();
                return item;
            }

            @Override
            public String description(Control item) {
                return null;
            }
        };
    }
    public static Operation<Control, Control> sendKeys(final String keys) {
        return new Operation<Control, Control>() {

            @Override
            public Control apply(Control item) {
                item.verify(States.exists(Page.getTimeout()));
                item.element().sendKeys(keys);
                return item;
            }

            @Override
            public String description(Control item) {
                return null;
            }
        };
    }
    public static Operation<Control, Control> clear() {
        return new Operation<Control, Control>() {

            @Override
            public Control apply(Control item) {
                item.verify(States.exists(Page.getTimeout()));
                item.element().clear();
                return item;
            }

            @Override
            public String description(Control item) {
                return null;
            }
        };
    }
    public static <T extends Page > Operation<T, Control> waitFor(final Class<T> pageClass) {
        return new Operation<T, Control>() {

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

            @Override
            public String description(Control parameter) {
                // TODO Auto-generated method stub
                return null;
            }
            
        };
    }
}
