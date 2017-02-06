/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import static com.bac.policydsentitycomponent.external.EntityComponentNodeRelationship.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
public class DefaultNodeRelationshipResolver extends AbstractNodeRelationshipResolver implements Serializable {
    
    private static final long serialVersionUID = 70716816096164L;
    
    static {
        logger = LoggerFactory.getLogger(DefaultNodeRelationshipResolver.class);
    }
    
    {
        assignmentRelationships = EnumSet.of(ASSIGNEE_OF);
        //
        cascadeDeleteRelationships = EnumSet.of(HIERARCHY_OF, DEPENDENCY_OF);
        //
        compostionRelationships = EnumSet.of(DEPENDENCY_OF);
        //
        hierarchyRelationships = EnumSet.of(HIERARCHY_OF);
        //
        twoWayRelationships = EnumSet.of(ASSIGNEE_OF);
    }
    
    @Override 
    public Set<? extends NodeRelationship> getAllRelationships() {
  
        return Collections.unmodifiableSet(EnumSet.allOf(EntityComponentNodeRelationship.class));
    }
    
    @Override
    public NodeRelationship valueOf(String nodeRelationship) {

        //  Will throw NullPointerException if arg is null
        try {
            return EntityComponentNodeRelationship.valueOf(nodeRelationship);
        } catch (IllegalArgumentException e) {
            logger.warn("Unable to derive Node Relationship value of '{}'", nodeRelationship);
            return null;
        }
    }
    
}
