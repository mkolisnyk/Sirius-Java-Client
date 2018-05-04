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
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Alias {
    /**
     * The logical name to be assigned to the page/field.
     * @return logical name to be assigned to the page/field.
     */
    String value();
    int index() default -1;
    String id() default "";
}
