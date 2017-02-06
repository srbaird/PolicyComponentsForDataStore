/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import static com.bac.policydsentitycomponent.external.EntityComponentNodeProperty.*;
import java.io.Serializable;
import java.util.EnumSet;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
public class DefaultNodePropertyResolver extends AbstractNodePropertyResolver implements Serializable {

	private static final long serialVersionUID = -6818585462061731979L;

	static {
        logger = LoggerFactory.getLogger(DefaultNodePropertyResolver.class);
    }
    protected NodeProperty policyDatatypeNodeProperty = POLICYDATATYPE;

    {
        indexSet = EnumSet.of(VALUEDATE);
    }

    @Override
    public NodeProperty getPolicyDataTypeNodeProperty() {
        return policyDatatypeNodeProperty;
    }

    @Override
    public NodeProperty valueOf(String nodeProperty) {

        //  Will throw NullPointerException if arg is null
        try {
            return EntityComponentNodeProperty.valueOf(nodeProperty);
        } catch (IllegalArgumentException e) {
            logger.warn("Unable to derive Node Property value of '{}'", nodeProperty);
            return null;
        }
    }
}
