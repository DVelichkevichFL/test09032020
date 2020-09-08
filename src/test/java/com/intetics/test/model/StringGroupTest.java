package com.intetics.test.model;

import com.intetics.test.base.AbstractBaseTestCase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/03/2020 18:25
 *
 * @author d.velichkevich
 */
public class StringGroupTest extends AbstractBaseTestCase {

    private StringGroup parentGroup;


    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        parentGroup = createRootGroup();
    }

    protected StringGroup createRootGroup() {
        return new StringGroup();
    }

    @Test
    public void testInitialization() throws Exception {
        // Given - When
        StringGroup group = createGroupMock();

        // Then
        checkInitialization(group, 1, "TEST_TOKEN", parentGroup);
    }

    protected StringGroup createGroupMock() {
        return new StringGroup(1, "TEST_TOKEN", parentGroup) {

            @Override
            protected Collection<StringGroup> createChildrenGroupsCollection() {
                addActions("StringGroup* -> createChildrenGroupsCollection");

                return new HashSet<>();
            }
        };
    }

    @Test
    public void testInitializationRoot() throws Exception {
        // Given - When
        StringGroup group = createRootGroupMock();

        // Then
        checkInitialization(group, -1, "<ROOT_GROUP_NAME>", null);
    }

    protected StringGroup createRootGroupMock() {
        return new StringGroup() {

            @Override
            protected Collection<StringGroup> createChildrenGroupsCollection() {
                addActions("StringGroup* -> createChildrenGroupsCollection");

                return new HashSet<>();
            }
        };
    }

    private void checkInitialization(StringGroup group, int expectedLevel, String expectedToken, StringGroup expectedGroup) {
        assertThat(group.getGroupLevel()).isEqualTo(expectedLevel);
        assertThat(group.getToken()).isEqualTo(expectedToken);
        assertThat(group.getChildrenGroups()).isInstanceOf(HashSet.class);
        assertThat(group.getParentGroup()).isSameAs(expectedGroup);

        checkActions("StringGroup* -> createChildrenGroupsCollection");
    }

    @Test
    public void testCreatingChildrenGroupsCollection() throws Exception {
        // Given
        StringGroup group = createRootGroup();

        // When
        Collection<StringGroup> actual = group.createChildrenGroupsCollection();

        // Then
        assertThat(actual).isInstanceOf(getExpectedChildrenGroupsCollectionClass()).isEmpty();
    }

    protected Class<? extends Collection> getExpectedChildrenGroupsCollectionClass() {
        return ArrayList.class;
    }

    @Test
    public void testComparingTo() throws Exception {
        // Given
        StringGroup group = createGroup(1, "TOKEN_00", parentGroup);

        // When
        StringGroup anotherGroup = createGroup(1, "TOKEN_01", parentGroup);
        int actual = group.compareTo(anotherGroup);

        // Then
        assertThat(actual).isLessThan(0);
    }

    @Test
    public void testComparingToGreater() throws Exception {
        // Given
        StringGroup group = createGroup(1, "TOKEN_01", parentGroup);

        // When
        StringGroup anotherGroup = createGroup(1, "TOKEN_00", parentGroup);
        int actual = group.compareTo(anotherGroup);

        // Then
        assertThat(actual).isGreaterThan(0);
    }

    @Test
    public void testComparingToDifferentChildren() throws Exception {
        // Given
        StringGroup group = createGroup(1, "TOKEN_00", parentGroup);

        // When
        StringGroup anotherGroup = createGroupWithChild();
        int actual = group.compareTo(anotherGroup);

        // Then
        assertThat(actual).isLessThan(0);
    }

    @Test
    public void testComparingToDifferentChildrenGreater() throws Exception {
        // Given
        StringGroup group = createGroupWithChild();

        // When
        StringGroup anotherGroup = createGroup(1, "TOKEN_00", parentGroup);
        int actual = group.compareTo(anotherGroup);

        // Then
        assertThat(actual).isGreaterThan(0);
    }

    private StringGroup createGroupWithChild() {
        StringGroup result = createGroup(1, "TOKEN_00", parentGroup);
        result.getChildrenGroups().add(createGroup(2, "TEST_01", result));
        return result;
    }

    @Test
    public void testComparingToAllEqual() throws Exception {
        // Given
        StringGroup group = createGroup(1, "TOKEN_00", parentGroup);

        // When
        StringGroup anotherGroup = createGroup(1, "TOKEN_00", parentGroup);
        int actual = group.compareTo(anotherGroup);

        // Then
        assertThat(actual).isLessThan(0);
    }

    protected StringGroup createGroup(int level, String groupName, StringGroup parent) {
        return new StringGroup(level, groupName, parent);
    }


    // Getters And Setters

    protected StringGroup getParentGroup() {
        return parentGroup;
    }
}
