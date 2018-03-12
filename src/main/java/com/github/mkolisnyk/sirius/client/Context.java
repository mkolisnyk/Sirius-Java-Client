package com.github.mkolisnyk.sirius.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * Global object which stores context variables. They are represented as an association between
 * logical name (represented with the string) and actual value. It is mainly needed for data
 * transfer between steps while using BDD instructions.
 * </p>
 * <p>
 * Additionally this object handles multiple threads. So, if some tests are running in parallel
 * threads, the context variables will not be overridden. Instead they will be stored in the
 * separate storage associated with dedicated thread.
 * </p>
 * @author Mykola Kolisnyk
 */
public final class Context {

    private Context() {
    }

    private static ConcurrentHashMap<String, Map<String, Object>> contextVariables
        = new ConcurrentHashMap<String, Map<String, Object>>();
    private static String getThreadName() {
        return Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
    }
    /**
     * Assigns new value to the variable defined with the <b>name</b> parameter.
     * @param name the variable name to assign.
     * @param value the value to assign.
     */
    public static void put(String name, Object value) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String threadName = getThreadName();
        if (contextVariables.containsKey(threadName)) {
            dataMap = contextVariables.get(threadName);
        }
        dataMap.put(name, value);
        contextVariables.put(threadName, dataMap);
    }
    /**
     * Gets the value of the variable specified by <b>name</b> parameter
     * or null if no such variable available.
     * @param name the name of variable to get value of.
     * @return the value of the variable specified.
     */
    public static Object get(String name) {
        String threadName = getThreadName();
        if (contextVariables.containsKey(threadName)) {
            return contextVariables.get(threadName).get(name);
        }
        return null;
    }
    /**
     * Clears all variables data for current thread.
     */
    public static void clearCurrent() {
        contextVariables.put(getThreadName(), new HashMap<String, Object>());
    }
    /**
     * Lists all available variables for current thread.
     * @return the set of variables available for current thread.
     */
    public static Set<String> variables() {
        return contextVariables.get(getThreadName()).keySet();
    }
}
