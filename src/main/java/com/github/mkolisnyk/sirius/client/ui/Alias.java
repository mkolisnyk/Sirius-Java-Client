package com.github.mkolisnyk.sirius.client.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which is supposed to be applied to page classes or their fields.
 * It assigns logical name to the page object or it's field.
 * @author Mykola Kolisnyk
 *
 */
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Alias {
    /**
     * The logical name to be assigned to the page/field.
     * @return logical name to be assigned to the page/field.
     */
    String value();
    /**
     * Optional parameter which is responsible for element index.
     * Typically it is used for pages or frames which are normally located
     * either by id or index.
     * @return element/page index
     */
    int index() default -1;
    /**
     * Optional parameter which is responsible for element id.
     * Typically it is used for pages or frames which are normally located
     * either by id or index.
     * @return element/page id
     */
    String id() default "";
}
