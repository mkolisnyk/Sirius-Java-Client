package com.github.mkolisnyk.sirius.client;

/**
 * Some configuration options are frequently used and have fixed predefined names.
 * The purpose of this enumeration is to collect the most commonly used configuration
 * option names.
 * @author Mykola Kolisnyk
 * @see Configuration
 */
public enum ConfigurationOption {
    PLATFORM("platform"),
    TIMEOUT("timeout"),
    PAGES_PACKAGE("pages_package");

    private String value;

    ConfigurationOption(String param) {
        this.value = param;
    }
    /**
     * Converts string into {@link ConfigurationOption} value. If corresponding option
     * wasn't found the null value is returned.
     * @param input the configuration option name represented as the string.
     * @return the {@link ConfigurationOption} value corresponding to the input parameter.
     */
    public static ConfigurationOption fromString(String input) {
        for (ConfigurationOption option : ConfigurationOption.values()) {
            if (option.getValue().equals(input)) {
                return option;
            }
        }
        return null;
    }
    /**
     * Gets the string value associated with the constant.
     * @return the string value associated with the constant.
     */
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return getValue();
    }
}
