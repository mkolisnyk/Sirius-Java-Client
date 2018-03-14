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
     * Captures the screenshot of current page.
     * @return The byte array containing image data.
     * @throws IOException I/O problems while retrieving the image data.
     */
    public byte[] captureScreenShot() throws IOException {
        WebDriver augmentedDriver = new Augmenter().augment(this.getDriver());
        byte[] data = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
        return data;
    }

    /**
     * Captures the screenshot of current page and stores it in the file.
     * @param destination the destination path of the screenshot.
     * @return the {@link File} object referencing to generated file.
     * @throws IOException I/O problems.
     */
    public File captureScreenShot(String destination) throws IOException {
        WebDriver augmentedDriver = new Augmenter().augment(this.getDriver());
        File srcFile = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
        File output = new File(destination);
        FileUtils.copyFile(srcFile, output);
        return output;
    }

    /**
     * Gets the page source code. Usually it is page HTML (for web) or at least some
     * XML representation (for WebDriver implementation for other platforms).
     * @return current page source.
     */
    public String getSource() {
        return this.getDriver().getPageSource();
    }

    /**
     * <p>
     * Returns element which is available for scrolling. By default, it's first scrollable element.
     * </p>
     * <p>
     * <b>NOTE:</b> currently, it is applicable for Android platform only.
     * </p>
     * @return control object which can be used for scrolling.
     */
    public Control getScrollable() {
        Control scrollable = new Control(this, By.xpath("(//*[@scrollable='true'])[1]"));
        return scrollable;
    }

    /**
     * Dynamically generates control object which corresponds to the element
     * containing text specified by the parameter.
     * @param message the text the control should contain.
     * @return the control with the text containing specified message parameter.
     * @see Page#isTextPresent(String)
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
     * Returns the size of the entire screen.
     * @return the rectangle structure with screen dimensions.
     */
    private static Rectangle getScreenSize() {
        Rectangle area = new Rectangle();
        Dimension size = Driver.current().manage().window().getSize();
        area.setBounds(0, 0, size.getWidth(), size.getHeight());
        return area;
    }

    /**
     * Overloaded version of {@link Page#swipeScreen(boolean, boolean, boolean, int)}
     * where the scrolling time is set to 2 seconds.
     * @param vertical flag indicating if scroll should be vertical. If false,
     *  the scrolling is horizontal.
     * @param leftTop the direction of scrolling. If true, the scroll will be performed to the left or top
     *  depending on the <b>vertical</b> flag.
     * @param once flag identifying whether scrolling should be done once or until the end
     *  of the page is reached (if false).
     * @return true is scrolling was completed. If false, the operation cannot be performed
     *  due to scrollable element unavailability.
     * @see Page#swipeScreen(boolean, boolean, boolean, int)
     */
    public boolean swipeScreen(boolean vertical, boolean leftTop, boolean once) {
        return swipeScreen(vertical, leftTop, once, 2);
    }

    /**
     * <p>
     * Performs scrolling operation which scrolls to the size of visible screen.
     * This is the most common method for such operation and it also controls
     * whether we should perform vertical/horizontal scrolling as well as the
     * direction (left-to-right, top-bottom or vice versa).
     * </p>
     * <p>
     * Additionally it controls the number of swipe operations. Method performs
     * scrolling wither till the end of the page or just once. This is controlled by
     * dedicated flag.
     * </p>
     * <p>
     * <b>NOTE:</b> applicable for Android only.
     * </p>
     * @param vertical flag indicating if scroll should be vertical. If false,
     *  the scrolling is horizontal.
     * @param leftTop the direction of scrolling. If true, the scroll will be performed to the left or top
     *  depending on the <b>vertical</b> flag.
     * @param once flag identifying whether scrolling should be done once or until the end
     *  of the page is reached (if false).
     * @param seconds the number of seconds for scrolling operation.
     * @return true is scrolling was completed. If false, the operation cannot be performed
     *  due to scrollable element unavailability.
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
     * <p>
     * Swipes screen till specific control appears.
     * </p>
     * <p>
     * <b>NOTE:</b> applicable for Android only.
     * </p>
     * @param control the control object to scroll to.
     * @param up flag identifying if scrolling should be done into top.
     * @return if true, the control to scroll to is now visible on the screen. False - otherwise.
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
     * <p>
     * Swipes screen to the upper/lower limit of the page.
     * </p>
     * <p>
     * <b>NOTE:</b> applicable for Android only.
     * </p>
     * @param up flag identifying if scrolling should be performed to the top (if true)
     *  or the bottom (if false) of the page.
     * @return true is scrolling was completed. If false, the operation cannot be performed
     *  due to scrollable element unavailability.
     * @throws Exception any assertion or other exception which can happen during operaiton.
     */
    public boolean scrollTo(boolean up) throws Exception {
        return swipeScreen(true, up, false);
    }

    /**
     * <p>
     * Swipes screen till specific control appears. Unlike {@link Page#scrollTo(Control, boolean)}
     * method, this method defines general trajectory of scrolling. E.g. we may search for specific control
     * either by scrolling only to top/bottom of the screen or we may define that we should swipe to the top
     * first and then to the bottom.
     * </p>
     * <p>
     * <b>NOTE:</b> applicable for Android only.
     * </p>
     * @param control the control object to scroll to.
     * @param scrollDirection general trajectory to search for specified element.
     *  Please, refer to the {@link ScrollTo} enumeration description.
     * @return if true, the control to scroll to is now visible on the screen. False - otherwise.
     * @see ScrollTo
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
     * Overloaded {@link Page#scrollTo(Control, ScrollTo)} method which looks
     * for control by scrolling to the top of the screen first and then to the bottom.
     * @param control the control object to scroll to.
     * @return if true, the control to scroll to is now visible on the screen. False - otherwise.
     */
    public boolean scrollTo(Control control) {
        return scrollTo(control, ScrollTo.TOP_BOTTOM);
    }

    /**
     * <p>
     * Swipes screen till specific text appears.
     * </p>
     * <p>
     * <b>NOTE:</b> applicable for Android only.
     * </p>
     * @param text the text to scroll to.
     * @param up flag identifying if scrolling should be done into top.
     * @return if true, the text to scroll to is now visible on the screen. False - otherwise.
     */
    public boolean scrollTo(String text, boolean up) {
        Control control = this.getTextControl(text);
        return this.scrollTo(control, up);
    }

    /**
     * <p>
     * Swipes screen till specific text appears. Unlike {@link Page#scrollTo(String, boolean)}
     * method, this method defines general trajectory of scrolling. E.g. we may search for specific control
     * either by scrolling only to top/bottom of the screen or we may define that we should swipe to the top
     * first and then to the bottom.
     * </p>
     * <p>
     * <b>NOTE:</b> applicable for Android only.
     * </p>
     * @param text the text to scroll to.
     * @param scrollDirection general trajectory to search for specified element.
     *  Please, refer to the {@link ScrollTo} enumeration description.
     * @return if true, the text to scroll to is now visible on the screen. False - otherwise.
     * @see ScrollTo
     */
    public boolean scrollTo(String text, ScrollTo scrollDirection) {
        Control control = this.getTextControl(text);
        return scrollTo(control, scrollDirection);
    }

    /**
     * Overloaded {@link Page#scrollTo(String, ScrollTo)} method which looks
     * for text by scrolling to the top of the screen first and then to the bottom.
     * @param text the text to scroll to.
     * @return if true, the text to scroll to is now visible on the screen. False - otherwise.
     */
    public boolean scrollTo(String text) {
        return scrollTo(text, ScrollTo.TOP_BOTTOM);
    }

    /**
     * <p>
     * Forcibly hides the virtual keyboard.
     * </p>
     * <p>
     * <b>NOTE:</b> applicable for Android only.
     * </p>
     */
    public void hideKeyboard() {
        try {
            ((AppiumDriver<?>) this.getDriver()).hideKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * @param timeoutValue the timeout to wait for each element to appear.
     * @return true if all searched control on current page object are actually present.
     * @throws Exception mainly related to reflection problems when some control attributes are missing.
     * @see {@link FindBy#excludeFromSearch()}
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
     * Overloaded version of {@link Page#isCurrent(long)} which waits for page during default timeout.
     * @return true if all searched control on current page object are actually present.
     * @throws Exception mainly related to reflection problems when some control attributes are missing.
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
     * Checks whether all elements passed as parameter exist.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean allElementsExist(Control[] elements) throws Exception {
        return allElementsAre(elements, "exists");
    }

    /**
     * Checks whether all elements passed as parameter do not exist.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean allElementsDoNotExist(Control[] elements) throws Exception {
        return allElementsAre(elements, "disappears");
    }

    /**
     * Checks whether all elements passed as parameter are visible.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean allElementsAreVisible(Control[] elements) throws Exception {
        return allElementsAre(elements, "visible");
    }

    /**
     * Checks whether all elements passed as parameter are in invisible state.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean allElementsAreInvisible(Control[] elements) throws Exception {
        return allElementsAre(elements, "invisible");
    }

    /**
     * Checks whether all elements passed as parameter are at enabled state.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean allElementsAreEnabled(Control[] elements) throws Exception {
        return allElementsAre(elements, "enabled");
    }

    /**
     * Checks whether all elements passed as parameter are at disabled state.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean allElementsAreDisabled(Control[] elements) throws Exception {
        return allElementsAre(elements, "disabled");
    }

    /**
     * Checks if at least one of the elements passed as the parameter exists.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean anyOfElementsExist(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "exists");
    }

    /**
     * Checks if at least one of the elements passed as the parameter doesn't exist.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean anyOfElementsDoNotExist(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "disappears");
    }

    /**
     * Checks if at least one of the elements passed as the parameter is visible.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean anyOfElementsIsVisible(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "visible");
    }

    /**
     * Checks if at least one of the elements passed as the parameter is invisible.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean anyOfElementsIsInvisible(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "invisible");
    }

    /**
     * Checks if at least one of the elements passed as the parameter is enabled.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean anyOfElementsIsEnabled(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "enabled");
    }

    /**
     * Checks if at least one of the elements passed as the parameter is disabled.
     * @param elements the list of elements to check.
     * @return true if condition is met. False - otherwise.
     * @throws Exception reflection related errors.
     */
    public boolean anyOfElementsIsDisabled(Control[] elements) throws Exception {
        return anyOfElementsIs(elements, "disabled");
    }

    /**
     * <p>
     * Gets the current page class control with logical name specified.
     * </p>
     * <p>
     * Mainly it goes through all fields of current page class and checks
     * only fields which are of {@link Control} class or any extended classes.
     * For each of such control objects the method gets the {@link Alias}
     * annotation and gets it value.
     * </p>
     * <p>
     * If this value equals the name specified as the
     * parameter the corresponding control is returned.
     * </p>
     * @param name the logical name of the control to get from current page object.
     * @return the control corresponding to the logical name passed as the parameter
     *  or <b>null</b> if no such element found.
     * @throws Exception either reflection problems (like access) or missing attributes.
     * @see Alias
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
