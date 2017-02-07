/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.entity.EntityComponentRelationship;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.condition.IfNull;
import com.googlecode.objectify.condition.IfZero;

/**
 *
 * @author Simon Baird
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class AbstractDSEntityComponent<T extends AbstractDSEntityComponent<T>>
		implements EntityComponent, EntityComponentRelationship, DSEntityComponent<T>, Serializable {
	//
	// Objectify persists fields and only fields.
	//
	@Id
	protected Long id;
	//
	// Parent is indexed only if null to identify root components
	//
	@Index(IfNull.class)
	protected Ref<T> parent;
	//
	protected Long timestamp;
	//
	@IgnoreSave(IfZero.class)
	protected Long valueDate;
	//
	protected String name;
	//
	protected String dataType;
	//
	protected String content;
	//
	protected String icon;
	//
	// Holds the key to the parent when the component is built as an assignment
	//
	@Ignore
	protected Key<T> relationshipParentKey;
	@Ignore
	protected boolean reverseRelationship;
	@Ignore
	protected NodeRelationship relationshipType;
	@Ignore
	protected List<EntityComponent> entityComponents = new LinkedList<>();
	//
	// Index of Assignees. All components with a relationship to this
	//
	@Load
	protected List<Ref<T>> assignees;
	//
	// Assignee map. Relationship mapping to components that have this
	// component as an assignment. Only appropriate for Reverse data types
	//
	@Load
	protected Map<String, Set<Ref<T>>> assigneeMap;
	//
	// References to assignments
	//
	@Load
	protected Set<Ref<T>> assignmentIndex;
	//
	// Assignee map. Relationship mapping to components that have been
	// assigned to this.
	//
	@Load
	protected Map<String, Set<Ref<T>>> assignmentMap;
	// logger
	@Ignore
	protected static Logger logger = LoggerFactory.getLogger(AbstractDSEntityComponent.class);

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public Long getTimestamp() {
		return timestamp;
	}

	@Override
	public Object getRelationshipId() {
		return relationshipParentKey;
	}

	@Override
	public NodeRelationship getRelationshipType() {
		return relationshipType;
	}

	@Override
	public boolean isReverseRelationship() {
		return reverseRelationship;
	}

	@Override
	public abstract DataType getDataType();

	@Override
	public void setDataType(DataType dataType) {
		this.dataType = dataType == null ? null : dataType.name();
	}

	@Override
	public String getIcon() {
		return icon;
	}

	@Override
	public void setIcon(String icon) {
		this.icon = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Long getValueDate() {
		return valueDate;
	}

	;

	@Override
	public void setValueDate(Long valueDate) {
		this.valueDate = valueDate;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void setContent(String text) {
		this.content = text;
	}

	@Override
	public List<EntityComponent> getEntityComponents() {
		return this.entityComponents;
	}

	@Override
	public void setEntityComponents(List<EntityComponent> entityComponents) {
		this.entityComponents = entityComponents;
	}

	@Override
	public void addEntityComponent(EntityComponent entityComponent) {

		if (entityComponents != null) {
			entityComponents.add(entityComponent);
		}
	}

	//
	// Implementation of DSEntityComponent
	//
	@Override
	public void addAssignee(NodeRelationship relationship, T assigneeComponent) {

		if (assigneeMap == null) {
			assigneeMap = new HashMap<>();
		}
		String key = relationship == null ? "Unknown" : relationship.name();
		Set<Ref<T>> assignSet = assigneeMap.get(key);
		if (assignSet == null) {
			assignSet = new HashSet<>();
		}
		Ref<T> ref = Ref.create(assigneeComponent);
		assignSet.add(ref);
		assigneeMap.put(key, assignSet);
	}

	@Override
	public void addAssignment(NodeRelationship relationship, T assignmentComponent) {

		if (assignmentMap == null) {
			assignmentMap = new HashMap<>();
		}
		String key = relationship == null ? "Unknown" : relationship.name();
		Set<Ref<T>> assignSet = assignmentMap.get(key);
		if (assignSet == null) {
			assignSet = new HashSet<>();
		}
		Ref<T> assignmentRef = Ref.create(assignmentComponent);
		assignSet.add(assignmentRef);
		assignmentMap.put(key, assignSet);
	}

	@Override
	public void addAssignmentIndex(T assignmentComponent) {
		if (assignmentComponent == null) {
			return;
		}
		if (assignmentIndex == null) {
			assignmentIndex = new HashSet<>();
		}
		Ref<T> assignmentRef = Ref.create(assignmentComponent);
		assignmentIndex.add(assignmentRef);
	}

	@Override
	public void finalizeLoad(NodeRelationshipResolver relationshipResolver, T rootComponent) {
		finalizeLoad(relationshipResolver, null, rootComponent);
	}

	@Override
	public void finalizeLoad(NodeRelationshipResolver relationshipResolver,
			Set<? extends NodeRelationship> relationshipsToBuild, T rootComponent) {
		//
		// The sequence here is important because finalizeAssignments() clears
		// the list of entityComponents
		//
		finalizeAssignments(relationshipResolver, relationshipsToBuild, rootComponent);
		finalizeAssignees(relationshipResolver, relationshipsToBuild, rootComponent);
	}

	protected void finalizeAssignees(NodeRelationshipResolver relationshipResolver,
			Set<? extends NodeRelationship> relationshipsToBuild, T rootComponent) {

		if (assigneeMap != null) {
			for (Map.Entry<String, Set<Ref<T>>> assigneeSet : assigneeMap.entrySet()) {
				String relationshipName = assigneeSet.getKey();
				Set<Ref<T>> entitySet = assigneeSet.getValue();
				if (entitySet == null) {
					continue;
				}
				for (Ref<T> entityItem : entitySet) {
					T assigneeComponent = entityItem.get();
					if (assigneeComponent == null) {
						continue;
					}
					if (EntityComponents.equals(assigneeComponent, rootComponent)) {
						continue;
					}
					NodeRelationship relationship = relationshipResolver.valueOf(relationshipName);
					if (!finalizeThisRelationship(relationship, relationshipsToBuild)) {
						continue;
					}
					assigneeComponent.setReverseRelationship(true);
					// assigneeComponent.setRelationshipParentKey(Key.create(getClass(),
					// id));
					assigneeComponent.setRelationshipParentKey(getKey());
					assigneeComponent.setRelationshipType(relationship);
					// Note this does not process assignees but assignments
					// instead
					assigneeComponent.finalizeAssignments(relationshipResolver, relationshipsToBuild, rootComponent);
					entityComponents.add(assigneeComponent);
				}
			}
		}
	}

	protected void finalizeAssignments(NodeRelationshipResolver relationshipResolver,
			Set<? extends NodeRelationship> relationshipsToBuild, T rootComponent) {

		entityComponents.clear();
		if (assignmentMap != null) {
			for (Map.Entry<String, Set<Ref<T>>> assignmentSet : assignmentMap.entrySet()) {
				String relationshipName = assignmentSet.getKey();
				Set<Ref<T>> entitySet = assignmentSet.getValue();
				if (entitySet == null) {
					continue;
				}
				for (Ref<T> entityItem : entitySet) {
					T assignmentComponent = entityItem.get();
					if (assignmentComponent == null) {
						continue;
					}
					if (EntityComponents.equals(assignmentComponent, rootComponent)) {
						continue;
					}
					NodeRelationship relationship = relationshipResolver.valueOf(relationshipName);
					if (!finalizeThisRelationship(relationship, relationshipsToBuild)) {
						continue;
					}
					assignmentComponent.setRelationshipParentKey(getKey());
					assignmentComponent.setRelationshipType(relationship);
					assignmentComponent.finalizeAssignments(relationshipResolver, relationshipsToBuild, rootComponent);
					entityComponents.add(assignmentComponent);
				}
			}
		}
	}

	@Override
	public Ref<T> getParent() {
		return parent;
	}

	@Override
	public void setParent(T parentComponent) {

		if (parentComponent == null) {
			parent = null;
			return;
		}
		this.parent = Ref.create(parentComponent);
	}

	@Override
	public Set<Ref<T>> getAssignmentIndex() {
		return assignmentIndex;
	}

	@Override
	public Map<String, Set<Ref<T>>> getAssignmentMap() {
		return assignmentMap;
	}

	@Override
	public void populateEntityComponent(EntityComponent entityComponent) {

		if (entityComponent == null) {
			return;
		}
		// Only populate if the id is valid, id's of zero are not allowed
		Long idToPopulate = entityComponent.getId();
		if (idToPopulate != null && idToPopulate != 0) {
			id = idToPopulate;
		}
		setContent(entityComponent.getContent());
		setDataType(entityComponent.getDataType());
		setIcon(entityComponent.getIcon());
		setName(entityComponent.getName());
		setValueDate(entityComponent.getValueDate());
	}

	@Override
	public boolean removeAssignee(T assigneeComponent) {

		if (assigneeMap == null) {
			return false;
		}
		Ref<T> assigneeRef = Ref.create(assigneeComponent);
		boolean wasRemoved = false;
		for (Set<Ref<T>> assigneeSet : assigneeMap.values()) {
			wasRemoved = wasRemoved || assigneeSet.remove(assigneeRef);
		}
		return wasRemoved;
	}

	@Override
	public boolean removeAssignment(T assignmentComponent) {

		if (assignmentMap == null) {
			return false;
		}
		Ref<T> assignmentRef = Ref.create(assignmentComponent);
		boolean wasRemoved = false;
		for (Set<Ref<T>> assignmentSet : assignmentMap.values()) {
			wasRemoved = wasRemoved || assignmentSet.remove(assignmentRef);
		}
		return wasRemoved;
	}

	@Override
	public boolean removeAssignmentIndex(T assignmentComponent) {

		if (assignmentIndex == null || assignmentComponent == null) {
			return false;
		}
		Ref<T> assignmentRef = Ref.create(assignmentComponent);
		boolean wasRemoved = assignmentIndex.remove(assignmentRef);
		if (Objects.equals(parent, assignmentRef)) {
			parent = null;
			wasRemoved = true;
		}
		return wasRemoved;
	}
	//
	//
	//

	protected void setRelationshipType(NodeRelationship relationshipType) {
		this.relationshipType = relationshipType;
	}

	protected void setReverseRelationship(boolean reverseRelationship) {
		this.reverseRelationship = reverseRelationship;
	}

	public void setRelationshipParentKey(Key<T> relationshipParentKey) {
		this.relationshipParentKey = relationshipParentKey;
	}

	public abstract Key<T> getKey();

	//
	// Private methods
	//
	private boolean finalizeThisRelationship(NodeRelationship relationship,
			Set<? extends NodeRelationship> relationshipsToFinalize) {

		return relationshipsToFinalize == null ? true : relationshipsToFinalize.contains(relationship);
	}
}
