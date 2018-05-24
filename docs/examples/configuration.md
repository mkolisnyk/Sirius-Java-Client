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

Typically Configuration object is needed for the following groups of operations:

* Loading properties
* Getting property value
* Setting/getting basic attributes

## Loading properties

Properties are retrieved from the text file which contains the list of "property-value" pairs and each line has the format:

```
<property name>=<property value>
```

### Loading properties from default location

By default, the configuration properties are retrieved from the **config.properties** file which is either located in the project root folder or is provided as the resource. In this case, typical properties initialisation looks like:

``` java
Configuration.load();
```

### Customising properties location

The library provides an ability to load properties from any different location. Again, location should refer either to the file on local system or resource file. Here is an example of loading properties from non-default location:

``` java
Configuration.load("src/test/resources/custom.properties");
```

Alternatively, there is possibility to define the default location of the properties file. It may be needed if the actual location of configuration source is controlled externally via environment variables, system properties or so. The above code is completely identical to previous example:

``` java
Configuration.setDefaultConfigFile("src/test/resources/custom.properties");
Configuration.load();
```

### Restoring initial state

Additionally, if we need to switch back to the default settings there is possibility to reset configuration data:

``` java
Configuration.reset();
```

## Getting property value

Each specific property can be retrieved by it's name using the following code:

``` java
Configuration.get("some_property");
```

If, for any reason, properties were not initialised, they will be forcibly loaded.

## Pre-defined properties

Some of the properties can be quite frequent and widely used. Also, they can be used by other components of the library. For such properties there are dedicated methods retrieving just that properties. In particular, the following properties have dedicated methods:

* timeout - the time limit for waiting for element to have some state before throwing error.
* platform - defines which specific platform is used (either browser or mobile system).
* pages_package - mainly needed for Page class to filter packages which are actually page objects.

Here is the code sample where some of the predefined configuration options are used:

``` java 
Assert.assertTrue("Only web platforms are supported by this test",
	Configuration.platform().isWeb());
DesiredCapabilities cap = new DesiredCapabilities();
Driver.init("", Configuration.platform(), cap);
Page.setTimeout(Configuration.timeout());
```

# Related topics