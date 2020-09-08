package com.intetics.test.service.impl;

import com.intetics.test.model.StringGroup;
import com.intetics.test.service.StringGroupOrganizingStrategy;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/02/2020 22:27
 *
 * @author d.velichkevich
 */
public class DefaultStringGroupOrganizingStrategy implements StringGroupOrganizingStrategy {

    @Override
    public StringGroup createRoot() {
        return new StringGroup();
    }

    @Override
    public StringGroup create(int level, String token, StringGroup parent) {
        return new StringGroup(level, token, parent);
    }
}
