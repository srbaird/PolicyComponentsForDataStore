/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import java.util.Objects;

/**
 *
 * @author user0001
 */
public class EntityComponents {

    public static boolean equals(Object e1, Object e2) {

        if (e1 == e2) {
            return true;
        }
        if (e1 == null || e2 == null) {
            return false;
        }
        // Ensure that any proxies are handled
        if (e1 instanceof EntityComponent && e2 instanceof EntityComponent) {
            // If both Ids are null then neither can be a component
            //  Objects.equals() would return true in this case
            if (((EntityComponent) e1).getId() == null && ((EntityComponent) e2).getId() == null ){
                return false;
            }
            return Objects.equals(((EntityComponent) e1).getId(), ((EntityComponent) e2).getId());
        } else {
            return false;
        }
    }

    public static EntityComponent getEntityComponent(EntityComponent e) {

        if (e == null) {
            return null;
        }
        // Ensure that any proxies are handled
        if (e instanceof EntityComponentProxy) {
            EntityComponentProxy proxy = (EntityComponentProxy) e;
            e = proxy.getDelegate();
        }
        return e;
    }
}
