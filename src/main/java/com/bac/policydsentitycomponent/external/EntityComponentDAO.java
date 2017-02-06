/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import java.util.List;
import java.util.Set;

/**
 *
 * @author user0001
 */
public interface EntityComponentDAO<T> extends ComponentDAO {

	T createEntityAssociation(EntityComponent assigneeComponent, EntityComponent assignmentComponent);

	T createEntityAssociation(EntityComponent assigneeComponent, EntityComponent assignmentComponent,
			NodeRelationship relationship);

	T createEntityComponent(EntityComponent entityComponent);

	T createEntityComponent(EntityComponent parentComponent, EntityComponent childComponent);

	T deleteAssociation(EntityComponent entityComponent);

	Set<T> deleteEntityComponent(EntityComponent entityComponent);

	List<T> listComponentsByProperty(DSNodeProperty nodeProperty);

	List<T> listComponentsByProperty(DataType dataType, DSNodeProperty nodeProperty);

	List<T> listComponentsByProperty(DataType dataType, DSNodeProperty nodeProperty, Object startRange, Object endRange);

	List<T> listRootNodes(DataType datatype);

	List<T> listRootNodesSimple(DataType datatype);

	T newEntityComponent(DataType dataType);

	T newEntityComponent(EntityComponent entityComponent);

	T readEntityComponent(EntityComponent entityComponent);

	T readEntityHierarchyStructure(EntityComponent entityComponent);

	T updateEntityComponent(EntityComponent entityComponent);

}
