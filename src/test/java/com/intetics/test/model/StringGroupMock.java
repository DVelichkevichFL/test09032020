package com.intetics.test.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/03/2020 21:41
 *
 * @author d.velichkevich
 */
public class StringGroupMock extends StringGroup {

    public StringGroupMock() {
        super();
    }

    public StringGroupMock(int groupLevel, String token, StringGroup parentGroup) {
        super(groupLevel, token, parentGroup);
    }

    @Override
    protected Collection<StringGroup> createChildrenGroupsCollection() {
        return new ArrayList<>();
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") StringGroup o) {
        return 0;
    }
}
