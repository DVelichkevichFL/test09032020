package com.intetics.test.base;

import com.intetics.test.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/03/2020 21:04
 *
 * @author d.velichkevich
 */
@RunWith(JUnitPlatform.class)
public abstract class AbstractBaseTestCase extends Assertions {

    @BeforeEach
    public void setUp() throws Exception {
        TestUtils.reset();
    }

    /**
     * Adds actual actions of the code execution
     *
     *  @param actions {@link Object}... collection of actual actions
     */
    protected void addActions(Object... actions) {
        TestUtils.addActions(actions);
    }

    /**
     * Checks whether expected actions are equal to the actual actions
     *
     * @param expectedActions {@link Object}... collection of actual actions
     */
    protected void checkActions(Object... expectedActions) {
        TestUtils.checkActions(expectedActions);
    }
}
