/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import java.util.List;

/**
 *
 * @author Simon Baird
 */
public interface EntityComponent {

    Long getId();

    Long getTimestamp();

//    Object getRelationshipId();

//    NodeRelationship getRelationshipType();

//    boolean isReverseRelationship();

    DataType getDataType();

    void setDataType(DataType type);

    String getIcon();

    void setIcon(String name);

    String getName();

    void setName(String name);

    Long getValueDate();

    void setValueDate(Long valueDate);

    String getContent();

    void setContent(String text);

    List<EntityComponent> getEntityComponents();

    void setEntityComponents(List<EntityComponent> entityComponents);

    void addEntityComponent(EntityComponent entityComponent);

}
