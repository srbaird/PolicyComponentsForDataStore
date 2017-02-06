
package com.bac.policydsentitycomponent.entity;

import static com.bac.policydsentitycomponent.external.EntityComponentDataType.IMAGE_COMPONENT;
import static com.bac.policydsentitycomponent.external.EntityComponentDataType.INLINE_NOTE;
import static com.bac.policydsentitycomponent.external.EntityComponentNodeRelationship.DEPENDENCY_OF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.EntityComponents;
import com.bac.policydsentitycomponent.external.NodeRelationship;

/**
 * Deleting associations retains the nodes but removes the relationships between
 * them.
 * 
 * @author Simon Baird
 */
public class PolicyDSEntityTestDeleteAssociations extends AbstractEntityTest {

	static {
		logger = LoggerFactory.getLogger(PolicyDSEntityTestDeleteAssociations.class);
	}

	//
	/**
	 * It should be possible only to remove an association using a related
	 * component.
	 */
	@Test
	public void a_Deleted_Association_Should_Remove_Relationships_Between_Nodes() {

		logger.info("a_Deleted_Association_Should_Remove_Relationships_Between_Nodes");
		//
		DataType expRootDataType = IMAGE_COMPONENT;
		DataType expNodeDataType = INLINE_NOTE;
		NodeRelationship expRelationshipType = DEPENDENCY_OF;
		//
		PolicyDSEntity rootComponent;
		PolicyDSEntity nodeComponent;
		{
			//
			// Create the Root, Node and add a Relationship
			//
			PolicyDSEntity createdRootEntityComponent = instance
					.createPolicyComponent(getMockClassEntityComponent(expRootDataType));
			PolicyDSEntity createdNodeEntityComponent = instance
					.createPolicyComponent(getMockClassEntityComponent(expNodeDataType));
			createdRootEntityComponent = instance.createPolicyAssociation(createdRootEntityComponent,
					createdNodeEntityComponent, expRelationshipType);
			//
			// Read them back
			//
			rootComponent = instance.readPolicyComponent(createdRootEntityComponent);
			nodeComponent = instance.readPolicyComponent(createdNodeEntityComponent);
		}
		//
		final int EXPECTED_RELATIONSHIPS_SIZE = 1;
		assertEquals(EXPECTED_RELATIONSHIPS_SIZE, rootComponent.getEntityComponents().size());

		final int INDEX_OF_FIRST_ELEMENT = 0;
		final PolicyDSEntityRelationshipProxy relatedComponent = (PolicyDSEntityRelationshipProxy) rootComponent
				.getEntityComponents().get(INDEX_OF_FIRST_ELEMENT);
		assertTrue(EntityComponents.equals(nodeComponent, relatedComponent));
		//
		//
		//
		instance.deletePolicyAssociation(relatedComponent);
		rootComponent = instance.readPolicyComponent(rootComponent);
		assertEquals(0, rootComponent.getEntityComponents().size());

	}

}
