
package com.bac.policydsentitycomponent.entity;

import static com.bac.policydsentitycomponent.external.EntityComponentDataType.ENTITY_COMPONENT_CLASS;
import static com.bac.policydsentitycomponent.external.EntityComponentDataType.IMAGE_COMPONENT;
import static com.bac.policydsentitycomponent.external.EntityComponentDataType.TAG;
import static com.bac.policydsentitycomponent.external.EntityComponentNodeRelationship.HIERARCHY_OF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.external.ClassEntityComponent;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.EntityComponents;
import com.bac.policydsentitycomponent.external.NodeRelationship;

/**
 * Direct updates to policy entities are rare, they only have one mutable
 * attribute. At an application level this may be undesirable anyway.
 * Nevertheless the update is tested for completeness
 * 
 * @author Simon Baird
 */
public class PolicyDSEntityTestUpdate extends AbstractEntityTest {

	static {
		logger = LoggerFactory.getLogger(PolicyDSEntityTestUpdate.class);
	}
	//
	final DataType classDataType = ENTITY_COMPONENT_CLASS;

	/**
	 * An entity should persist changes to it's PolicyDataType.
	 */
	@Test
	public void changes_To_PolicyDataType_Should_Be_Persisted() {

		logger.info("changes_To_PolicyDataType_Should_Be_Persisted");
		//
		DataType initialDataType = IMAGE_COMPONENT;
		DataType updateDataType = TAG;
		ClassEntityComponent mockPolicyComponent = getMockClassEntityComponent(initialDataType);
		PolicyDSEntity createdEntityComponent = instance.createPolicyComponent(mockPolicyComponent);
		{
			createdEntityComponent.setPolicyDataType(updateDataType);
			instance.updatePolicyComponent(createdEntityComponent);
		}
		//
		ClassEntityComponent updatedComponent = instance.readPolicyComponent(createdEntityComponent);
		assertEquals(updateDataType, updatedComponent.getPolicyDataType());
	}

	/**
	 * A Root (Root -> Relationship -> Node) which is updated should retain the
	 * same assignments as before. That is, only the PolicyDataType should
	 * change
	 */
	@Test
	public void a_Root_With_Assignments_Should_Retain_Them_After_Update() {

		logger.info("a_Node_Should_Appear_In_Assignments_Once_A_Relationship_Is_Established");
		//
		final DataType initialDataType = mock(DataType.class);
		final DataType updateDataType = mock(DataType.class);
		final NodeRelationship expRelationshipType = HIERARCHY_OF;
		//
		// Create the relationship
		//
		PolicyDSEntity createdRootEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(initialDataType));
		final PolicyDSEntity createdNodeEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(updateDataType));

		createdRootEntityComponent = instance.createPolicyAssociation(createdRootEntityComponent,
				createdNodeEntityComponent, expRelationshipType);
		//
		{
			createdRootEntityComponent.setPolicyDataType(updateDataType);
			instance.updatePolicyComponent(createdRootEntityComponent);

		}
		createdRootEntityComponent = instance.readPolicyComponent(createdRootEntityComponent);
		final int single = 1;
		assertEquals(single, createdRootEntityComponent.getEntityComponents().size());
		final int INDEX_OF_FIRST_ITEM = 0;
		assertTrue(EntityComponents.equals(createdNodeEntityComponent,
				createdRootEntityComponent.getEntityComponents().get(INDEX_OF_FIRST_ITEM)));
	}
}
