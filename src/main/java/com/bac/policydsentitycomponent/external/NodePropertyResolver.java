/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import java.util.Set;

/**
 *
 * @author user0001
 */
public interface NodePropertyResolver {

    Set<? extends NodeProperty> getIndexSet();

    Set<? extends NodeProperty> getMandatoryNodeProperties(DataType dataType);

    Set<? extends NodeProperty> getNodeProperties(DataType dataType);

    NodeProperty getPolicyDataTypeNodeProperty();

    NodeProperty valueOf(String nodeProperty);
}
