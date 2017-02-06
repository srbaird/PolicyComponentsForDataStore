/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.access;

import static com.bac.policydsentitycomponent.access.OfyService.ofy;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.entity.PolicyDSEntity;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.NodeRelationship;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

/**
 *
 * @author user0001
 */
public class DataAccessor extends AbstractDataAccessor<PolicyDSEntity> {

    static {
        logger = LoggerFactory.getLogger(DataAccessor.class);
    }
    private static final long serialVersionUID = 436837696393727L;

    @Override
    protected Objectify getOfy() {
        return ofy();
    }

    //
    //  Initialize the relationship policy 
    //
//    @Override
    public void _init() {
        //
        super.init();
        //
        ObjectifyService.run(new Work<Object>() {

            @Override
            public Object run() {
                List<PolicyDSEntity> policyNodes = listRootNodes(dataTypeResolver.getEntityComponentClassDataType());
                relationshipPolicy.setRelationshipMapping(policyNodes);
                logger.info("Added {} items to the Relationship Policy", policyNodes == null ? 0 : policyNodes.size());
                return null;
            }
        });
    }

    //
    //  Override the root node list build. The default behaviour requires that only components that have no hierarchy relationships
    //  are returned. For policy components this means that items which have a hierarchy relationship will not be supplied. If 
    //  the datatype is a Class component datatype then override this behaviour.
    //

    @Override
    protected List<PolicyDSEntity> buildRootNodesList(DataType datatype, Set<? extends NodeRelationship> relationshipsToBuild) {
        if (datatype == null) {
            return null;
        }
        setUserNameSpace();

        List<PolicyDSEntity> returnNodes = new LinkedList<>();
        // Build the query
        LoadType<? extends PolicyDSEntity> loadType = getLoadType(getOfy().load(), datatype);

        List<? extends PolicyDSEntity> rootNodes;
        //
        //  Only provide null parent filter on non-policy datatypes
        //      
        if (dataTypeResolver.getEntityComponentClassDataType() == datatype) {
            rootNodes = loadType.list();
        } else {    // Add the filter            
            Query<? extends PolicyDSEntity> query = loadType.filter(getNullFilter(PARENT_INDEX));
            rootNodes = query.list();
        }
        //
        for (PolicyDSEntity rootNode : rootNodes) {
            returnNodes.add(_buildEntityComponent(rootNode, relationshipsToBuild));
        }
        return returnNodes;
    }

}
