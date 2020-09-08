package com.intetics.test.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.assertj.core.api.Assertions;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/03/2020 21:44
 *
 * @author d.velichkevich
 */
public class TestUtils {

    private static List<Object> actualActions;


    /**
     * Resets the actual collection of actions
     */
    public static void reset() {
        actualActions = new ArrayList<>();
    }

    /**
     * Adds actual actions of the code execution
     *
     *  @param actions {@link Object}... collection of actual actions
     */
    public static void addActions(Object... actions) {
        Collections.addAll(actualActions, actions);
    }

    /**
     * Checks whether expected actions are equal to the actual actions
     *
     * @param expectedActions {@link Object}... collection of actual actions
     */
    public static void checkActions(Object... expectedActions) {
        if (actualActions.size() != expectedActions.length) {
            Assertions.fail(
                    "Expected actions size is not equal to the actual actions size! Expected: "
                            + expectedActions.length + ", actual: " + actualActions.size());
        }

        Iterator<Object> iterator = actualActions.iterator();
        for (int i = 0; i < expectedActions.length; i++) {
            Object expectedAction = expectedActions[i];
            Object actualAction = iterator.next();

            if ((null != expectedAction)
                    ? (expectedAction.equals(actualAction)) : (null == actualAction)) {

                continue;
            }

            Assertions.fail(
                    "Expected action #" + i
                            + " is not equal to the corresponding actual action! Expected: "
                            + expectedAction + ", actual: " + actualAction);
        }
    }
}
