package com.bac.policydsentitycomponent.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.bac.policydsentitycomponent.external.AbstractBeanFactory;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.EntityComponentFactory;



/**
 *
 * @author Simon Baird
 */
public class PolicyDSEntityComponentFactory extends AbstractBeanFactory<DataType, PolicyDSEntity>
        implements EntityComponentFactory<DataType, PolicyDSEntity>, Serializable {

    private static final long serialVersionUID = 5707558775708L;

    private Map<DataType, Class<PolicyDSEntity>> componentClassMap;

    @SuppressWarnings("unchecked")
	@Override
    public Class<PolicyDSEntity> getEntityComponentClass(DataType type) {
    	
        if (componentClassMap == null) {
            componentClassMap = new HashMap<>();
        }
        if (!componentClassMap.containsKey(type)) {
            PolicyDSEntity classArchetype = createBeanByDataType(type);
            componentClassMap.put(type, (Class<PolicyDSEntity>) classArchetype.getClass());
        }
        return componentClassMap.get(type);
    }

    @Override
    public PolicyDSEntity createBean(String beanName) {

        ApplicationContext applicationContext = getApplicationContext();
        PolicyDSEntity returnProxy = applicationContext.getBean(beanName, PolicyDSEntity.class);
        
        return returnProxy;
    }
}
