package com.intetics.test.base;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/04/2020 18:34
 *
 * @author d.velichkevich
 */
@FunctionalInterface
public interface TestableOperation {

    /**
     * A functional interface for using existing methods with operations to test as parameters
     */
    void execute() throws Exception;
}
