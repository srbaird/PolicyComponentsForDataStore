/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user0001
 */
@XmlRootElement(name="nodeRelationship") 
public enum EntityComponentNodeRelationship implements NodeRelationship {

    DEPENDENCY_OF("Owner of")
    , HIERARCHY_OF("Forms hierarchy of")
    , ASSIGNEE_OF("Can assign a")
    , LINK_WITH("Can link to a");
    
    private final String displayName;

    EntityComponentNodeRelationship(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
