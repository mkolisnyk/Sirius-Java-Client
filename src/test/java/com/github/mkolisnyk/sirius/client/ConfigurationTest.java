package com.github.mkolisnyk.sirius.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ConfigurationTest {

    @Before
    public void setUp() {
        Configuration.reset();
    }

    @Test
    public void loadFromResourceTest() throws IOException {
        Configuration.load("src/test/resources/config.properties");
        Assert.assertEquals("com.sample.tests.pages", Configuration.get("pages_package"));
        Configuration.print();
        Assert.assertEquals(Platform.CHROME, Configuration.platform());
        Assert.assertEquals(30, Configuration.timeout());
    }
    @Test
    public void getDefaultLocation() {
        Assert.assertEquals("config.properties", Configuration.getDefaultConfigFile());
    }
    @Test
    public void loadFromDefaultLocation() throws IOException {
        Configuration.setDefaultConfigFile("src/test/resources/config.properties");
        Configuration.load();
        Assert.assertEquals("com.sample.tests.pages", Configuration.get("pages_package"));
        Configuration.print();
        Assert.assertEquals(Platform.CHROME, Configuration.platform());
        Assert.assertEquals(30, Configuration.timeout());
    }
    @Test
    public void getWithoutInitialLoad() {
        Configuration.setDefaultConfigFile("src/test/resources/config.properties");
        Assert.assertEquals("com.sample.tests.pages", Configuration.get("pages_package"));
        Assert.assertEquals(Platform.CHROME, Configuration.platform());
        Assert.assertEquals(30, Configuration.timeout());
        Configuration.print();
    }
    @Test
    public void getNonExistingValueShouldReturnEmptyString() {
        Configuration.setDefaultConfigFile("src/test/resources/config.properties");
        Assert.assertEquals("", Configuration.get("undefined"));
    }
    @Test
    public void getDefaultPredefinedConfigurations() {
        Configuration.setDefaultConfigFile("src/test/resources/empty_config.properties");
        Assert.assertEquals(60, Configuration.timeout());
        Assert.assertEquals(Platform.ANY, Configuration.platform());
    }
}
