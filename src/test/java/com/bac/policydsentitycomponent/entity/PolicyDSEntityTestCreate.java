
package com.bac.policydsentitycomponent.entity;

import static com.bac.policydsentitycomponent.external.EntityComponentDataType.IMAGE_COMPONENT;
import static com.bac.policydsentitycomponent.external.EntityComponentDataType.TAG;
import static com.bac.policydsentitycomponent.external.EntityComponentNodeRelationship.ASSIGNEE_OF;
import static com.bac.policydsentitycomponent.external.EntityComponentNodeRelationship.HIERARCHY_OF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.external.ClassEntityComponent;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.EntityComponent;
import com.bac.policydsentitycomponent.external.EntityComponents;
import com.bac.policydsentitycomponent.external.NodeRelationship;

/**
 *
 * @author Simon Baird
 */
public class PolicyDSEntityTestCreate extends AbstractEntityTest {

	static {
		logger = LoggerFactory.getLogger(PolicyDSEntityTestCreate.class);
	}
	//

	/**
	 * Create a Policy. It should reflect the DataType used to create it and
	 * have no assignments.
	 */
	@Test
	public void a_Newly_Created_Component_Should_Have_no_Assignments() {

		logger.info("a_Newly_Created_Component_Should_Have_no_Assignments");
		//
		DataType expPolicyDataType = TAG;
		ClassEntityComponent mockPolicyComponent = getMockClassEntityComponent(expPolicyDataType);
		//
		PolicyDSEntity createdEntityComponent = instance.createPolicyComponent(mockPolicyComponent);
		PolicyDSEntity readPolicyComponent = instance.readPolicyComponent(createdEntityComponent);
		
		
		assertNotNull(readPolicyComponent);
		//
		assertEquals(classDataType, readPolicyComponent.getDataType());
		//
		assertEquals(expPolicyDataType, readPolicyComponent.getPolicyDataType());
		//
		List<EntityComponent> assignments = readPolicyComponent.getEntityComponents();
		assertNotNull(assignments);
		assertTrue(assignments.isEmpty());
	}

	/**
	 * Create two policies and assign one to the other. One policy should show
	 * the other as an assignment.
	 */
	@Test
	public void a_Node_Should_Appear_In_Assignments_Once_A_Relationship_Is_Established() {

		logger.info("a_Node_Should_Appear_In_Assignments_Once_A_Relationship_Is_Established");
		//
		DataType expRootDataType = IMAGE_COMPONENT;
		DataType expNodeDataType = TAG;
		//
		// Create the root
		//
		ClassEntityComponent mockRootPolicyComponent = getMockClassEntityComponent(expRootDataType);
		//
		PolicyDSEntity createdRootEntityComponent = instance.createPolicyComponent(mockRootPolicyComponent);
		assertNotNull(createdRootEntityComponent);
		//
		// Create the node
		//
		ClassEntityComponent mockNodePolicyComponent = getMockClassEntityComponent(expNodeDataType);
		//
		PolicyDSEntity createdNodeEntityComponent = instance.createPolicyComponent(mockNodePolicyComponent);
		assertNotNull(createdNodeEntityComponent);
		//
		// Root should have no assignments
		//
		List<EntityComponent> assignments = createdRootEntityComponent.getEntityComponents();
		assertNotNull(assignments);
		assertTrue(assignments.isEmpty());
		//
		// Assign the Node to the Root
		//
		NodeRelationship expRelationshipType = ASSIGNEE_OF;
		createdRootEntityComponent = instance.createPolicyAssociation(createdRootEntityComponent,
				createdNodeEntityComponent, expRelationshipType);
		//
		assignments = createdRootEntityComponent.getEntityComponents();
		int single = 1;
		assertEquals(single, assignments.size());
		//
		final int INDEX_OF_FIRST_COMPONENT = 0;
		EntityComponent resultChildComponent = assignments.get(INDEX_OF_FIRST_COMPONENT);
		assertTrue(EntityComponents.equals(createdNodeEntityComponent, resultChildComponent));
		assertEquals(createdNodeEntityComponent.getId(), resultChildComponent.getId());
		//
		final PolicyDSEntityRelationshipProxy resultRelationship = (PolicyDSEntityRelationshipProxy) resultChildComponent;
		assertFalse(resultRelationship.isReverseRelationship());
		NodeRelationship resultRelationshipType = resultRelationship.getRelationshipType();
		assertEquals(expRelationshipType, resultRelationshipType);
	}

	/**
	 * It should be possible to assign component to itself and therefore a
	 * component should be in its own assignments. This allows for hierarchies
	 * to be created e.g. Node -> Relationship -> Node.
	 *
	 */
	@Test
	public void a_Node_Can_Appear_In_its_Own_Assignments_Once_A_Relationship_Is_Established() {

		logger.info("a_Node_Can_Appear_In_its_Own_Assignments_Once_A_Relationship_Is_Established");
		//
		DataType expPolicyDataType = TAG;
		//
		// Create the node
		//
		ClassEntityComponent mockNodePolicyComponent = getMockClassEntityComponent(expPolicyDataType);
		//
		PolicyDSEntity createdNodeEntityComponent = instance.createPolicyComponent(mockNodePolicyComponent);
		assertNotNull(createdNodeEntityComponent);
		//
		// Assign the Node to itself
		//
		NodeRelationship expRelationshipType = HIERARCHY_OF;
		createdNodeEntityComponent = instance.createPolicyAssociation(createdNodeEntityComponent,
				createdNodeEntityComponent, expRelationshipType);
		assertNotNull(createdNodeEntityComponent);
		//
		List<EntityComponent> assignments = createdNodeEntityComponent.getEntityComponents();
		int single = 1;
		assertEquals(single, assignments.size());
		//
		final int INDEX_OF_FIRST_COMPONENT = 0;
		EntityComponent resultChildComponent = assignments.get(INDEX_OF_FIRST_COMPONENT);
		assertEquals(createdNodeEntityComponent.getId(), resultChildComponent.getId());
		//
		final PolicyDSEntityRelationshipProxy resultRelationship = (PolicyDSEntityRelationshipProxy) resultChildComponent;
		assertFalse(resultRelationship.isReverseRelationship());
		NodeRelationship resultRelationshipType = resultRelationship.getRelationshipType();
		assertEquals(expRelationshipType, resultRelationshipType);
	}

	/**
	 * When a component is assigned only the 'other' component should have
	 * assignments. The original component should have no assignments. e.g. for
	 * Root -> Relationship -> Node then 'Node' should have empty assignments
	 */
	@Test
	public void an_Assigned_Component_Should_Have_No_Assignments_Itself() {

		logger.info("an_Assigned_Component_Should_Have_No_Assignments_Itself");
		//
		DataType expRootDataType = IMAGE_COMPONENT;
		DataType expNodeDataType = TAG;
		//
		// Create the root
		//
		ClassEntityComponent mockRootPolicyComponent = getMockClassEntityComponent(expRootDataType);
		PolicyDSEntity createdRootEntityComponent = instance.createPolicyComponent(mockRootPolicyComponent);
		//
		ClassEntityComponent mockNodePolicyComponent = getMockClassEntityComponent(expNodeDataType);
		PolicyDSEntity createdNodeEntityComponent = instance.createPolicyComponent(mockNodePolicyComponent);
		//
		//
		// Assign the Relationship From Root to Node
		//
		NodeRelationship expRelationshipType = ASSIGNEE_OF;
		createdRootEntityComponent = instance.createPolicyAssociation(createdRootEntityComponent,
				createdNodeEntityComponent, expRelationshipType);
		//
		createdNodeEntityComponent = instance.readPolicyComponent(createdNodeEntityComponent);

		int empty = 0;
		assertEquals(empty, createdNodeEntityComponent.getEntityComponents().size());
	}

	/**
	 * When an existing relationship between two nodes is removed then the
	 * owning node should have no assignments when it is read
	 */
	@Test
	public void a_Removed_Relationship_Should_Result_In_Empty_Assignment_List() {

		logger.info("a_Removed_Relationship_Should_Result_In_Empty_Assignment_List");
		//
		DataType expRootDataType = IMAGE_COMPONENT;
		DataType expNodeDataType = TAG;
		NodeRelationship expRelationshipType = ASSIGNEE_OF;
		//
		// Create the root
		//
		PolicyDSEntity createdRootEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expRootDataType));
		//
		// Create the node
		//
		PolicyDSEntity createdNodeEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expNodeDataType));
		//
		// Assign the Node to the Root
		//
		createdRootEntityComponent = instance.createPolicyAssociation(createdRootEntityComponent,
				createdNodeEntityComponent, expRelationshipType);
		//
		int single = 1;
		assertEquals(single, createdRootEntityComponent.getEntityComponents().size());
		//
		// Get the Node. This represents an relationship
		//
		final int INDEX_OF_FIRST_COMPONENT = 0;
		EntityComponent resultChildComponent = createdRootEntityComponent.getEntityComponents()
				.get(INDEX_OF_FIRST_COMPONENT);
		//
		// Remove the association. The root which now should have no assignments
		//
		assertTrue(resultChildComponent instanceof ClassEntityComponent);
		instance.deletePolicy((ClassEntityComponent) resultChildComponent);
		//
		createdRootEntityComponent = instance.readPolicyComponent(createdRootEntityComponent);
		assertTrue(createdRootEntityComponent.getEntityComponents().isEmpty());
	}

	/**
	 * Creating an relationship between two nodes should not prevent the
	 * assigned node from appearing in a list of all root nodes. That is, given
	 * Root -> Relationship -> Node then 'Node' should also be regarded as the
	 * starting point of any query.
	 */
	@Test
	public void an_Assigned_Node_Should_Still_Appear_In_A_List_Of_Root_Nodes() {

		logger.info("an_Assigned_Node_Should_Still_Appear_In_A_List_Of_Root_Nodes");

		logger.info("a_Removed_Relationship_Should_Result_In_Empty_Assignment_List");
		//
		DataType expRootDataType = IMAGE_COMPONENT;
		DataType expNodeDataType = TAG;
		NodeRelationship expRelationshipType = ASSIGNEE_OF;
		//
		// Create the root, node and assignment
		//
		PolicyDSEntity createdRootEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expRootDataType));
		PolicyDSEntity createdNodeEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expNodeDataType));
		createdRootEntityComponent = instance.createPolicyAssociation(createdRootEntityComponent,
				createdNodeEntityComponent, expRelationshipType);
		//
		// Get a Policy list. Both Policy entities should be returned
		//
		int bothNodesPresent = 2;
		List<? extends PolicyDSEntity> listRootNodes = instance.listPolicies();
		assertNotNull(listRootNodes);
		int resultNodesSize = listRootNodes.size();
		assertEquals(bothNodesPresent, resultNodesSize);

		assertTrue(listRootNodes.contains(createdRootEntityComponent));
		assertTrue(listRootNodes.contains(createdNodeEntityComponent));
	}

	/**
	 * Creating an relationship from a node to itself should not result in more
	 * than one node being represented in a list of root nodes. That is, given
	 * Node -> Relationship -> Node then only 'Node' should actually exist
	 */
	@Test
	public void an_Self_Assigned_Node_Should_Not_Result_In_Two_Nodes_Appearing_In_List_Of_Root_Nodes() {

		logger.info("an_Self_Assigned_Node_Should_Not_Result_In_Two_Nodes_Appearing_In_List_Of_Root_Nodes");

		DataType expNodeDataType = TAG;
		NodeRelationship expRelationshipType = HIERARCHY_OF;
		//
		// Create the node and assign it to itself
		//
		PolicyDSEntity createdRootEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(expNodeDataType));

		instance.createPolicyAssociation(createdRootEntityComponent, createdRootEntityComponent, expRelationshipType);
		//
		// Get a Policy list and ensure only one node appears
		//
		int singleNodeOnly = 1;
		List<? extends PolicyDSEntity> listRootNodes = instance.listPolicies();
		assertNotNull(listRootNodes);
		int resultNodesSize = listRootNodes.size();
		assertEquals(singleNodeOnly, resultNodesSize);

		assertTrue(listRootNodes.contains(createdRootEntityComponent));
	}

}
