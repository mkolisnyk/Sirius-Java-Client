package com.github.mkolisnyk.sirius.client.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.mkolisnyk.sirius.client.Platform;
import com.github.mkolisnyk.sirius.client.ui.controls.TableView;

/**
 * <p>
 * Major annotation assigned to page controls. It contains multiple attributes
 * which help identifying object on the page as well as defines the way to process each
 * specific control element.
 * </p>
 * <p>
 * This annotation can be assigned to the same Control object multiple times. Major difference
 * is related to the platform each specific version of the locator is applied for.
 * </p>
 * @author Mykola Kolisnyk
 */
@Target(ElementType.FIELD)
@Repeatable(FindByList.class)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FindBy {
    /**
     * String which is normally used to identify the way the element can be found on the page.
     * @return string representing element locator.
     */
    String locator();

    /**
     * The field responsible for platform definition. It can be used to filter the the record which
     * is applicable for current platform.
     * @return the platform current annotation is applicable for.
     */
    Platform platform() default Platform.ANY;

    /**
     * The part of sub-element identifier. Used for compound objects definition.
     * Mainly this functionality is used with compound control classes like {@link TableView}
     * @return sub-element identifier.
     */
    String itemLocator() default "";

    /**
     * The text to scroll to in case current element isn't found on existing screen.
     * Mainly, it is needed for Android and any other WebDriver implementation when
     * elements are seen only if they are on current screen.
     * @return text to scroll to.
     */
    String scrollTo() default "";

    /**
     * Defines the direction of search for {@link FindBy#scrollTo()} text value.
     * Used in combination with {@link FindBy#scrollTo()} and defines the sequence
     * to scroll (top-bottom, bottom-top, top-only, bottom-only).
     * @return the direction of search for {@link FindBy#scrollTo()} text value.
     */
    ScrollTo scrollDirection() default ScrollTo.TOP_BOTTOM;

    /**
     * Additional attribute which is responsible for various format transformations.
     * Typically, it is used for custom controls where the value is represented in some custom format.
     * @return the format string.
     */
    String format() default "";

    /**
     * Defines whether current element is supposed to be checked while verifying if current
     * page class represents the page which is currently available on the screen.
     * @return exclude from search flag.
     */
    boolean excludeFromSearch() default false;
}
