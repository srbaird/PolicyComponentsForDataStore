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
public abstract class AbstractDataTypeResolver implements DataTypeResolver {

    protected Set<? extends DataType> aggregationTagDataTypes;
    //
    protected Set<? extends DataType> assignableDataTypes;
    //
    protected Set<? extends DataType> atomicDataTypes;
    //
    protected Set<? extends DataType> binaryDataTypes;
    //
    protected Set<? extends DataType> nonPrimaryDataTypes;
    //
    protected Set<? extends DataType> primaryDataTypes;
    //
    protected Set<? extends DataType> reversibleDataTypes;
    //
    protected Set<? extends DataType> tagDataTypes;
    //
    protected final Set<? extends DataType> emptySet = Collections.emptySet();
    // logger
    protected static Logger logger = LoggerFactory.getLogger(AbstractDataTypeResolver.class);

    @Override
    public abstract DataType getEntityComponentClassDataType();

    @Override
    public Set<? extends DataType> getAssignableDataTypes() {
        return assignableDataTypes != null ? Collections.unmodifiableSet(assignableDataTypes) : emptySet;
    }

    @Override
    public Set<? extends DataType> getNonPrimaryDataTypes() {
        return nonPrimaryDataTypes != null ? Collections.unmodifiableSet(nonPrimaryDataTypes) : emptySet;
    }

    @Override
    public Set<? extends DataType> getPrimaryDataTypes() {
        return primaryDataTypes != null ? Collections.unmodifiableSet(primaryDataTypes) : emptySet;
    }

    @Override
    public abstract DataType getUnresolvedDataType();

    @Override
    public boolean isAggregationTagType(DataType dataType) {
        return aggregationTagDataTypes == null || dataType == null ? false : aggregationTagDataTypes.contains(dataType);
    }

    @Override
    public boolean isAtomicDataType(DataType dataType) {
        return atomicDataTypes == null || dataType == null ? false : atomicDataTypes.contains(dataType);
    }

    @Override
    public boolean isBinaryDataType(DataType dataType) {
        return binaryDataTypes == null || dataType == null ? false : binaryDataTypes.contains(dataType);
    }

    @Override
    public boolean isPrimary(DataType dataType) {
        return primaryDataTypes == null || dataType == null ? false : primaryDataTypes.contains(dataType);
    }

    @Override
    public boolean isReversibleDataType(DataType dataType) {
        return reversibleDataTypes == null || dataType == null ? false : reversibleDataTypes.contains(dataType);
    }

    @Override
    public boolean isTagDataType(DataType dataType) {
        return tagDataTypes == null || dataType == null ? false : tagDataTypes.contains(dataType);
    }

    @Override
    public abstract DataType valueOf(String dataType);
}
