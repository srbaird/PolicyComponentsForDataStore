/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
public abstract class AbstractNodePropertyResolver implements NodePropertyResolver {

    // logger
    protected static Logger logger = LoggerFactory.getLogger(AbstractNodePropertyResolver.class);
    //
    protected Set<? extends NodeProperty> indexSet;
    //
    protected Map<DataType, Set<? extends NodeProperty>> validPropertiesMap;
    //
    protected Map<DataType, Set<? extends NodeProperty>> mandatoryPropertiesMap;
    //
    private Map<DataType, Set<? extends NodeProperty>> emptyMap = Collections.emptyMap();
    //
    private Set<? extends NodeProperty> emptySet = Collections.emptySet();
    //
    protected final String NULL_DATATYPE = "Null Data Type supplied";

    public Map<DataType, Set<? extends NodeProperty>> getValidPropertiesMap() {

        return validPropertiesMap == null ? emptyMap : Collections.unmodifiableMap(validPropertiesMap);
    }

    public void setValidPropertiesMap(Map<DataType, Set<? extends NodeProperty>> validPropertiesMap) {

        this.validPropertiesMap = validPropertiesMap;
    }

    public Map<DataType, Set<? extends NodeProperty>> getMandatoryPropertiesMap() {

        return mandatoryPropertiesMap == null ? emptyMap : Collections.unmodifiableMap(mandatoryPropertiesMap);
    }

    public void setMandatoryPropertiesMap(Map<DataType, Set<? extends NodeProperty>> mandatoryPropertiesMap) {

        this.mandatoryPropertiesMap = mandatoryPropertiesMap;
    }

    @Override
    public Set<? extends NodeProperty> getIndexSet() {
        
        return indexSet == null ? emptySet : Collections.unmodifiableSet(indexSet);
    }

    @Override
    public Set<? extends NodeProperty> getMandatoryNodeProperties(DataType dataType) {
        if (dataType == null) {
            throw new IllegalArgumentException(NULL_DATATYPE);
        }
        Set<? extends NodeProperty> emptySet = Collections.emptySet();
        if (mandatoryPropertiesMap == null || !mandatoryPropertiesMap.containsKey(dataType)) {
            return emptySet;
        }
        Set<? extends NodeProperty> returnSet = mandatoryPropertiesMap.get(dataType);
        return returnSet != null ? Collections.unmodifiableSet(returnSet) : emptySet;
    }

    @Override
    public Set<? extends NodeProperty> getNodeProperties(DataType dataType) {
        if (dataType == null) {
            throw new IllegalArgumentException(NULL_DATATYPE);
        }
        Set<? extends NodeProperty> emptySet = Collections.emptySet();
        if (validPropertiesMap == null || !validPropertiesMap.containsKey(dataType)) {
            return emptySet;
        }
        Set<? extends NodeProperty> returnSet = validPropertiesMap.get(dataType);
        return returnSet != null ? Collections.unmodifiableSet(returnSet) : emptySet;
    }

    @Override
    public abstract NodeProperty getPolicyDataTypeNodeProperty();

    @Override
    public abstract NodeProperty valueOf(String nodeProperty);

}
