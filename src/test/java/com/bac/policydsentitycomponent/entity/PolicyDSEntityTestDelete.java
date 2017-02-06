
package com.bac.policydsentitycomponent.entity;

import static com.bac.policydsentitycomponent.external.EntityComponentDataType.ENTITY_COMPONENT_CLASS;
import static com.bac.policydsentitycomponent.external.EntityComponentDataType.IMAGE_COMPONENT;
import static com.bac.policydsentitycomponent.external.EntityComponentDataType.INLINE_NOTE;
import static com.bac.policydsentitycomponent.external.EntityComponentNodeRelationship.DEPENDENCY_OF;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.external.ClassEntityComponent;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.NodeRelationship;

/**
 * Deleting Class components enforces different constraints. No cascade deletes
 * are permitted despite the relationship type
 * 
 * @author user0001
 */
public class PolicyDSEntityTestDelete extends AbstractEntityTest {

	static {
		logger = LoggerFactory.getLogger(PolicyDSEntityTestDelete.class);
	}
	//
	final DataType classDataType = ENTITY_COMPONENT_CLASS;

	public PolicyDSEntityTestDelete() {

	}

	/**
	 * Create a Policy and delete it. It should no longer appear in the data
	 * store
	 */
	@Test
	public void a_Deleted_Policy_Should_Be_Removed_From_The_Datastore() {

		logger.info("a_Deleted_Policy_Should_Be_Removed_From_The_Datastore");
		//
		ClassEntityComponent mockPolicyComponent = getMockClassEntityComponent();
		//
		PolicyDSEntity createdEntityComponent = instance.createPolicyComponent(mockPolicyComponent);
		assertNotNull(createdEntityComponent);
		//
		instance.deletePolicy(createdEntityComponent);
		//
		// Should not longer exist
		//
		createdEntityComponent = instance.readPolicyComponent(mockPolicyComponent);
		assertNull(createdEntityComponent);
	}

	/**
	 * A dependency relationship ( Root -> DEPENDENCY_OF -> Node) should not
	 * cascade when the owner ('Root') is deleted. That is, the 'Node' should
	 * not be deleted
	 */
	@Test
	public void deleting_A_Root_That_Has_A_Dependency_Relationship_Should_Not_Delete_The_Node() {

		logger.info("deleting_A_Root_That_Has_A_Dependency_Relationship_Should_Not_Delete_The_Node");
		//
		DataType expRootDataType = IMAGE_COMPONENT;
		DataType expNodeDataType = INLINE_NOTE;
		NodeRelationship expRelationshipType = DEPENDENCY_OF;
		//
		// Create the Root
		//
		PolicyDSEntity createdRootEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expRootDataType));
		//
		// Create the Node
		//
		PolicyDSEntity createdNodeEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expNodeDataType));
		//
		// Assign the Node to the Root
		//
		createdRootEntityComponent = instance.createPolicyAssociation(createdRootEntityComponent,
				createdNodeEntityComponent, expRelationshipType);
		//
		// Delete the Root
		//
		instance.deletePolicy(createdRootEntityComponent);
		//
		// Read back the Root item
		//
		createdNodeEntityComponent = instance.readPolicyComponent(createdNodeEntityComponent);
		assertNotNull(createdNodeEntityComponent);
	}

	/**
	 * When a relationship ( Root -> Relationship -> Node) exists then deleting
	 * Root policy should result in the Node policy should no longer have a
	 * (reverse) relationship to the root
	 *
	 */
	@Test
	public void deleting_A_Root_That_Has_A_Dependency_Relationship_Should_Remove_Reverse_Relationship_To_Node() {

		logger.info("deleting_A_Root_That_Has_A_Dependency_Relationship_Should_Remove_Reverse_Relationship_To_Node");
		//
		DataType expRootDataType = IMAGE_COMPONENT;
		DataType expNodeDataType = INLINE_NOTE;
		NodeRelationship expRelationshipType = DEPENDENCY_OF;
		//
		// Create the Root
		//
		PolicyDSEntity createdRootEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expRootDataType));
		//
		// Create the Node
		//
		PolicyDSEntity createdNodeEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expNodeDataType));
		//
		// Assign the Node to the Root
		//
		createdRootEntityComponent = instance.createPolicyAssociation(createdRootEntityComponent,
				createdNodeEntityComponent, expRelationshipType);
		//
		// Delete the Root
		//
		instance.deletePolicy(createdRootEntityComponent);
		//
		// Read back the Node item
		//
		createdNodeEntityComponent = instance.readPolicyComponent(createdNodeEntityComponent);
		//
		// Ensure that the assignment index exists and is empty
		//
		assertTrue(createdNodeEntityComponent.getAssignmentIndex().isEmpty());
	}

	/**
	 * When the 'Node' of a relationship ( Root -> Relationship -> Node) is
	 * deleted then the relationship should be removed. Reading the 'Root'
	 * should result in an empty assignment list
	 */
	@Test
	public void deleting_A_Node_In_A_Dependency_Relationship_Should_Remove_The_Relationship() {

		logger.info("deleting_A_Node_In_A_Dependency_Relationship_Should_Remove_The_Relationship");
		//
		final DataType expRootDataType = IMAGE_COMPONENT;
		final DataType expNodeDataType = INLINE_NOTE;
		final NodeRelationship expRelationshipType = DEPENDENCY_OF;
		//
		// Create the Root
		//
		PolicyDSEntity createdRootEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expRootDataType));
		//
		// Create the Node
		//
		PolicyDSEntity createdNodeEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expNodeDataType));
		//
		// Assign the Node to the Root
		//
		createdRootEntityComponent = instance.createPolicyAssociation(createdRootEntityComponent,
				createdNodeEntityComponent, expRelationshipType);
		//
		// Delete the Node
		//
		instance.deletePolicy(createdNodeEntityComponent);
		//
		// Read back the Root item
		//
		createdRootEntityComponent = instance.readPolicyComponent(createdRootEntityComponent);
		assertTrue(createdRootEntityComponent.getEntityComponents().isEmpty());
	}
}
