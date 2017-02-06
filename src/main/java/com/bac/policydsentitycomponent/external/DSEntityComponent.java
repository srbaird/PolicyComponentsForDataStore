/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import java.util.Map;
import java.util.Set;

import com.googlecode.objectify.Ref;

/**
 *
 * @author user0001
 * @param <T>
 */
public interface DSEntityComponent<T> {

    void addAssignee(NodeRelationship relationship, T assigneeComponent);

    void addAssignment(NodeRelationship relationship, T assignmentComponent);

    void addAssignmentIndex(T assignmentComponent);

    void finalizeLoad(NodeRelationshipResolver nodeRelationshipResolver, T rootComponent);

    void finalizeLoad(NodeRelationshipResolver nodeRelationshipResolver, Set<? extends NodeRelationship> relationshipsToBuild, T rootComponent);

    Set<Ref<T>> getAssignmentIndex();

    Map<String, Set<Ref<T>>> getAssignmentMap();

    Ref<T> getParent();

    void populateEntityComponent(EntityComponent entityComponent);

    boolean removeAssignee(T assigneeComponent);

    boolean removeAssignment(T assignmentComponent);

    boolean removeAssignmentIndex(T assignmentComponent);

    void setParent(T parentComponent);
}
