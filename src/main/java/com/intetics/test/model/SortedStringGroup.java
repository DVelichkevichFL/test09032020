package com.intetics.test.model;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/02/2020 18:48
 *
 * @author d.velichkevich
 */
public class SortedStringGroup extends StringGroup {

    public SortedStringGroup() {
        super();
    }

    public SortedStringGroup(int groupLevel, String token, StringGroup parentGroup) {
        super(groupLevel, token, parentGroup);
    }

    @Override
    protected Collection<StringGroup> createChildrenGroupsCollection() {
        return new TreeSet<>();
    }
}
