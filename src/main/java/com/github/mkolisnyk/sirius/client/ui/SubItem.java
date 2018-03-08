package com.github.mkolisnyk.sirius.client.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.mkolisnyk.sirius.client.Platform;
import com.github.mkolisnyk.sirius.client.ui.controls.Control;

@Target(ElementType.FIELD)
@Repeatable(SubItems.class)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SubItem {
    String name();

    String locator();

    Platform platform() default Platform.ANY;

    Class<? extends Control> controlType() default Control.class;
}
