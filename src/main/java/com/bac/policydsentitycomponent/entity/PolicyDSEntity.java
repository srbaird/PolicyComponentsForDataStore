/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.external.AbstractDSEntityComponent;
import com.bac.policydsentitycomponent.external.ClassEntityComponent;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.EntityComponent;
import com.bac.policydsentitycomponent.external.EntityComponentDataType;
import com.bac.policydsentitycomponent.external.NodeRelationship;
import com.bac.policydsentitycomponent.external.NodeRelationshipResolver;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/**
 *
 * @author user0001
 */
@Entity
public class PolicyDSEntity extends AbstractDSEntityComponent<PolicyDSEntity> implements ClassEntityComponent {

    private static final long serialVersionUID = 93052916826885L;

    static {
        logger = LoggerFactory.getLogger(PolicyDSEntity.class);
    }
    //
    //  Policy assignment map. Ensures that only one relationship can exist for a Policy. 
    //  Load must be done manually 
    //
    protected Map<String, AssignmentWrapper> policyAssignmentMap;
    //
    @Index
    private String policyDataType;

    @Override
    public Key<PolicyDSEntity> getKey() {
        return Key.create(this);
    }
    //
    //  Override the assignment map accessor to return the Policy Assignment Map
    //  for the Delete processing
    //
    @Override
    public Map<String, Set<Ref<PolicyDSEntity>>> getAssignmentMap() {
        
        Map<String, Set<Ref<PolicyDSEntity>>> proxyAssignmentMap = new HashMap<>();
        if (policyAssignmentMap == null) {
            return proxyAssignmentMap;
        }
        for (AssignmentWrapper entry : policyAssignmentMap.values()) {
            
            String relationshipName = entry.getNodeRelationshipName();
            Set<Ref<PolicyDSEntity>> keySet = proxyAssignmentMap.get(relationshipName);
            if (keySet == null) {
                keySet = new HashSet<>();
            }
            keySet.add(entry.getAssignmnent());
            proxyAssignmentMap.put(relationshipName, keySet);
        }
        return proxyAssignmentMap;
    }
    //
    //  As the Datastore 'kind' is mapped to the class then all data types will need
    //  to be subclassed in which case the data type can be associated with the domain
    //  data type enum
    //

    @Override
    public DataType getDataType() {

        return dataType == null ? null : EntityComponentDataType.valueOf(dataType);
    }

    @Override
    public DataType getPolicyDataType() {
        return policyDataType == null ? null : EntityComponentDataType.valueOf(policyDataType);
    }

    @Override
    public void setPolicyDataType(DataType policyDataType) {
        this.policyDataType = policyDataType == null ? null : policyDataType.name();
    }

    @Override
    public void populateEntityComponent(EntityComponent entityComponent) {

        super.populateEntityComponent(entityComponent);
        if (entityComponent == null) {
            return;
        }
        if (entityComponent instanceof ClassEntityComponent) {
            setPolicyDataType(((ClassEntityComponent) entityComponent).getPolicyDataType());
        } else {

            setPolicyDataType(null);
        }
    }

    @Override
    public void addAssignee(NodeRelationship relationship, PolicyDSEntity assigneeComponent) {
        //
        //  Ignore assignees for Policy entities
        //
    }

    @Override
    public boolean removeAssignee(PolicyDSEntity assigneeComponent) {

        return false;
    }

    @Override
    protected void finalizeAssignees(NodeRelationshipResolver relationshipResolver, Set<? extends NodeRelationship> relationshipsToBuild, PolicyDSEntity rootComponent) {
        //
        //  Ignore assignees for Policy entities
        //
    }

    @Override
    public void addAssignment(NodeRelationship relationship, PolicyDSEntity assignmentComponent) {

        if (policyAssignmentMap == null) {
            policyAssignmentMap = new HashMap<>();
        }
        String key = relationship == null ? "Unknown" : relationship.name();
        Ref<PolicyDSEntity> assignmentRef = Ref.create(assignmentComponent);
        policyAssignmentMap.put(assignmentComponent.getId().toString(), new AssignmentWrapper(assignmentRef, key));
    }

    @Override
    public boolean removeAssignment(PolicyDSEntity assignmentComponent) {

        if (policyAssignmentMap == null) {
            return false;
        }
        String key = assignmentComponent.getId().toString();
        AssignmentWrapper remove = policyAssignmentMap.remove(key);
        return remove != null;
    }

    @Override
    protected void finalizeAssignments(NodeRelationshipResolver relationshipResolver, Set<? extends NodeRelationship> relationshipsToBuild, PolicyDSEntity rootComponent) {

        entityComponents.clear();

        if (policyAssignmentMap != null) {
            for (Map.Entry<String, AssignmentWrapper> assignmentEntry : policyAssignmentMap.entrySet()) {
                String relationshipName = assignmentEntry.getValue().getNodeRelationshipName();
                Ref<PolicyDSEntity> entityItem = assignmentEntry.getValue().getAssignmnent();
                PolicyDSEntity assignmentComponent = entityItem.get();
                if (assignmentComponent == null) {
                    continue;
                }
                //
                //  No recursive build on the assignments
                //
                NodeRelationship relationship = relationshipResolver.valueOf(relationshipName);
                PolicyDSEntityRelationshipProxy assignmentRelationship = new PolicyDSEntityRelationshipProxy(assignmentComponent);
                assignmentRelationship.setRelationshipId(getKey());
                assignmentRelationship.setRelationshipType(relationship);
                entityComponents.add(assignmentRelationship);
            }
        }
    }

    private static class AssignmentWrapper implements Serializable {

        private static final long serialVersionUID = 93052916826886L;

        private Ref<PolicyDSEntity> assignment;
        private String nodeRelationshipName;


        public AssignmentWrapper(Ref<PolicyDSEntity> assignmnent, String nodeRelationshipName) {
            this.assignment = assignmnent;
            this.nodeRelationshipName = nodeRelationshipName;
        }

        public Ref<PolicyDSEntity> getAssignmnent() {
            return assignment;
        }


        public String getNodeRelationshipName() {
            return nodeRelationshipName;
        }

    }
}
