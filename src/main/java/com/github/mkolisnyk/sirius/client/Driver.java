package com.github.mkolisnyk.sirius.client;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

/**
 * <p>
 * Global object for initializing and storing WebDriver objects.
 * It provides functionality which defines the WebDriver instance for current thread
 * which can be used for parallel execution.
 * </p>
 * @author Mykola Kolisnyk
 */
public final class Driver {

    private Driver() {
    }

    private static ConcurrentHashMap<String, WebDriver> driverThreadMap = new ConcurrentHashMap<String, WebDriver>();
    private static final Map<String, Class<?>> DRIVER_MAP = new HashMap<String, Class<?>>() {
        private static final long serialVersionUID = 1L;

        {
            put(Platform.CHROME.getValue(), ChromeDriver.class);
            put(Platform.FIREFOX.getValue(), FirefoxDriver.class);
            put(Platform.IE.getValue(), InternetExplorerDriver.class);
            put(Platform.SAFARI.getValue(), SafariDriver.class);
            put(Platform.OPERA.getValue(), OperaDriver.class);
            put(Platform.ANDROID_NATIVE.getValue(), AndroidDriver.class);
            put(Platform.ANDROID_WEB.getValue(), AndroidDriver.class);
            put(Platform.IOS_NATIVE.getValue(), IOSDriver.class);
        }
    };

    /**
     * Generates string which corresponds to current thream. It includes the thread and and id.
     * @return string which represents current thread name.
     */
    public static String getThreadName() {
        return Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
    }

    /**
     * Puts specified WebDriver instance into internal storage.
     * @param driver the WebDriver instance to store.
     */
    public static void init(WebDriver driver) {
        String threadName = getThreadName();
        driverThreadMap.put(threadName, driver);
    }

    /**
     * Initializes WebDriver instance and puts it into internal storage based on URL, platform
     * and capabilities. This method is common wrapper above supported WebDriver implementations.
     * URL is notmally needed for remote WebDriver instances.
     * @param url the remote WebDriver URL. Will be ignored for standard web platforms.
     * @param platform the target platform.
     * @param capabilities the set of capabilities.
     * @return initialized WebDriver instance based on the input parameters.
     * @throws Exception any exception which may appear during WebDriver instance initialization.
     */
    public static WebDriver init(String url, Platform platform, Capabilities capabilities) throws Exception {
        WebDriver driver;
        if (platform.isMobile()) {
            driver = (WebDriver) DRIVER_MAP.get(platform.getValue()).getConstructor(URL.class, Capabilities.class)
                    .newInstance(new URL(url), capabilities);
        } else {
            driver = (WebDriver) DRIVER_MAP.get(platform.getValue()).getConstructor(Capabilities.class)
                    .newInstance(capabilities);
        }
        init(driver);
        return driver;
    }

    /**
     * Returns the WebDriver instance for current thread.
     * @return currently used WebDriver instance.
     */
    public static WebDriver current() {
        String threadName = getThreadName();
        return driverThreadMap.get(threadName);
    }
}
