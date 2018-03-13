package com.github.mkolisnyk.sirius.client.ui;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTimeConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.Augmenter;
import org.reflections.Reflections;

import com.github.mkolisnyk.sirius.client.Driver;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;
import com.github.mkolisnyk.sirius.cucumber.steps.PageSteps;

import io.appium.java_client.AppiumDriver;

/**
 * Common class for all page objects. Any abstraction which represents the application
 * under test page should be extended from this class.
 * @author Mykola Kolisnyk
 */
public class Page {
    private static final long TINY_TIMEOUT = 1;
    private static final long SHORT_TIMEOUT = 5;
    private static final int SCROLL_TOP_PART = 9;
    private static final int SCROLL_TOTAL_PARTS = 10;
    private static final long DEFAULT_TIMEOUT = 60L;
    private static long timeout = DEFAULT_TIMEOUT;
    private static ConcurrentHashMap<String, Page> currentPages = new ConcurrentHashMap<String, Page>();
    private static String defaultPagesPackage = "";

    private WebDriver driver;

    /**
     * Default constructor which binds the WebDriver instance to the Page abstractions.
     * This constructor should be available in all extended classes especially when
     * page object initialization is done via {@link PageFactory#init(WebDriver, Class)} call.
     * @param driverValue the actual WebDriver instance.
     */
    public Page(WebDriver driverValue) {
        this.driver = driverValue;
    }

    /**
     * Returns the timeout value which is currently set.
     * This is explicit waiting timeout which is used to wait for some event to happen.
     * @return currently set timeout.
     */
    public static long getTimeout() {
        return timeout;
    }

    /**
     * Assigns new timeout value.
     * @param timeoutValue new timeout value.
     */
    public static void setTimeout(long timeoutValue) {
        Page.timeout = timeoutValue;
    }

    /**
     * Returns package name which is used as the root package for all
     * page classes in the entire solution. Such package customisation is needed
     * for {@link Page#forName(String)} method to minimise search time as
     * the entire solution may appear to be big.
     * @return root package for page classes.
     */
    public static String getDefaultPagesPackage() {
        return defaultPagesPackage;
    }

    /**
     * Sets package name which is used as the root package for all
     * page classes in the entire solution. Such package customisation is needed
     * for {@link Page#forName(String)} method to minimise search time as
     * the entire solution may appear to be big.
     * @param defaultPagesPackageValue root package for page classes.
     */
    public static void setDefaultPagesPackage(String defaultPagesPackageValue) {
        Page.defaultPagesPackage = defaultPagesPackageValue;
    }

    /**
     * Retrieves page object by it's logical name specified as the value of {@link Alias}
     * annotation. This is overloaded version of the {@link Page#forName(String, String)} method
     * where the pages package name is retrieved from locally stored static variable by means of
     * {@link Page#getDefaultPagesPackage()} call.
     * @param name the logical name of the page class to retrieve instance of.
     * @return the page class which alias matches the <b>name</b> paremeter.
     * @throws Exception any exception related to data conversion or null value.
     * @see Alias
     */
    public static Page forName(String name) throws Exception {
        return forName(name, getDefaultPagesPackage());
    }

    /**
     * Retrieves page object by it's logical name specified as the value of {@link Alias}
     * annotation. Mainly, it searches for classes extended from {@link Page} class inside
     * the package specified by <b>pagePackage</b> parameter.
     * @param name the logical name of the page class to retrieve instance of.
     * @param pagePackage the package to search page classes in.
     * @return the page class which alias matches the <b>name</b> paremeter.
     * @throws Exception any exception related to data conversion or null value.
     * @see Page#forName(String)
     * @see Alias
     */
    public static Page forName(String name, String pagePackage) throws Exception {
        Reflections reflections = new Reflections(pagePackage);
        Set<Class<? extends Page>> subTypes = reflections.getSubTypesOf(Page.class);
        for (Class<? extends Page> type : subTypes) {
            Alias annotation = type.getAnnotation(Alias.class);
            if (annotation != null && annotation.value().equals(name)) {
                return PageFactory.init(Driver.current(), type);
            }
        }
        return null;
    }

    /**
     * Gets the current page for current thread instance.
     * @return the Page object for current page.
     */
    public static Page getCurrent() {
        return currentPages.get(Driver.getThreadName());
    }

    /**
     * Sets the current page for current thread instance.
     * @param newPage the Page object for current page.
     */
    public static void setCurrent(Page newPage) {
        currentPages.put(Driver.getThreadName(), newPage);
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
     * of proposed page classes fits the current state.
     * @throws Exception any class conversion or null pointer exception.
     * @see {@link Page#isCurrent()}
     */
    public static Page getCurrentFromList(Class<? extends Page>[] pageClasses, int tries) throws Exception {
        return getCurrentFromList(pageClasses, tries, false);
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
     * cached source for verifications prior to applying to application under test directly.
     * @return the Page instance for the current page found or null if none
     * of proposed page classes fits the current state.
     * @throws Exception any class conversion or null pointer exception.
     * @see {@link Page#isCurrent()}
     */
    public static Page getCurrentFromList(Class<? extends Page>[] pageClasses, int tries, boolean useCache)
            throws Exception {
        Page[] pages = new Page[pageClasses.length];
        for (int i = 0; i < pageClasses.length; i++) {
            pages[i] = PageFactory.init(Driver.current(), pageClasses[i]);
        }
        for (int i = 0; i < tries; i++) {
            for (Page page : pages) {
                if (page.isCurrent(1)) {
                    return page;
                }
            }
        }
        return null;
    }

    /**
     * Goes through the list of proposed controls and returns the first one which appears.
     * @param controls the list of controls to look for first available in.
     * @param tries the limit of tries (similar to the timeout).
     * @return the first control object which appears to be existing.
     * @throws Exception any assertion or data conversion errors.
     */
    public static Control getFirstAvailableControlFromList(Control[] controls, int tries) throws Exception {
        for (int i = 0; i < tries; i++) {
            for (Control control : controls) {
                if (control.exists(1)) {
                    return control;
                }
            }
        }
        return null;
    }

    /**
     * Gets the actual WebDriver object if some WebDriver API is needed directly.
     * @return the actual WebDriver object.
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * <p>
     * Performs actions to navigate to current page. By default it does nothing.
     * But any extended class can override this method and perform actual actions
     * which result with specific page instance to appear.
     * </p>
     * <p>
     * This method is actively used by {@link PageSteps#navigateToPage(String)} method
     * as well as associated Cucumber-JVM keywords:
     * <pre>
     * Given I am on the "&lt;Page Name&gt;" page
     * </pre>
     * or
     * <pre>
     * When I go to the "&lt;Page Name&gt;" page
     * </pre>
     * The idea is that for any page classes which can be used for such navigation operation the
     * <b>navigate</b> method is overridden to implement actual navigation behaviour. This way
     * the same implementation can be applied for all page classes which can be navigated to.
     * </p>
     * @return the object corresponding to the page which should be current.
     * @throws Exception any assertion or other exception which appears
     * during page navigation processing.
     */
    public Page navigate() throws Exception {
        return this;
    }

    /**
     * Checks if some text is available on current page.
     * In a number of cases we just need to check that some labels are available or some text is shown.
     * It's too expensive to reserve dedicated field for each of such elements. But in order to make
     * such check widely used the <b>isTextPresent</b> method gets such element dynamically and
     * waits for element to appear.
     * @param text the text value to wait for.
     * @return true - the text label is available on screen, false - otherwise.
     */
    public boolean isTextPresent(String text) {
        Control element = getTextControl(text);
        return element.exists();
    }

    /**
     * 
     * @return
     * @throws IOException
     */
    public byte[] captureScreenShot() throws IOException {
        WebDriver augmentedDriver = new Augmenter().augment(this.getDriver());
        byte[] data = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
        return data;
    }

    /**
     * 
     * @param destination
     * @return
     * @throws IOException
     */
    public File captureScreenShot(String destination) throws IOException {
        WebDriver augmentedDriver = new Augmenter().augment(this.getDriver());
        File srcFile = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
        File output = new File(destination);
        FileUtils.copyFile(srcFile, output);
        return output;
    }

    /**
     * 
     * @return
     */
    public String getSource() {
        return this.getDriver().getPageSource();
    }

    /**
     * 
     * @return
     */
    public Control getScrollable() {
        Control scrollable = new Control(this, By.xpath("(//*[@scrollable='true'])[1]"));
        return scrollable;
    }

    /**
     * 
     * @param message
     * @return
     */
    public Control getTextControl(String message) {
        Control text = null;
        String locator = "";
        locator = "//*[@text=\"" + message + "\" or contains(@text,\"" + message + "\") or contains(text(),\"" + message
                + "\") or text()=\"" + message + "\" or contains(@content-desc,\"" + message + "\")]";
        text = new Control(this, By.xpath(locator));
        return text;
    }

    /**
     * 
     * @return
     */
    private static Rectangle getScreenSize() {
        Rectangle area = new Rectangle();
        Dimension size = Driver.current().manage().window().getSize();
        area.setBounds(0, 0, size.getWidth(), size.getHeight());
        return area;
    }

    /**
     * 
     * @param vertical
     * @param leftTop
     * @param once
     * @return
     */
    public boolean swipeScreen(boolean vertical, boolean leftTop, boolean once) {
        return swipeScreen(vertical, leftTop, once, 2);
    }

    /**
     * 
     * @param vertical
     * @param leftTop
     * @param once
     * @param seconds
     * @return
     */
    public boolean swipeScreen(boolean vertical, boolean leftTop, boolean once, int seconds) {
        Control scrollable = getScrollable();
        if (!scrollable.exists(SHORT_TIMEOUT)) {
            return false;
        }
        Rectangle area = scrollable.getRect();
        Rectangle screenArea = Page.getScreenSize();
        area.x = Math.max(area.x, screenArea.x);
        area.y = Math.max(area.y, screenArea.y);
        area.width = Math.min(area.width, screenArea.width - area.x);
        area.height = Math.min(area.height, screenArea.height - area.y);

        int startX = area.x + area.width / 2;
        int startY = 0;
        int endX = area.x + area.width / 2;
        int endY = 0;
        if (vertical) {
            startX = area.x + area.width / 2;
            endX = area.x + area.width / 2;
            if (leftTop) {
                startY = area.y + area.height / SCROLL_TOTAL_PARTS;
                endY = area.y + SCROLL_TOP_PART * area.height / SCROLL_TOTAL_PARTS;
            } else {
                startY = area.y + SCROLL_TOP_PART * area.height / SCROLL_TOTAL_PARTS;
                endY = area.y + area.height / SCROLL_TOTAL_PARTS;
            }
        } else {
            startY = area.y + area.height / 2;
            endY = area.y + area.height / 2;
            if (leftTop) {
                startX = area.x + SCROLL_TOP_PART * area.width / SCROLL_TOTAL_PARTS;
                endX = area.x + area.width / SCROLL_TOTAL_PARTS;
            } else {
                startX = area.x + area.width / SCROLL_TOTAL_PARTS;
                endX = area.x + SCROLL_TOP_PART * area.width / SCROLL_TOTAL_PARTS;
            }
        }
        String prevState = "";
        String currentState = this.getSource();
        int times = 0;
        final int maxTries = 50;
        while (!currentState.equals(prevState)) {
            TouchActions action = new TouchActions(driver);
            action.scroll(scrollable.element(), endX - startX, endY - startY)
                    .pause(seconds * DateTimeConstants.MILLIS_PER_SECOND);
            action.perform();
            // ((AppiumDriver<?>) this.getDriver())
            // .swipe(startX, startY, endX, endY, seconds *
            // DateTimeConstants.MILLIS_PER_SECOND);
            if (once || times > maxTries) {
                break;
            }
            times++;
            prevState = currentState;
            currentState = this.getSource();
        }
        return true;
    }

    /**
     * 
     * @param control
     * @param up
     * @return
     */
    public boolean scrollTo(Control control, boolean up) {
        if (control.exists(TINY_TIMEOUT)) {
            return true;
        }
        Control scrollable = getScrollable();
        if (!scrollable.exists(TINY_TIMEOUT)) {
            return false;
        }
        String prevState = "";
        String currentState = this.getSource();
        while (!currentState.equals(prevState) && this.swipeScreen(true, up, true)) {
            if (control.exists(TINY_TIMEOUT)) {
                return true;
            }
            prevState = currentState;
            currentState = this.getSource();
        }
        return false;
    }

    /**
     * 
     * @param up
     * @return
     * @throws Exception
     */
    public boolean scrollTo(boolean up) throws Exception {
        return swipeScreen(true, up, false);
    }

    /**
     * 
     * @param control
     * @param scrollDirection
     * @return
     */
    public boolean scrollTo(Control control, ScrollTo scrollDirection) {
        switch (scrollDirection) {
        case TOP_ONLY:
            return scrollTo(control, true);
        case TOP_BOTTOM:
            return scrollTo(control, true) || scrollTo(control, false);
        case BOTTOM_ONLY:
            return scrollTo(control, false);
        case BOTTOM_TOP:
            return scrollTo(control, false) || scrollTo(control, true);
        default:
            return scrollTo(control, true) || scrollTo(control, false);
        }
    }

    /**
     * 
     * @param control
     * @return
     * @throws Exception
     */
    public boolean scrollTo(Control control) throws Exception {
        return scrollTo(control, ScrollTo.TOP_BOTTOM);
    }

    /**
     * 
     * @param text
     * @param up
     * @return
     * @throws Exception
     */
    public boolean scrollTo(String text, boolean up) throws Exception {
        Control control = this.getTextControl(text);
        return this.scrollTo(control, up);
    }

    /**
     * 
     * @param text
     * @param scrollDirection
     * @return
     */
    public boolean scrollTo(String text, ScrollTo scrollDirection) {
        Control control = this.getTextControl(text);
        return scrollTo(control, scrollDirection);
    }

    /**
     * 
     * @param text
     * @return
     */
    public boolean scrollTo(String text) {
        return scrollTo(text, ScrollTo.TOP_BOTTOM);
    }

    /**
     * 
     */
    public void hideKeyboard() {
        try {
            ((AppiumDriver<?>) this.getDriver()).hideKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param timeoutValue
     * @return
     * @throws Exception
     */
    public boolean isCurrent(long timeoutValue) throws Exception {
        Field[] fields = this.getClass().getFields();
        for (Field field : fields) {
            if (Control.class.isAssignableFrom(field.getType())) {
                Control control = (Control) field.get(this);
                if (!control.isExcludeFromSearch() && !control.exists(timeoutValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public boolean isCurrent() throws Exception {
        return isCurrent(getTimeout());
    }

    protected boolean allElementsAre(Control[] elements, String state) throws Exception {
        for (Control element : elements) {
            if (!(Boolean) (element.getClass().getMethod(state).invoke(element))) {
                return false;
            }
        }
        return true;
    }

    protected boolean anyOfElementsIs(Control[] elements, String state) throws Exception {
        for (Control element : elements) {
            if ((Boolean) element.getClass().getMethod(state, long.class).invoke(element, 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean allElementsExist(Control[] elements) throws Exception {
        return allElementsAre(elements, "exists");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean allElementsDoNotExist(Control[] elements) throws Exception {
        return allElementsAre(elements, "disappears");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean allElementsAreVisible(Control[] elements) throws Exception {
        return allElementsAre(elements, "visible");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean allElementsAreInvisible(Control[] elements) throws Exception {
        return allElementsAre(elements, "invisible");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean allElementsAreEnabled(Control[] elements) throws Exception {
        return allElementsAre(elements, "enabled");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean allElementsAreDisabled(Control[] elements) throws Exception {
        return allElementsAre(elements, "disabled");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean anyOfElementsExist(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "exists");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean anyOfElementsDoNotExist(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "disappears");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean anyOfElementsIsVisible(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "visible");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean anyOfElementsIsInvisible(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "invisible");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean anyOfElementsIsEnabled(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "enabled");
    }

    /**
     * 
     * @param elements
     * @return
     * @throws Exception
     */
    public boolean anyOfElementsIsDisabled(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "disabled");
    }

    /**
     * 
     * @param name
     * @return
     * @throws Exception
     */
    public Control onPage(String name) throws Exception {
        for (Field field : this.getClass().getFields()) {
            if (Control.class.isAssignableFrom(field.getType())) {
                Alias alias = field.getAnnotation(Alias.class);
                if (alias != null && name.equals(alias.value())) {
                    return (Control) field.get(this);
                }
            }
        }
        return null;
    }
}
