/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author user0001
 * @param <K>
 * @param <T>
 */
public abstract class AbstractBeanFactory<K,T> implements ApplicationBeanFactory<K,T>, Serializable {
    
	private static final long serialVersionUID = 2514510121253111894L;
	
	@Autowired
	transient protected ApplicationContext appContext;
    //
    protected Map<K, String> beanDataTypeMap = new HashMap<>();
    //
    protected String defaultBeanName;
    //
    protected ApplicationContextProvider provider;
    //
    protected final String NULL_DEFAULT_BEAN_NAME = " Unable to generate a component for '%s'";
    protected final String UNSUPPORTED_OPERATION_FORMAT = " Method '%s' is not supported";
    //
    protected static Logger logger = LoggerFactory.getLogger(AbstractBeanFactory.class);

    @Override
    public void setBeanDataTypeMap(Map<K, String> beanNames) {
        beanDataTypeMap = beanNames;
    }

    @Override
    public void setDefaultBeanName(String beanName) {
        defaultBeanName = beanName;
    }

    public ApplicationContextProvider getApplicationContextProvider() {
        return provider;
    }

    public void setApplicationContextProvider(ApplicationContextProvider provider) {
        this.provider = provider;
    }

    protected ApplicationContext getApplicationContext() {

        return appContext;
    }

    @Override
    public T createBeanByDataType(K dataType) {

        String beanName = defaultBeanName;
        if (beanDataTypeMap != null && beanDataTypeMap.containsKey(dataType)) {
            beanName = beanDataTypeMap.get(dataType);
        }
        if (beanName == null) {
            throw new IllegalStateException(String.format(NULL_DEFAULT_BEAN_NAME, dataType == null ? "Not Supplied" : dataType));
        }
        T bean = createBean(beanName);
        return bean;
    }

    public T createBean(String beanName) {

        @SuppressWarnings("unchecked")
        T bean = (T) getApplicationContext().getBean(beanName);
        return bean;
    }
}
