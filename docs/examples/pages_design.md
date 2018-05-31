---
title: Pages and Controls Design.
layout: default
---

# Introduction

If you take a look at typical example written using plain WebDriver API you'll notice that the code looks too technical. Even if it's possible to figure out which primitive actions (like clicks, text entries), it's hard to figure out what exactly is happening and what we are interacting with. Here is some typical plain WebDriver example:

``` java
        driver.findElement(By.id("ss")).click();
        driver.findElement(By.id("ss")).clear();
        driver.findElement(By.id("ss")).sendKeys("London");
        driver.findElement(By.cssSelector("i.sb-date-field__chevron.bicon-downchevron")).click();
        driver.findElement(By.xpath("//td[contains(@class, 'c2-day-s-today')]")).click();
        driver.findElement(By.name("sb_travel_purpose")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.id("ss")).click();
```

The biggest difficulty here is understanding the purpose of the elements we interact with as each element is represented using some specific locator which is not that informative.

Also, if you take a look at current sample test code you’ll notice that same locators are explicitly used in multiple places. So, potentially, if UI is changed we’ll have to update all occurrences of such locators which is costly. So, we need to minimise the use of explicit locators.

And finally, some elements can be custom and may reflect standard interaction with some additional operations (just like text field with look-up ability). In order to handle such elements smoothly we could use classes inheritance capabilities which generally brings additional extensibility.

For this purpose the library introduces 2 major entities: pages and controls.

Control here is an object which provides access to actual element wrapping major functionality. 

Pages are containers for control objects where page is represented as the class and each control is represented with the field of this page class.

With such approach the above code sample can be transformed to something like this (NOTE, it's not the actual example but mainly pseudo-code for demonstration purposes):

``` java
		SearchPage searchPage = PageFactory.init(driver, SearchPage.class);
        searchPage.editDestination.setText("London");
        searchPage.buttonDownShevron.click();
        searchPage.buttonTodaysDate.click();
        searchPage.radioBusiness.click();
        SearchResultsPage searchResultsPage = searchPage.buttonSubmit
                                                .click(SearchResultsPage.class);
        searchResultsPage.editDestination.click();
        Assert.assertTrue(searchResultsPage.isTextPresent("London"));
```

Going through above sample we can see that here we expose more context on where we are and which actual elements we interact with. The key thing is that we operate with some names which are closer to logical names.

So, let's take a closer look at the way each page class is defined and used.

# Page classes

Every page class is the container of page elements as well as common abstraction above operations which are applicable to the entire page, not just specific element. Additionally, classes of this group are immediate wrappers above WebDriver object functionality.

Each class representing some page should be extended from the [Page](/Sirius-Java-Client/javadoc/com/github/mkolisnyk/sirius/client/ui/Page.html) class.

Here is how the most primitive page class looks like:

``` java
package com.sample.pages;

import org.openqa.selenium.WebDriver;

import com.github.mkolisnyk.sirius.client.ui.Page;

public class SamplePage extends Page {
    public SamplePage(WebDriver driverValue) {
        super(driverValue);
    }
}
```

Every page class should have constructor which accepts WebDriver object.

# Control fields

# Initialising pages

# Related topics