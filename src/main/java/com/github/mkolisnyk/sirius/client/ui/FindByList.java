package com.github.mkolisnyk.sirius.client.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Placeholder for multiple {@link FindBy} annotations.
 * @author Mykola Kolisnyk
 */
@Target(ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FindByList {
    /**
     * List of multiple {@link FindBy} annotations.
     * @return list of multiple {@link FindBy} annotations.
     */
    FindBy[] value();
}
