package com.github.mkolisnyk.sirius.client.ui;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.Configuration;
import com.github.mkolisnyk.sirius.client.bdd.samples.pages.banking.HomePage;
import com.github.mkolisnyk.sirius.client.mocks.MockWebDriver;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Before;

public class PageFactoryTest {

    @Alias("Plain Page")
    public static class SamplePlainPage extends Page {

        public SamplePlainPage(WebDriver driverValue) {
            super(driverValue);
        }

        public int nonControlField;

        @FindBy(locator = "name=test")
        public Control labelSample;
    }
    @Alias("Plain Page")
    public static class MultilevelPage extends Page {

        public MultilevelPage(WebDriver driverValue) {
            super(driverValue);
        }

        @Alias("Nested")
        public class SubPage extends Page {

            public SubPage(WebDriver driverValue) {
                super(driverValue);
            }
            @FindBy(locator = "css=test")
            public Control labelSubPageSample;
        }
        
        @FindBy(locator = "name=test")
        public Control labelSample;
        
        @Alias("Sub-Page")
        public SubPage subPage;
    }
    @Before
    public void setUp() throws IOException {
        Configuration.setDefaultConfigFile("src/test/resources/config.properties");
        Configuration.load();
    }
    
    @Test
    public void testPageInitialization() throws Exception {
        SamplePlainPage sample = PageFactory.init(new MockWebDriver(), SamplePlainPage.class);
        Assert.assertNotNull(sample);
        Assert.assertNotNull(sample.labelSample);
        Assert.assertNotNull(sample.labelSample.getLocator());
        Assert.assertNotNull(sample.nonControlField);
    }

    @Test
    public void testMultiLevelPagesWithInstance() throws Exception {
        MultilevelPage sample = PageFactory.init(new MockWebDriver(), MultilevelPage.class);
        Assert.assertNotNull(sample);
        Assert.assertNotNull(sample.labelSample);
        Assert.assertNotNull(sample.labelSample.getLocator());
        Assert.assertNotNull(sample.subPage);
        Assert.assertNotNull(sample.subPage.getDriver());
        Assert.assertNotNull(sample.subPage.labelSubPageSample);
    }
    @Test
    public void testSearchForNestedPage() throws Exception {
        MultilevelPage sample = PageFactory.init(new MockWebDriver(), MultilevelPage.class);
        Assert.assertNotNull(sample);
        Assert.assertNotNull(sample.section("Sub-Page"));
        Assert.assertNull(Page.forName("Nested"));
    }
    @Test
    public void testInitNestedPageLevel2() throws Exception {
        HomePage home = PageFactory.init(new MockWebDriver(), HomePage.class);
        Assert.assertNotNull(home);
        MultilevelPage multi = PageFactory.init(new MockWebDriver(), MultilevelPage.class);
        Assert.assertNotNull(multi);
        MultilevelPage.SubPage page = PageFactory.init(new MockWebDriver(), MultilevelPage.SubPage.class);
        Assert.assertNotNull(page);
    }
}
