/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import java.util.Collections;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
public abstract class AbstractNodeRelationshipResolver implements NodeRelationshipResolver {

    protected Set<? extends NodeRelationship> assignmentRelationships;
    //
    protected Set<? extends NodeRelationship> cascadeDeleteRelationships;
    //
    protected Set<? extends NodeRelationship> compostionRelationships;
    //
    protected Set<? extends NodeRelationship> hierarchyRelationships;
    //
    protected Set<? extends NodeRelationship> twoWayRelationships;
    //
    protected final Set<? extends NodeRelationship> emptySet = Collections.emptySet();
    // logger
    protected static Logger logger = LoggerFactory.getLogger(AbstractNodeRelationshipResolver.class);

    @Override
    public boolean equivalent(NodeRelationship r1, NodeRelationship r2) {

        return r1 == null || r2 == null ? false : r1 == r2;
    }

    @Override
    public Set<? extends NodeRelationship> getAssignmentRelationships() {

        return assignmentRelationships != null ? Collections.unmodifiableSet(assignmentRelationships) : emptySet;
    }

    @Override
    public Set<? extends NodeRelationship> getCascadeDeleteRelationships() {

        return cascadeDeleteRelationships != null ? Collections.unmodifiableSet(cascadeDeleteRelationships) : emptySet;
    }

    @Override
    public Set<? extends NodeRelationship> getCompostionRelationships() {

        return compostionRelationships != null ? Collections.unmodifiableSet(compostionRelationships) : emptySet;
    }

    @Override
    public Set<? extends NodeRelationship> getHierarchyRelationships() {

        return hierarchyRelationships != null ? Collections.unmodifiableSet(hierarchyRelationships) : emptySet;
    }

    @Override
    public boolean isAssignmentRelationship(NodeRelationship nodeRelationship) {

        return assignmentRelationships == null || nodeRelationship == null ? false : assignmentRelationships.contains(nodeRelationship);
    }

    @Override
    public boolean isCascadeDeleteRelationship(NodeRelationship nodeRelationship) {
        return cascadeDeleteRelationships == null || nodeRelationship == null ? false : cascadeDeleteRelationships.contains(nodeRelationship);
    }

    @Override
    public boolean isDependencyRelationship(NodeRelationship nodeRelationship) {

        return compostionRelationships == null || nodeRelationship == null ? false : compostionRelationships.contains(nodeRelationship);
    }

    @Override
    public boolean isHierarchyRelationship(NodeRelationship nodeRelationship) {

        return hierarchyRelationships == null || nodeRelationship == null ? false : hierarchyRelationships.contains(nodeRelationship);
    }

    @Override
    public boolean isTwoWayRelationship(NodeRelationship nodeRelationship) {

        return twoWayRelationships == null || nodeRelationship == null ? false : twoWayRelationships.contains(nodeRelationship);
    }

    @Override
    public abstract NodeRelationship valueOf(String nodeRelationship);

}
