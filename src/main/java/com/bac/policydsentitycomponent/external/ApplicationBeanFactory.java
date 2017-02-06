/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;


import java.util.Map;

/**
 *
 * @author user0001
 * @param <K>
 * @param <T>
 */
public interface ApplicationBeanFactory<K,T> {

    T createBeanByDataType(K dataType);

    void setDefaultBeanName(String beanName);

    void setBeanDataTypeMap(Map<K, String> beanNames);
}
