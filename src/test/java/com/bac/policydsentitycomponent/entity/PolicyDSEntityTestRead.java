
package com.bac.policydsentitycomponent.entity;

import static com.bac.policydsentitycomponent.external.EntityComponentDataType.ENTITY_COMPONENT_CLASS;
import static com.bac.policydsentitycomponent.external.EntityComponentDataType.TAG;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.external.DataType;

/**
 * Test to the read methods of the Data Accessor. There are a number of list..()
 * methods...
 * 
 * List<T> listComponentsByProperty(NodeProperty nodeProperty); List<T>
 * listComponentsByProperty(DataType dataType, NodeProperty nodeProperty);
 * List<T> listComponentsByProperty(DataType dataType, NodeProperty
 * nodeProperty, Object startRange, Object endRange); List<T>
 * listRootNodes(DataType datatype); List<T> listRootNodesSimple(DataType
 * datatype);
 * 
 * ... and read_() methods
 * 
 * @author Simon Baird
 */
public class PolicyDSEntityTestRead extends AbstractEntityTest {

	static {
		logger = LoggerFactory.getLogger(PolicyDSEntityTestRead.class);
	}
	//
	final DataType classDataType = ENTITY_COMPONENT_CLASS;

	/**
	 * A list of Root nodes read by datatype should only contain those entities
	 * with the correct data type. This implementation of the accessor only
	 * supports one datatype so testing is limited.
	 */
	@Test
	public void a_List_By_Datatype_Should_Only_Contain_Entities_With_That_Datatype() {

		logger.info("a_List_By_Datatype_Should_Only_Contain_Entities_With_That_Datatype");
		//
		DataType listPolicyDataType = mock(DataType.class);
		final int LIMIT = 1000;
		for (int i = 0; i < LIMIT; i++) {
			//
			instance.createPolicyComponent(getMockClassEntityComponent(listPolicyDataType));
		}
		//
		List<? extends PolicyDSEntity> nodes = instance.listPolicies();
		assertEquals(LIMIT, nodes.size());
		//
		nodes.stream().forEach(node -> assertEquals(classDataType, node.getDataType()));
	}

	/**
	 * A List of nodes may be selected by a property of the Node. In this case
	 * the wrapped data type may be used to form a list. Note that if this is
	 * required then the data type value in the DS entity class should be
	 * annotated with '@Index'
	 */
	@Test
	public void a_List_By_Property_Should_Only_Contain_Entities_Where_That_Property_Is_Not_Null() {

		logger.info("a_List_By_Property_Should_Only_Contain_Entities_Where_That_Property_Is_Not_Null");
		//
		DataType listPolicyDataType = TAG;
		final int LIMIT = 1000;
		for (int i = 0; i < LIMIT; i++) {
			//
			instance.createPolicyComponent(getMockClassEntityComponent(listPolicyDataType));
		}
		//
		List<? extends PolicyDSEntity> nodes = instance.listPolicies();
		assertEquals(LIMIT, nodes.size());
		//
		nodes.stream().forEach(node -> assertEquals(listPolicyDataType, node.getPolicyDataType()));
	}
}
