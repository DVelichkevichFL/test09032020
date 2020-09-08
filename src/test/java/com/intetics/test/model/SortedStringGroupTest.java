package com.intetics.test.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/03/2020 19:45
 *
 * @author d.velichkevich
 */
public class SortedStringGroupTest extends StringGroupTest {

    @Override
    protected StringGroup createRootGroup() {
        return new SortedStringGroup();
    }

    @Override
    protected StringGroup createGroupMock() {
        return new SortedStringGroup(1, "TEST_TOKEN", getParentGroup()) {

            @Override
            protected Collection<StringGroup> createChildrenGroupsCollection() {
                addActions("StringGroup* -> createChildrenGroupsCollection");

                return new HashSet<>();
            }
        };
    }

    @Override
    protected StringGroup createRootGroupMock() {
        return new SortedStringGroup() {

            @Override
            protected Collection<StringGroup> createChildrenGroupsCollection() {
                addActions("StringGroup* -> createChildrenGroupsCollection");

                return new HashSet<>();
            }
        };
    }

    @Override
    protected Class<? extends Collection> getExpectedChildrenGroupsCollectionClass() {
        return TreeSet.class;
    }

    @Override
    protected StringGroup createGroup(int level, String groupName, StringGroup parent) {
        return new SortedStringGroup(level, groupName, parent);
    }
}
