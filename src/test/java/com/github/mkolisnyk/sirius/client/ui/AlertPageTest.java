package com.github.mkolisnyk.sirius.client.ui;

import static com.github.mkolisnyk.sirius.client.ui.predicates.Actions.click;
import static com.github.mkolisnyk.sirius.client.ui.predicates.Getters.alert;
import static com.github.mkolisnyk.sirius.client.ui.predicates.Getters.parent;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.current;
import static com.github.mkolisnyk.sirius.client.ui.predicates.States.hasText;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.mkolisnyk.sirius.client.Configuration;
import com.github.mkolisnyk.sirius.client.Context;
import com.github.mkolisnyk.sirius.client.Driver;
import com.github.mkolisnyk.sirius.client.bdd.samples.pages.alert.AlertHostPage;
import com.github.mkolisnyk.sirius.client.bdd.samples.pages.frames.OpenNewTabPage;
import com.github.mkolisnyk.sirius.client.ui.predicates.Actions;

public class AlertPageTest {

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
    public void testAlertBoxes() throws Exception {
        AlertHostPage alertHost = PageFactory.init(Driver.current(), AlertHostPage.class);
        alertHost.navigate().verify(current());
        alertHost.buttonAlert.perform(click())
            .get(parent())
            .get(alert()).accept();
        alertHost.buttonConfirmation.perform(click())
            .get(parent())
            .get(alert()).accept();
        alertHost.buttonConfirmation.perform(click())
            .get(parent())
            .get(alert()).dismiss();
        alertHost.buttonPrompt.perform(click())
            .get(parent())
            .get(alert()).prompt("Sample");
        alertHost.buttonPrompt.verify(hasText("Sample"));
    }
    @Test
    public void testDimensionAlert() throws Exception {
        AlertHostPage alertHost = PageFactory.init(Driver.current(), AlertHostPage.class);
        alertHost.navigate().verify(current());
        AlertPage alert = alertHost.buttonDimension.perform(click())
            .get(parent())
            .get(alert());
        alert.accept();
        alert = alertHost.buttonDimension.perform(click(20, 20))
            .get(parent())
            .get(alert());
        String actual = alert.getText();
        alert.accept();
        Assert.assertEquals("20;20", actual);
        alert = alertHost.buttonDimension.perform(click(Direction.LEFT))
                .get(parent())
                .get(alert());
        actual = alert.getText();
        alert.accept();
        System.out.println("Dimensions: " + actual);

        alert = alertHost.buttonDimension.perform(click(Direction.RIGHT))
                .get(parent())
                .get(alert());
        actual = alert.getText();
        alert.accept();
        System.out.println("Dimensions: " + actual);

        alert = alertHost.buttonDimension.perform(click(Direction.TOP))
                .get(parent())
                .get(alert());
        actual = alert.getText();
        alert.accept();
        System.out.println("Dimensions: " + actual);

        alert = alertHost.buttonDimension.perform(click(Direction.BOTTOM))
                .get(parent())
                .get(alert());
        actual = alert.getText();
        alert.accept();
        System.out.println("Dimensions: " + actual);

    }
}
