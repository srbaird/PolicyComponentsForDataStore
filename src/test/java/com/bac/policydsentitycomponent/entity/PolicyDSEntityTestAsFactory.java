
package com.bac.policydsentitycomponent.entity;

import static com.bac.policydsentitycomponent.external.EntityComponentDataType.ENTITY_COMPONENT_CLASS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.external.DataType;

/**
 * A Data Accessor also functions as a factory to generate components entity
 * components that are entity component proxies. The reduced set of methods for
 * handling policy data only specifies one factory method
 * 
 * @author Simon Baird
 */
public class PolicyDSEntityTestAsFactory extends AbstractEntityTest {

	static {
		logger = LoggerFactory.getLogger(PolicyDSEntityTestAsFactory.class);
	}
	//
	final DataType classDataType = ENTITY_COMPONENT_CLASS;

	/**
	 * The implementation of this data accessor only supports creation from one
	 * type.
	 * 
	 */
	@Test
	public void accesor_Should_Create_Proxy_Object_For_Valid_DataType() {

		logger.info("accesor_Should_Create_Proxy_Object_For_Valid_DataType");

		PolicyDSEntity newPolicyComponent = instance.newPolicyComponent();
		assertNotNull(newPolicyComponent);
		assertEquals(classDataType, newPolicyComponent.getDataType());
	}
}
