package com.github.mkolisnyk.sirius.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Properties;

import org.joda.time.DateTimeConstants;

/**
 * <p>
 * Global object for configuration data storage.
 * Configuration data is supposed to be stored in the properties file.
 * By default the configuration is taken from the <b>config.properties</b> file
 * which should be located either in the root project folder or in the resource
 * root. If there's a need to read configuration from different location it is
 * recommended to call {@link Configuration#setDefaultConfigFile(String)} method
 * before any other calls to other configuration methods.
 * </p>
 * <p>
 * Sample use:
 * <pre>
 * Configuration.load(); // Loads configuration options
 * String value = Configuration.get("config_property"); // Gets <b>config_property</b> property value
 * </pre>
 * For more information read methods description.
 * </p>
 * @author Mykola Kolisnyk
 *
 */
public final class Configuration {

    private Configuration() {
    }

    private static Properties properties;
    private static String defaultConfigFile = "config.properties";

    /**
     * Restores the internal data to initial state when the list of properties
     * is null and the default configuration path is <b>config.properties</b>.
     */
    public static void reset() {
        properties = null;
        defaultConfigFile = "config.properties";
    }
    /**
     * <p>
     * Loads properties from the file specified by the location. The location may refer
     * either to file path (either absolute or relative) or to resource path in case the
     * configuration path is provided as the resource.
     * </p>
     * <p>
     * <b>NOTE:</b> if both external file and resource are available by the path specified by
     * location parameter, the external file is read.
     * </p>
     * @param location path to the properties file which can be either external file or internal resource.
     * @throws IOException invoked in case of any problems with reading file.
     */
    public static void load(String location) throws IOException {
        properties = new Properties();
        File configFile = new File(location);
        InputStream is = null;
        if (configFile.exists()) {
            is = new FileInputStream(configFile);
        } else {
            is = Configuration.class.getResourceAsStream(location);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        try {
            properties.load(reader);
        } finally {
            is.close();
            reader.close();
        }
    }
    /**
     * <p>
     * Overloaded version of the {@link Configuration#load(String)} method where the default
     * configuration location is used. Without any additional settings that would be <b>config.properties</b>
     * file. If any other default location is needed, it is recommended to call
     * {@link Configuration#setDefaultConfigFile(String)} method first.
     * </p>
     * @throws IOException invoked in case of any problems with reading file.
     * @see Configuration#setDefaultConfigFile(String)
     * @see Configuration#getDefaultConfigFile()
     * @see Configuration#load(String)
     */
    public static void load() throws IOException {
        load(getDefaultConfigFile());
    }

    /**
     * Returns the default location of the configuration file to read data from.
     * @return default configuration file/resource location.
     */
    public static String getDefaultConfigFile() {
        return defaultConfigFile;
    }
    /**
     * <p>
     * Sets the default location for the configuration file. If custom configuration location
     * is used it is recommended to invoke this method first.
     * </p>
     * @param defaultConfigFileValue New default configuration file/ resource location.
     */
    public static void setDefaultConfigFile(String defaultConfigFileValue) {
        Configuration.defaultConfigFile = defaultConfigFileValue;
    }
    /**
     * <p>
     * Gets the value of the configuration option specified by the parameter.
     * </p>
     * <p>
     * If configuration hasn't been loaded yet the {@link Configuration#load()} method
     * will be forcibly invoked. It means that by default the {@link Configuration} object
     * invokes configuration load from the default location.
     * </p>
     * @param option the configuration option name to get value for. Name is represented as the string.
     * @return the configuration option value.
     */
    public static String get(String option) {
        if (properties == null) {
            try {
                load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String value = properties.getProperty(option);
        if (value == null) {
            return "";
        }
        return value;
    }
    /**
     * Gets value for the predefined configuration option which is defined as
     * {@link ConfigurationOption} enumeration. Such version is needed for
     * frequently used configuration options which have dedicated constant.
     * @param option the configuration option code to get value for.
     * @return the value of configuration parameter.
     * @see ConfigurationOption
     */
    public static String get(ConfigurationOption option) {
        return get(option.toString());
    }

    /**
     * Prints all available configuration options with their values into standard output stream.
     */
    public static void print() {
        for (Entry<Object, Object> entry : properties.entrySet()) {
            System.out.println(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Returns timeout value based on {@link ConfigurationOption#TIMEOUT} parameter.
     * If such option is unavailable the default value of 60 seconds is returned.
     * Mainly this method can be used together with WebDriver waiting predicates
     * as the default explicit timeout.
     * @return the timeout value.
     */
    public static long timeout() {
        String value = Configuration.get(ConfigurationOption.TIMEOUT);
        if (value == null || value.trim().equals("")) {
            return DateTimeConstants.SECONDS_PER_MINUTE;
        }
        return Long.parseLong(value.trim());
    }

    /**
     * Retrieves configuration option value for currently defined platform.
     * @return platform name defined in configuration.
     * @see Platform
     */
    public static Platform platform() {
        return Platform.fromString(get(ConfigurationOption.PLATFORM));
    }
    /**
     * Retrieves the value of pages package. It is mainly needed for page related
     * classes which initialize page objects.
     * @return the package name which contains page classes.
     */
    public static String pagesPackage() {
        return get(ConfigurationOption.PAGES_PACKAGE);
    }
}
