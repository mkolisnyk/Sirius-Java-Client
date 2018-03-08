package com.github.mkolisnyk.sirius.client.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.mkolisnyk.sirius.client.Platform;

/**
 * 
 * @author Mykola Kolisnyk
 *
 */
@Target(ElementType.FIELD)
@Repeatable(FindByList.class)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FindBy {
    /**
     * 
     * @return
     */
    String locator();

    /**
     * 
     * @return
     */
    Platform platform() default Platform.ANY;

    /**
     * 
     * @return
     */
    String itemLocator() default "";

    /**
     * 
     * @return
     */
    String scrollTo() default "";

    /**
     * 
     * @return
     */
    ScrollTo scrollDirection() default ScrollTo.TOP_BOTTOM;

    /**
     * 
     * @return
     */
    String format() default "";

    /**
     * 
     * @return
     */
    boolean excludeFromSearch() default false;
}
