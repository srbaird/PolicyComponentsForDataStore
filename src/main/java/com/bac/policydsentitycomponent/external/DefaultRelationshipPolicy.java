/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import com.bac.policydsentitycomponent.entity.PolicyDSEntityRelationshipProxy;
import com.bac.policydsentitycomponent.external.ClassEntityComponent;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.DataTypeResolver;
import com.bac.policydsentitycomponent.external.EntityComponent;
import com.bac.policydsentitycomponent.external.NodeRelationship;
import com.bac.policydsentitycomponent.external.NodeRelationshipResolver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;
import static javax.swing.TransferHandler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
public class DefaultRelationshipPolicy implements RelationshipPolicy, Serializable {

	private static final long serialVersionUID = 5707558775709L;

	private DataTypeResolver dataTypeResolver;
	//
	private NodeRelationshipResolver nodeRelationshipResolver;
	//
	private List<EntityComponent> policyComponents;
	//
	private Map<DataType, Map<DataType, NodeRelationship>> relationshipMapping;
	// logger
	protected static Logger logger = LoggerFactory.getLogger(DefaultRelationshipPolicy.class);

	public DefaultRelationshipPolicy() {
	}

	public DefaultRelationshipPolicy(Map<DataType, Map<DataType, NodeRelationship>> relationshipMapping) {
		this.relationshipMapping = relationshipMapping;
	}

	public DataTypeResolver getDataTypeResolver() {
		return dataTypeResolver;
	}

	public void setDataTypeResolver(DataTypeResolver dataTypeResolver) {
		this.dataTypeResolver = dataTypeResolver;
	}

	public DefaultRelationshipPolicy(List<ClassEntityComponent> policyComponents) {
		buildMap(policyComponents);
	}

	public NodeRelationshipResolver getNodeRelationshipResolver() {
		return nodeRelationshipResolver;
	}

	public void setNodeRelationshipResolver(NodeRelationshipResolver nodeRelationshipResolver) {
		this.nodeRelationshipResolver = nodeRelationshipResolver;
	}

	@Override
	public void setRelationshipMapping(List<? extends ClassEntityComponent> policyComponents) {
		buildMap(policyComponents);
	}

	@Override
	public List<EntityComponent> getPolicyComponents() {
		return policyComponents;
	}

	@Override
	public void setRelationshipMapping(Map<DataType, Map<DataType, NodeRelationship>> relationshipMapping) {
		this.relationshipMapping = relationshipMapping;
	}

	@Override
	public NodeRelationship getRelationship(DataType from, DataType to) {

		if (isInvalidEnvironment(from, to)) {
			return null;
		}
		Map<DataType, NodeRelationship> mappings = relationshipMapping.get(from);
		if (mappings != null && mappings.containsKey(to)) {
			return mappings.get(to);
		}
		mappings = relationshipMapping.get(to);
		if (mappings != null && mappings.containsKey(from)) {
			NodeRelationship relationship = mappings.get(from);
			if (relationship != null && isTwoWayRelationship(relationship)) {
				return relationship;
			}
		}
		return null;
	}

	@Override
	public int getTransferType(EntityComponent assignee, EntityComponent assignment) {
		// If the relationship is hierarchical and the datatypes are the same
		if (assignee == null || assignment == null) {
			return NONE;
		}
		DataType assigneeDataType = assignee.getDataType();
		DataType assignmentDataType = assignment.getDataType();
		if (assigneeDataType == null || assignmentDataType == null) {
			return NONE;
		}
		NodeRelationship relationship = getRelationship(assigneeDataType, assignmentDataType);
		if (relationship == null) {
			return NONE;
		} else if (nodeRelationshipResolver.isHierarchyRelationship(relationship)) {
			return COPY;
		} else {
			return LINK;
		}
	}

	@Override
	public boolean hasRelationship(DataType from, DataType to) {

		if (isInvalidEnvironment(from, to)) {
			return false;
		}
		Map<DataType, NodeRelationship> mappings = relationshipMapping.get(from);

		if (mappings != null && mappings.containsKey(to)) {
			return true;
		}
		// Try the reverse relationship
		mappings = relationshipMapping.get(to);
		if (mappings != null && mappings.containsKey(from)) {
			return isTwoWayRelationship(mappings.get(from));
		}
		return false;
	}

	@Override
	public boolean isTwoWayRelationship(NodeRelationship relationship) {

		return nodeRelationshipResolver.isTwoWayRelationship(relationship);
	}

	private boolean isInvalidEnvironment(DataType from, DataType to) {

		if (from == null || to == null) {
			logger.warn("Incomplete arguments suppled to method: From = {}, to = {}", from, to);
			return true;
		}

		if (relationshipMapping == null) {
			logger.warn("Relationship mapping structure is null");
			return true;
		}
		return false;
	}

	@Override
	public Set<DataType> getPolicySet() {

		if (relationshipMapping != null) {
			return new HashSet<>(relationshipMapping.keySet());
		}
		return Collections.emptySet();
	}

	@Override
	public Map<DataType, NodeRelationship> getRelationshipMap(DataType dataType) {

		if (dataType != null && relationshipMapping != null && relationshipMapping.containsKey(dataType)) {
			return relationshipMapping.get(dataType);
		}
		return Collections.emptyMap();
	}

	private void buildMap(List<? extends ClassEntityComponent> policyComponents) {

		if (this.policyComponents == null) {
			this.policyComponents = new ArrayList<>();
		} else {
			this.policyComponents.clear();
		}

		if (policyComponents == null) {
			return;
		}
		if (relationshipMapping == null) {
			relationshipMapping = new HashMap<>();
		} else {
			relationshipMapping.clear();
		}
		// TODO: Remove this as duplicated in PolicyComponentDAO
		relationshipMapping = policyComponents.stream()
				.collect(Collectors.toMap(p -> p.getPolicyDataType(),
						p -> p.getEntityComponents().stream().collect(
								Collectors.toMap(e -> ((PolicyDSEntityRelationshipProxy) e).getPolicyDataType(),
										e -> ((PolicyDSEntityRelationshipProxy) e).getRelationshipType()))));

	}

	@Override
	public boolean isGroupDataType(DataType dataType) {

		// Default the Class Entity Component Data Type to true
		if (dataTypeResolver != null) {
			DataType classDataType = dataTypeResolver.getEntityComponentClassDataType();
			if (dataType == classDataType) { // note equivalence as they are
												// assumed to be Enums
				return true;
			}
		}
		if (dataType == null || relationshipMapping == null || !relationshipMapping.containsKey(dataType)) {
			return false; // No data type relationships
		}
		Map<DataType, NodeRelationship> relationships = relationshipMapping.get(dataType);
		if (relationships.isEmpty() || !relationships.containsKey(dataType)) {
			return false; // No relationship to itself
		}
		// True if relationship to itself is compositional
		return nodeRelationshipResolver.isHierarchyRelationship(relationships.get(dataType));
	}

	@Override
	public boolean canAssign(DataType from, DataType to) {
		//
		// Has an assign relationship
		if (!this.hasRelationship(from, to)) {
			return false;
		}
		// 'To' datatype is not part of a dependency relationship with another
		// datatype
		Map<DataType, NodeRelationship> toRelationships = relationshipMapping.get(to);
		for (Entry<DataType, NodeRelationship> entry : toRelationships.entrySet()) {
			if (nodeRelationshipResolver.isDependencyRelationship(entry.getValue()) && entry.getKey() != from) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isDependency(DataType dataType) {
		//
		Map<DataType, NodeRelationship> relationships = relationshipMapping.get(dataType);
		if (relationships == null || relationships.isEmpty()) {
			return false;
		}
		for (Entry<DataType, NodeRelationship> entry : relationships.entrySet()) {
			if (nodeRelationshipResolver.isDependencyRelationship(entry.getValue())) {
				return false;
			}
		}
		return true;
	}
}
