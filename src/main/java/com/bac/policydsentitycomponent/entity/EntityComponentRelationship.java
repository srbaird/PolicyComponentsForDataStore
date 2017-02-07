package com.bac.policydsentitycomponent.entity;

import com.bac.policydsentitycomponent.external.NodeRelationship;

/**
 * Represents the public methods required to identify the relationship to a
 * component. 
 * 
 * @author Simon Baird
 *
 */
public interface EntityComponentRelationship {

	boolean isReverseRelationship();

	Object getRelationshipId();

	NodeRelationship getRelationshipType();

}