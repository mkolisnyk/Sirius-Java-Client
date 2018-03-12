package com.github.mkolisnyk.sirius.client;

import java.util.HashSet;

/**
 * Enumeration containing the list of all supported platforms.
 * @author Mykola Kolisnyk
 */
public enum Platform {
    CHROME("chrome"),
    FIREFOX("firefox"),
    IE("ie"),
    SAFARI("safari"),
    OPERA("opera"),
    ANDROID_NATIVE("android_native"),
    ANDROID_WEB("android_web"),
    IOS_NATIVE("ios_native"),
    ANY("any");
    private String value;

    Platform(String param) {
        this.value = param;
    }

    /**
     * Gets the string associated with current enumeration value.
     * @return string associated with current enumeration value.
     */
    public String getValue() {
        return value;
    }
    /**
     * Identifies if current platform is Android native.
     * @return true - if current platform is Android native. False - otherwise.
     */
    public boolean isAndroidNative() {
        return this.equals(ANDROID_NATIVE);
    }

    /**
     * Identifies if current platform is iOS native.
     * @return true - if current platform is iOS native. False - otherwise.
     */
    public boolean isIOSNative() {
        return this.equals(IOS_NATIVE);
    }

    /**
     * Identifies if current platform belongs to mobile platforms.
     * @return True - if current platform either Android or iOS. False - otherwise.
     */
    public boolean isMobile() {
        return new HashSet<Platform>() {
            private static final long serialVersionUID = 1L;

            {
                add(ANDROID_NATIVE);
                add(ANDROID_WEB);
                add(IOS_NATIVE);
            }
        }.contains(this);
    }

    /**
     * Identifies if current platform belongs to the group of web platforms.
     * Mobile web platforms are also qualified.
     * @return True - current platform belongs to the group of web platforms. False - otherwise.
     */
    public boolean isWeb() {
        return new HashSet<Platform>() {
            private static final long serialVersionUID = 1L;

            {
                add(CHROME);
                add(FIREFOX);
                add(IE);
                add(SAFARI);
                add(OPERA);
                add(ANDROID_WEB);
                add(ANY);
            }
        }.contains(this);
    }

    /**
     * Gets the platform constant based on the string passed.
     * @param input the string representing platform to look for.
     * @return platform constant matching input string. Otherwise, Platform.ANY is returned.
     */
    public static Platform fromString(String input) {
        for (Platform platform : Platform.values()) {
            if (platform.getValue().equals(input)) {
                return platform;
            }
        }
        return Platform.ANY;
    }
}
