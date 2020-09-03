package com.intetics.test.service;

import com.intetics.test.model.StringGroup;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/02/2020 18:57
 *
 * @author d.velichkevich
 */
public interface StringGroupOrganizingStrategy {

    /**
     * Creates a root {@link StringGroup} instance, specific to the strategy
     *
     * @return {@link StringGroup}
     */
    StringGroup createRoot();

    /**
     * Creates a parametrized instance of {@link StringGroup}, specific to the strategy
     *
     * @param level {@link Integer} (primitive) value of group level
     * @param token {@link String} value of group name
     * @param parent {@link StringGroup} instance of group parent
     * @return {@link StringGroup} instance
     */
    StringGroup create(int level, String token, StringGroup parent);
}
