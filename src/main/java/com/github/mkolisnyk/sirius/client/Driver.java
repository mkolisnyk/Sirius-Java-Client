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

    public static String getThreadName() {
        return Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
    }

    public static void init(WebDriver driver) throws Exception {
        String threadName = getThreadName();
        driverThreadMap.put(threadName, driver);
    }

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

    public static WebDriver current() {
        String threadName = getThreadName();
        return driverThreadMap.get(threadName);
    }
}
