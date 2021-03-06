---
title: Sirius Java Client Home
layout: default
---

[![Build Status](https://travis-ci.org/mkolisnyk/Sirius-Java-Client.svg?branch=master)](https://travis-ci.org/mkolisnyk/Sirius-Java-Client)

# Sirius Java Client

**The Latest Version:** [![Sirius Java Client](https://maven-badges.herokuapp.com/maven-central/com.github.mkolisnyk/sirius-java-client/badge.svg?style=flat)](http://mvnrepository.com/artifact/com.github.mkolisnyk/sirius-java-client)

## What is it?

Sirius Java client is the library which wraps WebDriver API for different platforms into common programming interface. It provides major abstractions for:

* The container of WebDrivers to handle the driver which runs in current process.
* Configuration object which is global object to store configuration parameters
* Abstractions for pages and controls. In addition to page object model the library provides classes for managing controls. All the pages and controls can be addressed not just as program objects but also by means of logical names assigned.
* Common keywords for Cucumber-JVM. It includes the set of generic keywords which are applicable nearly for every application under test. It can be good addition if you already use Cucumber-JVM for your automation project.

## How to include?

The library is provided as the Jar package which is accessible from Maven:

``` xml
<dependency>
    <groupId>com.github.mkolisnyk</groupId>
    <artifactId>sirius-java-client</artifactId>
    <version>0.0.1</version>
</dependency>
```

or from Gradle:

``` groovy
compile group: 'com.github.mkolisnyk', name: 'sirius-java-client', version: '0.0.1'
```

## Javadoc

Javadoc pages can be found [here](/Sirius-Java-Client/javadoc/)

## Examples

 * [Driver](/Sirius-Java-Client/examples/driver)
 * [Configuration](/Sirius-Java-Client/examples/configuration)
 * Pages and Controls
   * [General way of declaring pages and elements](/Sirius-Java-Client/examples/pages_design)
   * [Locating elements](/Sirius-Java-Client/examples/locating_elements)
   * [Platform-specific identifiers](/Sirius-Java-Client/examples/platform_specific_identifiers)
   * [Dealing with complex controls](/Sirius-Java-Client/examples/complex_controls)
   * [Using custom controls](/Sirius-Java-Client/examples/custom_controls)
   * [Using logical names](/Sirius-Java-Client/examples/logical_names)
   * [Verify page is current](/Sirius-Java-Client/examples/verify_current_page)
   * [Other page-level verifications](/Sirius-Java-Client/examples/other_page_level_verifications)
   * [Control verifications](/Sirius-Java-Client/examples/control_verifications)
 * Cucumber-JVM keywords
   * [Control steps](/Sirius-Java-Client/examples/driver)
   * [Table steps](/Sirius-Java-Client/examples/driver)
   * [Page steps](/Sirius-Java-Client/examples/driver)

## Authors

Myk Kolisnyk (kolesnik.nickolay@gmail.com) 

<a href="http://ua.linkedin.com/pub/mykola-kolisnyk/14/533/903"><img src="http://www.linkedin.com/img/webpromo/btn_profile_bluetxt_80x15.png" width="80" height="15" border="0" alt="View Mykola Kolisnyk's profile on LinkedIn"></a>
<a href="http://plus.google.com/108480514086204589709?prsrc=3" rel="publisher" style="text-decoration:none;">
<img src="http://ssl.gstatic.com/images/icons/gplus-16.png" alt="Google+" style="border:0;width:16px;height:16px;"/></a>