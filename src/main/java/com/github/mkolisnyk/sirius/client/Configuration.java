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

public final class Configuration {

    private Configuration() {
    }

    private static Properties properties;
    private static String defaultConfigFile = "config.properties";
    public static void reset() {
        properties = null;
        defaultConfigFile = "config.properties";
    }
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
    public static void load() throws IOException {
        load(getDefaultConfigFile());
    }

    public static String getDefaultConfigFile() {
        return defaultConfigFile;
    }
    public static void setDefaultConfigFile(String defaultConfigFileValue) {
        Configuration.defaultConfigFile = defaultConfigFileValue;
    }
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
    public static String get(ConfigurationOption option) {
        return get(option.toString());
    }

    public static void print() {
        for (Entry<Object, Object> entry : properties.entrySet()) {
            System.out.println(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
    }

    public static long timeout() {
        String value = Configuration.get(ConfigurationOption.TIMEOUT);
        if (value == null || value.trim().equals("")) {
            return DateTimeConstants.SECONDS_PER_MINUTE;
        }
        return Long.parseLong(value.trim());
    }

    public static Platform platform() {
        return Platform.fromString(get(ConfigurationOption.PLATFORM));
    }
}
