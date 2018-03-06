package com.github.mkolisnyk.sirius.client;

public enum ConfigurationOption {
    PLATFORM("platform"),
    TIMEOUT("timeout"),
    PAGES_PACKAGE("pages_package");

    private String value;

    ConfigurationOption(String param) {
        this.value = param;
    }
    public static ConfigurationOption fromString(String input) {
        for (ConfigurationOption option : ConfigurationOption.values()) {
            if (option.getValue().equals(input)) {
                return option;
            }
        }
        return null;
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return getValue();
    }
}
