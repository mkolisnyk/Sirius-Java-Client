---
title: Driver Object Examples.
layout: default
---

# What is this?

Driver object is the global object which is responsible for handling multiple WebDriver instances. Also, it is responsible for WebDriver initialisation and parallel execution handling.

# Where it is used?

The entire library is the wrapper above the WebDriver object. And it is main object to access actual WebDriver instance.

Additionally it is used for parallel execution. Each actual WebDriver instance is stored in internal table of the Driver object and it is accessible by the key which corresponds to current thread. So, there is possibility to get WebDriver instance which was previously initialised in this thread.

# Usage

Mainly, the Driver object is responsible for 2 major operations:

* Initialise instance
* Get instance for current thread.

## Initialise instance

There are several ways to add the WebDriver instance to the Driver object for further use.
It can be either initialised externally and passed as ready instance or it can be initialised based on platform information taken from the configuration file.

### Explicit initialisation

Explicit initialisation is needed when user should be of full control of the WebDriver instance creation and needs to perform something more specific than just specific capabilities setting.

Normally, it may be related to custom implementation of the WebDriver. Here is an example of adding new ChromeDriver instance:

``` java
WebDriver driver = new ChromeDriver();
Driver.init(driver);
```

### Platform-based initialisation

Driver object itself provides default initialisation based on configuration parameters taken from the configuration options.

``` java
        Configuration.load();
        System.setProperty("webdriver.gecko.driver", new File("drivers/geckodriver").getAbsolutePath());
        System.setProperty("webdriver.chrome.driver", new File("drivers/chromedriver").getAbsolutePath());
        DesiredCapabilities cap = new DesiredCapabilities();
        Driver.init("", Configuration.platform(), cap);
```

## Get instance for current thread

``` java
Driver.current().quit();
```

# Related topics