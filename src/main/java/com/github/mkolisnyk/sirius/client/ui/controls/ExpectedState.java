package com.github.mkolisnyk.sirius.client.ui.controls;

/**
 * Common interface for control state predicate.
 * @author Mykola Kolisnyk
 * @param <T> the returning type.
 * @param <R> the parameter type.
 */
public interface ExpectedState<T, R> {
    /**
     * Performs predicate action.
     * @param parameter the parameter to pass.
     * @return any operation value.
     * @throws Exception any exception thrown.
     */
    T apply(R parameter);
    /**
     * Returns the string description explaining the expected state.
     * @param parameter typically that should be the reference to object
     *      where the {@link ExpectedState#apply(Object)} method is using
     *      to get additional information.
     * @return description value.
     */
    String description(R parameter);
}
