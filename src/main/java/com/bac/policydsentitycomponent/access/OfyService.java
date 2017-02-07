
package com.bac.policydsentitycomponent.access;

import com.bac.policydsentitycomponent.entity.PolicyDSEntity;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 *
 * @author Simon Baird
 */
public class OfyService {

    static {
        factory().register(PolicyDSEntity.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
