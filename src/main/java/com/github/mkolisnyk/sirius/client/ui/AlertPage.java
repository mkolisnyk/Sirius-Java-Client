package com.github.mkolisnyk.sirius.client.ui;

/**
 * Common page class for alert messages.
 * @author Mykola Kolisnyk
 *
 */
public class AlertPage extends Page {
    private Page parent;
    /**
     * Alert page constructor.
     * @param parentPage the reference to the parent page where alert was thrown from.
     */
    public AlertPage(Page parentPage) {
        super(parentPage.getDriver());
        this.parent = parentPage;
    }
    /**
     * Simply presses OK on available alert dialog.
     * @return original page object.
     */
    public Page accept() {
        this.getDriver().switchTo().alert().accept();
        return this.parent;
    }
    /**
     * Presses Cancel on confirmation dialog.
     * @return original page object.
     */
    public Page dismiss() {
        this.getDriver().switchTo().alert().dismiss();
        return this.parent;
    }
    /**
     * Enters value into prompt dialog and accepts it.
     * @param value the text to enter.
     * @return original page object.
     */
    public Page prompt(String value) {
        this.getDriver().switchTo().alert().sendKeys(value);
        return accept();
    }
    /**
     * Gets the alert message text.
     * @return alert message text.
     */
    public String getText() {
        return this.getDriver().switchTo().alert().getText();
    }
}
