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
@XmlRootElement(name = "dataType")
public enum EntityComponentDataType implements DataType {

    ENTITY_COMPONENT_CLASS("Policy node"), 
    TAG("Data tag"), 
    AGGREGATION_TAG("Accumulations"), 
    IMAGE_COMPONENT("Picture"), 
    INLINE_NOTE("Text"),
    PLANNER_EVENT("Planner"), 
    UNRESOLVED("Cannot resolve");

    private final String displayName;

    EntityComponentDataType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
