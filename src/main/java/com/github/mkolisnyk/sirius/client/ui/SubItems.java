package com.github.mkolisnyk.sirius.client.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Placeholder for multiple {@link SubItem} annotations.
 * @author Mykola Kolisnyk
 */
@Target(ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SubItems {
    /**
     * The list of {@link SubItem} elements.
     * @return list of {@link SubItem} elements.
     */
    SubItem[] value();
}
