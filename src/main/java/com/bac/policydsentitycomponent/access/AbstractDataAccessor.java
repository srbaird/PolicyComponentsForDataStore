/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.access;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.entity.EntityComponentRelationship;
import com.bac.policydsentitycomponent.external.AbstractDSEntityComponent;
import com.bac.policydsentitycomponent.external.ClassEntityComponent;
import com.bac.policydsentitycomponent.external.DSNodeProperty;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.DataTypeResolver;
import com.bac.policydsentitycomponent.external.EntityComponent;
import com.bac.policydsentitycomponent.external.EntityComponentDAO;
import com.bac.policydsentitycomponent.external.EntityComponentFactory;
import com.bac.policydsentitycomponent.external.NodeRelationship;
import com.bac.policydsentitycomponent.external.NodeRelationshipResolver;
import com.bac.policydsentitycomponent.external.RelationshipPolicy;
import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Loader;
import com.googlecode.objectify.cmd.Query;

/**
 *
 * @author user0001
 * @param <T>
 */
public abstract class AbstractDataAccessor<T extends AbstractDSEntityComponent<T>>
		implements EntityComponentDAO<T>, Serializable {

	private static final long serialVersionUID = 5707558775707L;

	protected DataTypeResolver dataTypeResolver;
	//
	protected NodeRelationshipResolver nodeRelationshipResolver;
	//
	protected RelationshipPolicy relationshipPolicy;
	//
	protected EntityComponentFactory<DataType, T> componentFactory;
	//
	protected final String PARENT_INDEX = "parent";
	//
	private final String GUEST_USERID = "GuestUserId";
	private final String NULL_COMPONENT_ARGUMENT = "A null Entity Component was supplied";
	private final String NULL_PROPERTY_ARGUMENT = "A null Node Property was supplied";
	private final String NULL_RELATIONSHIP_ARGUMENT = "A null Node Relationship was supplied";
	private final String COMPONENT_NOT_FOUND = "A required Entity Component was not found";
	private final String NO_COMPONENT_RELATIONSHIP = "Supplied component has no relationship";
	private final String NULL_NODE_RELATIONSHIP_RESOLVER = "No Node Relationship Resolver has been supplied";
	private final String NULL_RELATIONSHIP_POLICY = "No Relationship Policy has been supplied";
	private final String NULL_DATATYPE_RESOLVER = "No Data Type Resolver has been supplied";
	private final String NULL_COMPONENT_FACTORY = "No Entity Component Factory has been supplied";
	// logger
	protected static Logger logger = LoggerFactory.getLogger(AbstractDataAccessor.class);

	public EntityComponentFactory<DataType, T> getComponentFactory() {
		return componentFactory;
	}

	public void setComponentFactory(EntityComponentFactory<DataType, T> componentFactory) {
		this.componentFactory = componentFactory;
	}

	public DataTypeResolver getDataTypeResolver() {
		return dataTypeResolver;
	}

	public void setDataTypeResolver(DataTypeResolver dataTypeResolver) {
		this.dataTypeResolver = dataTypeResolver;
	}

	public NodeRelationshipResolver getNodeRelationshipResolver() {
		return nodeRelationshipResolver;
	}

	public void setNodeRelationshipResolver(NodeRelationshipResolver nodeRelationshipResolver) {
		this.nodeRelationshipResolver = nodeRelationshipResolver;
	}

	public RelationshipPolicy getRelationshipPolicy() {
		return relationshipPolicy;
	}

	public void setRelationshipPolicy(RelationshipPolicy relationshipPolicy) {
		this.relationshipPolicy = relationshipPolicy;
	}

	@Override
	public void init() {

		if (componentFactory == null) {
			throw new IllegalStateException(NULL_COMPONENT_FACTORY);
		}
		if (dataTypeResolver == null) {
			throw new IllegalStateException(NULL_DATATYPE_RESOLVER);
		}
		if (nodeRelationshipResolver == null) {
			throw new IllegalStateException(NULL_NODE_RELATIONSHIP_RESOLVER);
		}
		if (relationshipPolicy == null) {
			throw new IllegalStateException(NULL_RELATIONSHIP_POLICY);
		}
	}

	protected abstract Objectify getOfy();

	@Override
	public T createEntityAssociation(EntityComponent assigneeComponent, EntityComponent assignmentComponent) {

		if (assigneeComponent == null) {
			throw new IllegalArgumentException(NULL_COMPONENT_ARGUMENT);
		}
		if (assignmentComponent == null) {
			throw new IllegalArgumentException(NULL_COMPONENT_ARGUMENT);
		}
		setUserNameSpace();

		return _buildEntityComponent(_createEntityAssociation(assigneeComponent, assignmentComponent));
	}

	@Override
	public T createEntityAssociation(EntityComponent assigneeComponent, EntityComponent assignmentComponent,
			NodeRelationship relationship) {

		if (assigneeComponent == null) {
			throw new IllegalArgumentException(NULL_COMPONENT_ARGUMENT);
		}
		if (assignmentComponent == null) {
			throw new IllegalArgumentException(NULL_COMPONENT_ARGUMENT);
		}
		if (relationship == null) {
			throw new IllegalArgumentException(NULL_RELATIONSHIP_ARGUMENT);
		}
		setUserNameSpace();

		return _buildEntityComponent(_createEntityAssociation(assigneeComponent, assignmentComponent, relationship));
	}

	protected T _createEntityAssociation(EntityComponent assigneeComponent, EntityComponent assignmentComponent) {

		return _createEntityAssociation(assigneeComponent, assignmentComponent, null);

	}

	protected T _createEntityAssociation(EntityComponent assigneeComponent, EntityComponent assignmentComponent,
			NodeRelationship relationship) {

		T componentToUpdate = _loadEntityComponent(assigneeComponent);
		if (componentToUpdate == null) {
			throw new IllegalStateException(COMPONENT_NOT_FOUND);
		}
		T componentToAssign = _loadEntityComponent(assignmentComponent);
		if (componentToAssign == null) {
			throw new IllegalStateException(COMPONENT_NOT_FOUND);
		}
		// Ensure we have a relationship
		if (relationship == null) {
			DataType dataTypeFrom = assigneeComponent.getDataType();
			DataType dataTypeTo = assignmentComponent.getDataType();
			relationship = relationshipPolicy.getRelationship(dataTypeFrom, dataTypeTo);
		}

		boolean isReversibleDataType = dataTypeResolver.isReversibleDataType(componentToAssign.getDataType());
		boolean isAssignmentRelationship = nodeRelationshipResolver.isAssignmentRelationship(relationship);

		// All entities must be subclasses of T
		componentToUpdate.addAssignment(relationship, componentToAssign);

		if (nodeRelationshipResolver.isHierarchyRelationship(relationship)) {
			componentToAssign.setParent(componentToUpdate);
		}

		// Persist
		getOfy().save().entity(componentToUpdate).now();

		// Handle the assignment index
		componentToAssign.addAssignmentIndex(componentToUpdate);
		if (isReversibleDataType && isAssignmentRelationship) {
			componentToAssign.addAssignee(relationship, componentToUpdate);
		}
		getOfy().save().entity(componentToAssign).now();
		return componentToUpdate;
	}

	@Override
	public T createEntityComponent(EntityComponent entityComponent) {

		if (entityComponent == null) {
			throw new IllegalArgumentException(NULL_COMPONENT_ARGUMENT);
		}
		setUserNameSpace();
		return _buildEntityComponent(_createEntityComponent(entityComponent));
	}

	protected T _createEntityComponent(EntityComponent entityComponent) {

		T createdComponent = componentFactory.createBeanByDataType(entityComponent.getDataType());
		createdComponent.populateEntityComponent(entityComponent);
		getOfy().save().entity(createdComponent).now();
		// logger.info("Created: {}", createdComponent);
		return createdComponent;
	}

	@Override
	public T createEntityComponent(EntityComponent parentComponent, EntityComponent childComponent) {

		if (parentComponent == null) {
			throw new IllegalArgumentException(NULL_COMPONENT_ARGUMENT);
		}
		if (childComponent == null) {
			throw new IllegalArgumentException(NULL_COMPONENT_ARGUMENT);
		}
		setUserNameSpace();
		childComponent = _createEntityComponent(childComponent);
		if (childComponent == null) {
			return null;
		}
		_createEntityAssociation(parentComponent, childComponent);
		return _buildEntityComponent(childComponent);
	}

	@Override
	public T deleteAssociation(EntityComponent entityComponent) {

		if (entityComponent == null) {
			throw new IllegalArgumentException(NULL_COMPONENT_ARGUMENT);
		}
		if (!(entityComponent instanceof EntityComponentRelationship)) {
			throw new IllegalArgumentException(NO_COMPONENT_RELATIONSHIP);
		}
		// The component as a relationship
		final EntityComponentRelationship relationship = (EntityComponentRelationship) entityComponent;
		if (relationship.getRelationshipId() == null) {
			throw new IllegalArgumentException(NO_COMPONENT_RELATIONSHIP);
		}
		
		setUserNameSpace();
		@SuppressWarnings("unchecked")
		Key<T> relationshipId = (Key<T>) relationship.getRelationshipId();
		LoadResult<T> relationshipResult = getOfy().load().key(relationshipId);
		if (relationshipResult == null) {
			logger.warn("Relationship id load resolves to null");
			return null;
		}
		//
		// Resolve the actual direction of the relationship
		//
		T assigneeComponent;
		T assignmentComponent;

		if (relationship.isReverseRelationship()) {
			// Reverse the order
			assigneeComponent = _loadEntityComponent(entityComponent);
			assignmentComponent = relationshipResult.now();
		} else {
			// Standard order
			assigneeComponent = relationshipResult.now();
			assignmentComponent = _loadEntityComponent(entityComponent);
		}

		if (assigneeComponent == null || assignmentComponent == null) {
			return null;
		}
		if (assigneeComponent.removeAssignment(assignmentComponent)) {
			getOfy().save().entity(assigneeComponent).now();
		}
		if (assignmentComponent.removeAssignee(assigneeComponent)
				|| assignmentComponent.removeAssignmentIndex(assigneeComponent)) {
			getOfy().save().entity(assignmentComponent).now();
		}
		return _buildEntityComponent(assigneeComponent);
	}

	@Override
	public Set<T> deleteEntityComponent(EntityComponent entityComponent) {

		if (entityComponent == null) {
			throw new IllegalArgumentException(NULL_COMPONENT_ARGUMENT);
		}
		setUserNameSpace();

		T deleteComponent = _loadEntityComponent(entityComponent);
		if (deleteComponent == null) {
			return Collections.emptySet();
		}
		return _deleteEntityComponent(Ref.create(deleteComponent));
	}

	protected Set<T> _deleteEntityComponent(Ref<T> ref) {
		//
		// Get immediate family of components: load component and
		// getAssignmentIndex()
		// Generate list of cascade deletes: getAssignmentMap() and filter
		// Remove reference from immediate family
		// Recursive delete of cascades
		// Delete component
		// Return set of immediate family
		//
		T deleteComponent = _loadEntityComponent(ref);
		if (deleteComponent == null) {
			return null;
		}
		// Get immediate family of components

		// Generate list of cascade deletes
		// Build set of immediate family
		Set<Ref<T>> immediateFamily = new HashSet<>();
		Set<Ref<T>> cascadeDeletes = new HashSet<>();
		Map<String, Set<Ref<T>>> assignmentMap = deleteComponent.getAssignmentMap();
		if (assignmentMap != null) {
			for (String relationship : assignmentMap.keySet()) {
				NodeRelationship nodeRelationship = nodeRelationshipResolver.valueOf(relationship);
				immediateFamily.addAll(assignmentMap.get(relationship));
				if (nodeRelationshipResolver.isCascadeDeleteRelationship(nodeRelationship)) {
					cascadeDeletes.addAll(assignmentMap.get(relationship));
				}
			}
		}
		Set<T> familyMembers = new HashSet<>();
		for (Ref<T> familyMember : immediateFamily) {
			familyMembers.add(_buildEntityComponent(familyMember));
		}

		Set<Ref<T>> assignmentIndex = deleteComponent.getAssignmentIndex();
		if (assignmentIndex != null) {
			for (Ref<T> indexMember : assignmentIndex) {
				T indexComponent = _loadEntityComponent(indexMember);
				// Remove reference from immediate family
				indexComponent.removeAssignment(deleteComponent);
				getOfy().save().entity(indexComponent).now();
			}
		}
		// Recursive delete of cascades.
		//
		// TO DO: Replace the 'instanceof' with a method in DatatypeResolver
		//
		if (!(deleteComponent instanceof ClassEntityComponent)) {
			for (Ref<T> deleteMember : cascadeDeletes) {
				_deleteEntityComponent(deleteMember);
			}
		} else {
			//
			// Ensure the assignment index of the assigned component is updated
			// to reflect the assignee deletion
			//
			if (assignmentMap != null) {
				for (Set<Ref<T>> mapValue : assignmentMap.values()) {
					if (mapValue != null) {
						for (Ref<T> setValue : mapValue) {
							T setComponent = _loadEntityComponent(setValue);
							if (setComponent != null) {
								setComponent.removeAssignmentIndex(deleteComponent);
								getOfy().save().entity(setComponent).now();
							}
						}
					}
				}
			}
		}
		// Delete component
		familyMembers.add(deleteComponent);
		getOfy().delete().entity(deleteComponent).now();
		// Return set of immediate family
		return familyMembers;
	}

	@Override
	public List<T> listComponentsByProperty(DSNodeProperty DSNodeProperty) {

		return listComponentsByProperty(null, DSNodeProperty);
	}

	@Override
	public List<T> listComponentsByProperty(DataType dataType, DSNodeProperty DSNodeProperty) {

		if (DSNodeProperty == null) {
			throw new IllegalArgumentException(NULL_PROPERTY_ARGUMENT);
		}
		setUserNameSpace();

		List<T> returnNodes = new LinkedList<>();
		// Build the query. All data types where the property is not null
		LoadType<? extends T> loadType = getLoadType(getOfy().load(), dataType);
		logger.info("Load type: {}", loadType);

		Query<? extends T> query = loadType.filter(getNotNullFilter(DSNodeProperty));
		logger.info("Query: {}", query);
		List<? extends T> components = query.list();
		for (T component : components) {
			returnNodes.add(_buildEntityComponent(component));
		}
		return returnNodes;
	}

	@Override
	public List<T> listComponentsByProperty(DataType dataType, DSNodeProperty DSNodeProperty, Object startRange,
			Object endRange) {
		if (DSNodeProperty == null) {
			throw new IllegalArgumentException(NULL_PROPERTY_ARGUMENT);
		}
		if (startRange == null && endRange == null) {
			return listComponentsByProperty(dataType, DSNodeProperty);
		}
		setUserNameSpace();

		List<T> returnNodes = new LinkedList<>();
		// Build the query
		LoadType<? extends T> loadType = getLoadType(getOfy().load(), dataType);
		Query<? extends T> query = loadType.filter(getRangeFilter(DSNodeProperty, startRange, endRange));
		List<? extends T> components = query.list();
		for (T component : components) {
			returnNodes.add(_buildEntityComponent(component));
		}
		return returnNodes;
	}

	protected LoadType<? extends T> getLoadType(Loader loader, DataType dataType) {

		Class<? extends T> loadClass = componentFactory.getEntityComponentClass(dataType);
		return loader.type(loadClass);
	}

	protected FilterPredicate getNullFilter(String indexName) {

		return new FilterPredicate(indexName, FilterOperator.EQUAL, null);
	}

	protected Filter getNotNullFilter(DSNodeProperty property) {

		return getNotNullFilter(property.fieldName());
	}

	protected Filter getNotNullFilter(String indexName) {

		return new FilterPredicate(indexName, FilterOperator.NOT_EQUAL, null);
	}

	protected Filter getRangeFilter(DSNodeProperty DSNodeProperty, Object startRange, Object endRange) {

		if (startRange == null && endRange != null) {
			return new FilterPredicate(DSNodeProperty.name(), FilterOperator.LESS_THAN_OR_EQUAL, endRange);
		} else if (startRange != null && endRange == null) {
			return new FilterPredicate(DSNodeProperty.name(), FilterOperator.GREATER_THAN_OR_EQUAL, startRange);
		} else {
			return new CompositeFilter(CompositeFilterOperator.AND,
					Arrays.<Filter>asList(
							new FilterPredicate(DSNodeProperty.name(), FilterOperator.GREATER_THAN_OR_EQUAL,
									startRange),
							new FilterPredicate(DSNodeProperty.name(), FilterOperator.LESS_THAN_OR_EQUAL, endRange)));
		}
	}

	@Override
	public List<T> listRootNodes(DataType datatype) {

		setUserNameSpace();
		return buildRootNodesList(datatype, null);
	}

	@Override
	public List<T> listRootNodesSimple(DataType datatype) {

		setUserNameSpace();
		Set<? extends NodeRelationship> relationshipsToBuild = nodeRelationshipResolver == null ? Collections.emptySet()
				: nodeRelationshipResolver.getHierarchyRelationships();
		return buildRootNodesList(datatype, relationshipsToBuild);
	}

	@Override
	public T newEntityComponent(DataType dataType) {

		return componentFactory.createBeanByDataType(dataType);
	}

	@Override
	public T newEntityComponent(EntityComponent entityComponent) {

		if (entityComponent == null) {
			throw new IllegalArgumentException(NULL_COMPONENT_ARGUMENT);
		}
		T newComponent = componentFactory.createBeanByDataType(entityComponent.getDataType());
		newComponent.populateEntityComponent(entityComponent);
		return newComponent;
	}

	protected List<T> buildRootNodesList(DataType datatype, Set<? extends NodeRelationship> relationshipsToBuild) {
		if (datatype == null) {
			return null;
		}
		setUserNameSpace();

		List<T> returnNodes = new LinkedList<>();
		// Build the query
		LoadType<? extends T> loadType = getLoadType(getOfy().load(), datatype);
		Query<? extends T> query = loadType.filter(getNullFilter(PARENT_INDEX));
		List<? extends T> rootNodes = query.list();
		//
		for (T rootNode : rootNodes) {
			returnNodes.add(_buildEntityComponent(rootNode, relationshipsToBuild));
		}
		return returnNodes;
	}

	@Override
	public T readEntityComponent(EntityComponent entityComponent) {

		if (entityComponent == null) {
			return null;
		}
		setUserNameSpace();
		return _buildEntityComponent(entityComponent);
	}

	@Override
	public T readEntityHierarchyStructure(EntityComponent entityComponent) {
		if (entityComponent == null) {
			return null;
		}
		setUserNameSpace();
		//
		T loadComponent = _loadEntityComponent(entityComponent);
		Ref<T> parentRef = loadComponent.getParent();
		while (parentRef != null) {
			loadComponent = _loadEntityComponent(parentRef);
			parentRef = loadComponent.getParent();
		}
		// Only hierarchy relationships required
		@SuppressWarnings("unchecked")
		Set<? extends NodeRelationship> relationshipsToBuild = nodeRelationshipResolver == null ? Collections.EMPTY_SET
				: nodeRelationshipResolver.getHierarchyRelationships();
		return _buildEntityComponent(entityComponent, relationshipsToBuild);
	}

	protected T _loadEntityComponent(EntityComponent entityComponent) {

		long id = entityComponent.getId();
		if (id == 0) {
			return null; // Id cannot be zero
		}

		T readComponent = componentFactory.createBeanByDataType(entityComponent.getDataType());
		readComponent.populateEntityComponent(entityComponent);

		LoadResult<T> readResult = getOfy().load().key(readComponent.getKey());
		return readResult.now();
	}

	protected T _loadEntityComponent(Ref<T> ref) {

		if (ref == null) {
			return null; // Id cannot be zero
		}
		LoadResult<T> readResult = getOfy().load().ref(ref);
		return readResult.now();
	}

	protected T _buildEntityComponent(EntityComponent entityComponent) {

		return _buildEntityComponent(entityComponent, null);
	}

	protected T _buildEntityComponent(EntityComponent entityComponent,
			Set<? extends NodeRelationship> relationshipsToBuild) {

		T readComponent = _loadEntityComponent(entityComponent);
		return finalizeBuild(readComponent, relationshipsToBuild);
	}

	protected T _buildEntityComponent(Ref<T> ref) {

		return _buildEntityComponent(ref, null);
	}

	protected T _buildEntityComponent(Ref<T> ref, Set<? extends NodeRelationship> relationshipsToBuild) {

		T readComponent = _loadEntityComponent(ref);
		return finalizeBuild(readComponent, relationshipsToBuild);
	}

	protected T finalizeBuild(T readComponent, Set<? extends NodeRelationship> relationshipsToBuild) {
		if (readComponent != null) {
			readComponent.finalizeLoad(nodeRelationshipResolver, relationshipsToBuild, readComponent);
		}
		return readComponent;
	}

	@Override
	public T updateEntityComponent(EntityComponent entityComponent) {

		if (entityComponent == null) {
			return null;
		}
		setUserNameSpace();

		T updateComponent = _loadEntityComponent(entityComponent);
		if (updateComponent == null) {
			return _buildEntityComponent(_createEntityComponent(entityComponent));
		}
		updateComponent.populateEntityComponent(entityComponent);
		getOfy().save().entity(updateComponent).now();
		return _buildEntityComponent(entityComponent);
	}

	//
	//
	//
	protected void setUserNameSpace() {

		String namespace;
		try {
			namespace = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		} catch (NullPointerException e) {
			namespace = GUEST_USERID;
		}
		try {
			NamespaceManager.set(namespace);
		} catch (NullPointerException e) {
			logger.warn("Unable to set the data store name space");
		}
	}

	//
	// These methods are left over from an earlier definition of the DAO and are
	// specified in ComponentDAO. Only the init() method is implemented here so
	// they should all be refactored out
	//
	@Override
	public void destroyMethod() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setResourceName(String resourceName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
