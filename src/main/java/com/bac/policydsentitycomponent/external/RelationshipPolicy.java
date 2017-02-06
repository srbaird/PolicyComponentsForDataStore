
package com.bac.policydsentitycomponent.external;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Simon Baird
 */
public interface RelationshipPolicy {

    void setRelationshipMapping(Map<DataType, Map<DataType, NodeRelationship>> relationshipMapping);

    public void setRelationshipMapping(List<? extends ClassEntityComponent> policyComponents);

    List<EntityComponent> getPolicyComponents();

    NodeRelationship getRelationship(DataType from, DataType to);

    boolean hasRelationship(DataType from, DataType to);

    boolean canAssign(DataType from, DataType to);

    boolean isTwoWayRelationship(NodeRelationship relationship);

    Set<DataType> getPolicySet();

    Map<DataType, NodeRelationship> getRelationshipMap(DataType dataType);

    int getTransferType(EntityComponent assignee, EntityComponent assignment);

    boolean isGroupDataType(DataType dataType);
    
    boolean isDependency(DataType dataType);
}
