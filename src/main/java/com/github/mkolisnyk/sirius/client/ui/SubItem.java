package com.github.mkolisnyk.sirius.client.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.mkolisnyk.sirius.client.Platform;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

/**
 * Annotation which is used for complex elements processing.
 * @author Mykola Kolisnyk
 */
@Target(ElementType.FIELD)
@Repeatable(SubItems.class)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SubItem {
    /**
     * The sub-item name.
     * @return The sub-item name.
     */
    String name();
    /**
     * Locator string for sub-item. It is the part of the entire locator.
     * @return Locator string for sub-item.
     */
    String locator();
    /**
     * The flag identifying target platform.
     * @return target platform.
     */
    Platform platform() default Platform.ANY;
    /**
     * The class of the control object to cast sub-item element to.
     * @return the sub-item control type.
     */
    Class<? extends Control> controlType() default Control.class;
}
