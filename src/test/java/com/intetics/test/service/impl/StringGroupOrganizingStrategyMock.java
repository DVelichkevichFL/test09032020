package com.intetics.test.service.impl;

import com.intetics.test.model.StringGroup;
import com.intetics.test.model.StringGroupMock;
import com.intetics.test.service.StringGroupOrganizingStrategy;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/03/2020 21:41
 *
 * @author d.velichkevich
 */
public class StringGroupOrganizingStrategyMock implements StringGroupOrganizingStrategy {

    @Override
    public StringGroup createRoot() {
        return new StringGroupMock();
    }

    @Override
    public StringGroup create(int level, String token, StringGroup parent) {
        return new StringGroupMock(level, token, parent);
    }
}
