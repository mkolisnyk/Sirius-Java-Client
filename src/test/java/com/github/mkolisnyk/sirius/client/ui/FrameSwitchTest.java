package com.github.mkolisnyk.sirius.client.ui;

import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.click;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.current;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.mkolisnyk.sirius.client.Configuration;
import com.github.mkolisnyk.sirius.client.Context;
import com.github.mkolisnyk.sirius.client.Driver;
import com.github.mkolisnyk.sirius.client.bdd.samples.pages.frames.OpenNewTabPage;
import com.github.mkolisnyk.sirius.client.bdd.samples.pages.frames.OpenNewWindowPage;

public class FrameSwitchTest {
    @Before
    public void beforeScenario() throws Exception {
        Context.clearCurrent();
        Configuration.load("src/test/resources/config.properties");
        Configuration.print();
        System.setProperty("webdriver.gecko.driver", new File("drivers/geckodriver").getAbsolutePath());
        System.setProperty("webdriver.chrome.driver", new File("drivers/chromedriver").getAbsolutePath());
        Assert.assertTrue("Only web platforms are supported by this test", Configuration.platform().isWeb());
        DesiredCapabilities cap = new DesiredCapabilities();
        Driver.init("", Configuration.platform(), cap);
        Page.setTimeout(Configuration.timeout());
        Page.setDefaultPagesPackage(Configuration.pagesPackage());
    }
    @After
    public void afterScenario() throws Exception {
        Driver.current().quit();
    }

    @Test
    public void testOpenNewTab() throws Exception {
        OpenNewTabPage openNewTab = PageFactory.init(Driver.current(), OpenNewTabPage.class);
        openNewTab.navigate();
        openNewTab.buttonClickHere.perform(click());
        for (String handle : openNewTab.getDriver().getWindowHandles()) {
            System.out.println(handle);
        }
    }
    @Test
    public void testOpenNewWindow() throws Exception {
        OpenNewWindowPage openNewWindowPage = PageFactory.init(Driver.current(), OpenNewWindowPage.class);
        openNewWindowPage.navigate();
        openNewWindowPage.buttonClickHere.perform(click());
        openNewWindowPage.switchToLast().verify(current());
        openNewWindowPage.buttonClickHere.perform(click());
        openNewWindowPage.switchToLast().verify(current());
        String expectedHandle = openNewWindowPage.getDriver().getWindowHandles().iterator().next();
        openNewWindowPage.switchToDefault();
        String currentHandle = openNewWindowPage.getDriver().getWindowHandle();
        Assert.assertEquals(expectedHandle, currentHandle);
    }
}
