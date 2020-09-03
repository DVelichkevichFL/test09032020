package com.intetics.test.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/01/2020 19:44
 *
 * @author d.velichkevich
 */
public class StringGroup implements Comparable<StringGroup> {

    private static final int LEVEL_ROOT_GROUP = -1;

    private static final String NAME_ROOT_GROUP = "<ROOT_GROUP_NAME>";



    private int groupLevel;

    private String token;

    private StringGroup parentGroup;

    private Collection<StringGroup> childrenGroups;


    public StringGroup() {
        this(LEVEL_ROOT_GROUP, NAME_ROOT_GROUP, null);
    }

    public StringGroup(int groupLevel, String token, StringGroup parentGroup) {
        this.groupLevel = groupLevel;
        this.token = token;
        this.parentGroup = parentGroup;

        childrenGroups = createChildrenGroupsCollection();
    }

    /**
     * Not an abstract factory method for creating a collection of child groups
     *
     * @return {@link Collection}&lt;{@link StringGroup}&gt; instance
     */
    protected Collection<StringGroup> createChildrenGroupsCollection() {
        return new ArrayList<>();
    }


    // Getters And Setters

    public int getGroupLevel() {
        return groupLevel;
    }

    public String getToken() {
        return token;
    }

    public StringGroup getParentGroup() {
        return parentGroup;
    }

    public Collection<StringGroup> getChildrenGroups() {
        return childrenGroups;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((null == o) || (getClass() != o.getClass())) {
            return false;
        }

        StringGroup that = (StringGroup) o;

        if (groupLevel != that.getGroupLevel()) {
            return false;
        }

        //noinspection SimplifiableIfStatement
        if (!token.equals(that.getToken())) {
            return false;
        }

        return childrenGroups.equals(that.getChildrenGroups());
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") StringGroup o) {
        int result = token.compareTo(o.getToken());
        if (0 != result) {
            return result;
        }

        if (childrenGroups.size() != o.getChildrenGroups().size()) {
            return childrenGroups.size() - o.getChildrenGroups().size();
        }

        return -1;
    }

    @Override
    public int hashCode() {
        int result = groupLevel;
        result = 31 * result + token.hashCode();
        result = 31 * result + childrenGroups.hashCode();
        return result;
    }
}
