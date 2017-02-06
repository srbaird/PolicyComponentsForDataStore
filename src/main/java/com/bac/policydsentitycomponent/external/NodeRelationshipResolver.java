
package com.bac.policydsentitycomponent.external;

import java.util.Set;

/**
 *
 * @author Simon Baird
 */
public interface NodeRelationshipResolver {

    boolean equivalent(NodeRelationship r1, NodeRelationship r2);

    Set<? extends NodeRelationship> getAllRelationships();

    Set<? extends NodeRelationship> getAssignmentRelationships();

    Set<? extends NodeRelationship> getCascadeDeleteRelationships();

    Set<? extends NodeRelationship> getCompostionRelationships();

    boolean isAssignmentRelationship(NodeRelationship relationship);

    Set<? extends NodeRelationship> getHierarchyRelationships();

    boolean isCascadeDeleteRelationship(NodeRelationship nodeRelationship);

    boolean isDependencyRelationship(NodeRelationship nodeRelationship);

    boolean isHierarchyRelationship(NodeRelationship nodeRelationship);

    boolean isTwoWayRelationship(NodeRelationship nodeRelationship);

    NodeRelationship valueOf(String nodeRelationship);
}
