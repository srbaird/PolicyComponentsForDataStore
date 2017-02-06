/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;


import static com.bac.policydsentitycomponent.external.EntityComponentDataType.*;

import java.io.Serializable;
import java.util.EnumSet;

import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
public class DefaultDataTypeResolver extends AbstractDataTypeResolver implements Serializable {

    private static final long serialVersionUID = 383896045761171L;

    static {
        logger = LoggerFactory.getLogger(DefaultDataTypeResolver.class);
    }

    DataType entityComponentClassDataType = ENTITY_COMPONENT_CLASS;

    DataType unresolvedDataType = UNRESOLVED;

    {
        aggregationTagDataTypes = EnumSet.of(AGGREGATION_TAG);
        //
        assignableDataTypes = EnumSet.complementOf(EnumSet.of(ENTITY_COMPONENT_CLASS, UNRESOLVED));
        //
        atomicDataTypes = EnumSet.of(INLINE_NOTE);
        //
        binaryDataTypes = EnumSet.of(IMAGE_COMPONENT);
        //
        nonPrimaryDataTypes = EnumSet.of(ENTITY_COMPONENT_CLASS, INLINE_NOTE);
        //
        primaryDataTypes = EnumSet.complementOf(EnumSet.of(ENTITY_COMPONENT_CLASS, INLINE_NOTE));
        //
        reversibleDataTypes = EnumSet.of(TAG, AGGREGATION_TAG);
        //
        tagDataTypes = EnumSet.of(TAG);
    }

    @Override
    public DataType getEntityComponentClassDataType() {

        return entityComponentClassDataType;
    }

    @Override
    public DataType getUnresolvedDataType() {
        return unresolvedDataType;
    }

    @Override
    public DataType valueOf(String dataType) {

        //  Will throw NullPointerException if arg is null
        try {
            return EntityComponentDataType.valueOf(dataType);
        } catch (IllegalArgumentException e) {
            logger.debug("Unable to derive Data Type value of '{}'", dataType);
            return getUnresolvedDataType();
        }
    }
}
