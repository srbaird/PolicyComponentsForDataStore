
package com.bac.policydsentitycomponent.external;

import java.util.Set;

/**
 *
 * @author user0001
 */
public interface DataTypeResolver {

    DataType getEntityComponentClassDataType();

    Set<? extends DataType> getAssignableDataTypes();

    Set<? extends DataType> getNonPrimaryDataTypes();

    Set<? extends DataType> getPrimaryDataTypes();

    DataType getUnresolvedDataType();

    boolean isAggregationTagType(DataType dataType);

    boolean isAtomicDataType(DataType dataType);

    boolean isBinaryDataType(DataType dataType);

    boolean isPrimary(DataType dataType);

    boolean isReversibleDataType(DataType dataType);

    boolean isTagDataType(DataType dataType);

    DataType valueOf(String dataType);

}
