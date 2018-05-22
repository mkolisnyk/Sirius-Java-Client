package com.github.mkolisnyk.sirius.client.ui.predicates;

/**
 * .
 * @author Mykola Kolisnyk
 *
 * @param <T> .
 * @param <R> .
 */
public abstract class NonDescriptive<T, R> implements Operation<T, R> {
    @Override
    public String description(R parameter) {
        return null;
    }
}
