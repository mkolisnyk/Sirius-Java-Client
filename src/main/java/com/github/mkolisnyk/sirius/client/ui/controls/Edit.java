package com.github.mkolisnyk.sirius.client.ui.controls;

import static com.github.mkolisnyk.sirius.client.ui.predicates.Getters.attribute;
import org.openqa.selenium.By;

import com.github.mkolisnyk.sirius.client.Configuration;
import com.github.mkolisnyk.sirius.client.ui.Page;

/**
 * Control extension which corresponds to the edit control.
 * @author Mykola Kolisnyk
 *
 */
public class Edit extends Editable {

    /**
     * Default constructor.
     * @param parentValue parent page object reference.
     * @param locatorValue locator to current element.
     */
    public Edit(Page parentValue, By locatorValue) {
        super(parentValue, locatorValue);
    }

    @Override
    public String getText() {
        return get(attribute("value"));
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.sirius.client.ui.controls.Control#setValue(java.lang.String)
     */
    @Override
    public Editable setValue(String value) {
        if (Configuration.platform().isAndroidNative()) {
            this.getParent().hideKeyboard();
        }
        this.click();
        this.element().clear();
        this.element().sendKeys(value);
        if (Configuration.platform().isAndroidNative()) {
            this.getParent().hideKeyboard();
        }
        return this;
    }
}
