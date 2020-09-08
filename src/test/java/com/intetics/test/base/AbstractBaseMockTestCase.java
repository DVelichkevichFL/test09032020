package com.intetics.test.base;

import com.intetics.test.service.io.ResourceService;
import org.junit.Rule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/04/2020 1:55
 *
 * @author d.velichkevich
 */
@ExtendWith(MockitoExtension.class)
public abstract class AbstractBaseMockTestCase extends AbstractBaseTestCase {

    protected static final String TEST_DEFAULT_STRATEGY = "plain";

    protected static final String TEST_SOURCE_STRING = "\"(id,created,employee(id,firstname,employeeType(id), lastname),location)\"";

    protected static final String TEST_ERRONEOUS_SOURCE_STRING = "test(test(test)test,test)";


    @Rule
    private MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ResourceService resourceServiceMock;


    // Getters And Setters

    public ResourceService getResourceServiceMock() {
        return resourceServiceMock;
    }
}
