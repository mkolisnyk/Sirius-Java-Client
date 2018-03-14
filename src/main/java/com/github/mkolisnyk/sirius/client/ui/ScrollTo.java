package com.github.mkolisnyk.sirius.client.ui;

/**
 * <p>
 * Enumeration which is used for scrolling operations. Mainly, it is involved in the following
 * methods:
 * <ul>
 * <li> {@link Page#scrollTo(com.github.mkolisnyk.sirius.client.ui.controls.Control, ScrollTo)}
 * <li> {@link Page#scrollTo(String, ScrollTo)}
 * </ul>
 * Also, this enumeration is used for {@link FindBy#scrollDirection()} field.
 * </p>
 * <p>
 * Each enumeration value defines the sequence of scroll actions to reach some certain control
 * or text. Thus, the meaning of constants are the following:
 * <ul>
 * <li> TOP_ONLY - scrolling is performed only to the top.
 * <li> BOTTOM_ONLY - scrolling is performed only to the bottom.
 * <li> TOP_BOTTOM - the scrolling is performed to top first, then into the bottom.
 * <li> BOTTOM_TOP - the scrolling is performed to the bottom first, then into the top.
 * </ul>
 * </p>
 * @author Mykola Kolisnyk
 */
public enum ScrollTo {
    TOP_ONLY, BOTTOM_ONLY, TOP_BOTTOM, BOTTOM_TOP
}
