package com.github.mkolisnyk.sirius.client;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ConfigurationOptionTest {

    private String name;
    private ConfigurationOption expected;

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {"platform", ConfigurationOption.PLATFORM},
                {"timeout", ConfigurationOption.TIMEOUT},
                {"pages_package", ConfigurationOption.PAGES_PACKAGE},
                {"unknown", null},
        });
    }
    
    public ConfigurationOptionTest(String name, ConfigurationOption expected) {
        super();
        this.name = name;
        this.expected = expected;
    }


    @Test
    public void testFromString() {
        Assert.assertEquals(expected, ConfigurationOption.fromString(name));
    }
}
