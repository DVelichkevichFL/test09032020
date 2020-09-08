package com.intetics.test.service.impl;

import com.intetics.test.base.AbstractBaseTestCase;
import com.intetics.test.model.StringGroup;
import org.junit.jupiter.api.Test;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/04/2020 1:33
 *
 * @author d.velichkevich
 */
public class DefaultStringGroupOrganizingStrategyTest extends AbstractBaseTestCase {

    @Test
    public void testCreatingRoot() throws Exception {
        // Given
        DefaultStringGroupOrganizingStrategy strategy = new DefaultStringGroupOrganizingStrategy();

        // When
        StringGroup actual = strategy.createRoot();

        // Then
        assertThat(actual).isEqualTo(new StringGroup());
    }

    @Test
    public void testCreating() throws Exception {
        // Given
        DefaultStringGroupOrganizingStrategy strategy = new DefaultStringGroupOrganizingStrategy();

        // When
        StringGroup actual = strategy.create(1, "TEST_TOKEN", new StringGroup());

        // Then
        assertThat(actual).isEqualTo(new StringGroup(1, "TEST_TOKEN", new StringGroup()));
    }
}
