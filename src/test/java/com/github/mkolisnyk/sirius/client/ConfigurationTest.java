package com.github.mkolisnyk.sirius.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ConfigurationTest {

    @Test
    public void loadFromResourceTest() throws IOException {
        Configuration.load("src/test/resources/config.properties");
        Assert.assertEquals("com.sample.tests.pages", Configuration.get("pages_package"));
        Configuration.print();
        Assert.assertEquals(Platform.CHROME, Configuration.platform());
        Assert.assertEquals(30, Configuration.timeout());
    }
}
