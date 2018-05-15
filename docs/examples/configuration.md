---
title: Configuration Object Examples.
layout: default
---

# What is this?

Configuration object is the centralized global container for common settings.

# Where it is used?

Mainly it is used to store environment-specific characteristics like:

* Platform/browser in use
* Command timeout
* Various application URLs/paths
* Other settings

Major difference from any other global variables is that all those characteristics are read from external file (or resource) and mainly aren't supposed to be overridden.

# Usage

## Loading properties

``` java
Configuration.load("src/test/resources/config.properties");
Configuration.print();
```

## Getting property value

``` java
Configuration.get("some_property");
```

## Pre-defined properties

``` java 
Assert.assertTrue("Only web platforms are supported by this test", Configuration.platform().isWeb());
DesiredCapabilities cap = new DesiredCapabilities();
Driver.init("", Configuration.platform(), cap);
Page.setTimeout(Configuration.timeout());
```

# Related topics