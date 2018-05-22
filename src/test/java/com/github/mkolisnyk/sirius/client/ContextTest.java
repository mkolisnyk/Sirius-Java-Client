package com.github.mkolisnyk.sirius.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ContextTest {

    @Before
    public void setUp() {
        Context.reset();
    }

    @Test
    public void testPutIntoClearContext() {
        Context.put("test", "value");
        Assert.assertNotNull(Context.get("test"));
    }
    @Test
    public void testGetWithClearContext() {
        Assert.assertNull(Context.get("test"));
    }
    @Test
    public void testGetNonExistingItem() {
        Context.put("test", "value");
        Assert.assertNull(Context.get("utest"));
    }

}
