
package com.bac.policydsentitycomponent.entity;

import static com.bac.policydsentitycomponent.external.EntityComponentDataType.*;
import static com.bac.policydsentitycomponent.external.EntityComponentDataType.TAG;
import static com.bac.policydsentitycomponent.external.EntityComponentNodeRelationship.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.NodeRelationship;

/**
 * The data accessor for policy components contains a convenience method to
 * transform a graph into a set of mappings. This is primarily intended to form
 * the 'Policy' schema for an application i.e which relationships exist between
 * different data types. Ultimately a PolicyEntity is a simple wrapper to hold a
 * data type and the purpose of this part of an application is simply to define
 * the type of mapping described by the mapPolicies() method. The resulting map
 * is intended to be injected into an implementation of the
 * {@code RelationshipPolicy} interface which then controls assignments for the
 * actual data nodes in an application.
 * 
 * @author Simon Baird
 */
public class PolicyDSEntityTestMapPolicies extends AbstractEntityTest {

	static {
		logger = LoggerFactory.getLogger(PolicyDSEntityTestMapPolicies.class);
	}

	/**
	 * An empty data store should result in an empty policy map
	 */
	@Test
	public void listing_Policies_From_An_Empty_Datastore_Should_Return_An_Empty_Map() {

		logger.info("listing_Policies_From_An_Empty_Datastore_Should_Return_An_Empty_Map");

		Map<DataType, Map<DataType, NodeRelationship>> policies = instance.mapPolicies();
		assertTrue(policies.isEmpty());
	}

	/**
	 * Created Policy components without associations should be represented in
	 * the policy map with empty relationship maps
	 */
	@Test
	public void all_Components_Should_Be_Represented_In_Return_Map_With_Empty_Relationship_Maps() {

		logger.info("all_Components_Should_Be_Represented_In_Return_Map_With_Empty_Relationship_Maps");

		DataType tagPolicyDataType = TAG;
		DataType imagePolicyDataType = IMAGE_COMPONENT;
		instance.createPolicyComponent(getMockClassEntityComponent(tagPolicyDataType));
		instance.createPolicyComponent(getMockClassEntityComponent(imagePolicyDataType));
		//
		Map<DataType, Map<DataType, NodeRelationship>> policies = instance.mapPolicies();
		final int EXPECTED_MAP_SIZE = 2;
		assertEquals(EXPECTED_MAP_SIZE, policies.size());
		//
		List<DataType> expectedDataTypes = Arrays.asList(new DataType[] { tagPolicyDataType, imagePolicyDataType });
		policies.keySet().containsAll(expectedDataTypes);
		//
		assertEquals(Collections.emptyMap(), policies.get(tagPolicyDataType));
		assertEquals(Collections.emptyMap(), policies.get(imagePolicyDataType));
	}

	/**
	 * A component with a relationship should be represented in the map as a
	 * 'Root' -> ('Node', 'Relationship') mapping where 'Root' and 'Node' are
	 * data types. Both data types should appears in the key set and the 'Node'
	 * data type should have an empty set. The 'Root' data type should have a
	 * 'Node' -> 'Relationship' map.
	 */
	@Test
	public void a_Component_With_Relationship_Should_Appear_In_The_Policy_Mapping() {

		logger.info("a_Component_With_Relationship_Should_Appear_In_The_Policy_Mapping");

		DataType rootPolicyDataType = TAG;
		DataType nodePolicyDataType = IMAGE_COMPONENT;
		NodeRelationship expRelationshipType = DEPENDENCY_OF;
		//
		// Create the Root, Node and add a Relationship
		//
		PolicyDSEntity createdRootEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(rootPolicyDataType));
		PolicyDSEntity createdNodeEntityComponent = instance
				.createPolicyComponent(getMockClassEntityComponent(nodePolicyDataType));
		createdRootEntityComponent = instance.createPolicyAssociation(createdRootEntityComponent,
				createdNodeEntityComponent, expRelationshipType);
		//
		Map<DataType, Map<DataType, NodeRelationship>> policies = instance.mapPolicies();
		int expectedMapSize = 2;
		assertEquals(expectedMapSize, policies.size());
		//
		List<DataType> expectedDataTypes = Arrays.asList(new DataType[] { rootPolicyDataType, nodePolicyDataType });
		policies.keySet().containsAll(expectedDataTypes);
		//
		assertEquals(Collections.emptyMap(), policies.get(nodePolicyDataType));
		//
		Map<DataType, NodeRelationship> relationshipMap = policies.get(rootPolicyDataType);
		expectedMapSize = 1;
		assertEquals(expectedMapSize, relationshipMap.size());
		assertTrue(relationshipMap.containsKey(nodePolicyDataType));
	}

	/**
	 * A complex scenario. 'A' has relationships to 'B' and 'C', and 'B' has a
	 * relationship to 'D'. All 4 components should appear in the mapping. 'C'
	 * and 'D' should show no relationships of their own (empty map) and 'B'
	 * should show 'D' in its map. 'A' should show 'B' and 'C' in its map. To
	 * aid comprehension the names will closely follow the description above.
	 * 
	 * 'A' -> { 'B' -> 'AtoB', 'C' -> 'AtoC'} 
	 * 'B' -> { 'D; -> 'BtoD' } 
	 * 'C' -> {}
	 * 'D' -> {}
	 */
	@Test
	public void a_Complex_Set_Of_Relationship_Mappings() {

		logger.info("a_Complex_Set_Of_Relationship_Mappings");

		DataType A = TAG;
		DataType B = IMAGE_COMPONENT;
		DataType C = INLINE_NOTE;
		DataType D = PLANNER_EVENT;
		//
		NodeRelationship AtoB = DEPENDENCY_OF;
		NodeRelationship AtoC = ASSIGNEE_OF;
		NodeRelationship BtoD = ASSIGNEE_OF;
		//
		// Set up the scenario
		//
		PolicyDSEntity componentForA = instance.createPolicyComponent(getMockClassEntityComponent(A));
		PolicyDSEntity componentForB = instance.createPolicyComponent(getMockClassEntityComponent(B));
		PolicyDSEntity componentForC = instance.createPolicyComponent(getMockClassEntityComponent(C));
		PolicyDSEntity componentForD = instance.createPolicyComponent(getMockClassEntityComponent(D));
		//
		instance.createPolicyAssociation(componentForA, componentForB, AtoB);
		instance.createPolicyAssociation(componentForA, componentForC, AtoC);
		instance.createPolicyAssociation(componentForB, componentForD, BtoD);
		//
		Map<DataType, Map<DataType, NodeRelationship>> policies = instance.mapPolicies();
		int expectedMapSize = 4;
		assertEquals(expectedMapSize, policies.size());
		//
		final List<DataType> expectedDataTypes = Arrays.asList(new DataType[] { A, B, C, D });
		policies.keySet().containsAll(expectedDataTypes);
		//
		// Easy ones first
		//
		assertEquals(Collections.emptyMap(), policies.get(C));
		assertEquals(Collections.emptyMap(), policies.get(D));
		//
		// Now check 'A'
		//
		final Map<DataType, NodeRelationship> mapForA = policies.get(A);
		expectedMapSize = 2;
		assertEquals(expectedMapSize, mapForA.size());
		//
		assertTrue(mapForA.containsKey(B));
		assertEquals(AtoB, mapForA.get(B));
		//
		assertTrue(mapForA.containsKey(C));
		assertEquals(AtoC, mapForA.get(C));
		//
		// Finally check 'B'
		//
		final Map<DataType, NodeRelationship> mapForB = policies.get(B);
		expectedMapSize = 1;
		assertEquals(expectedMapSize, mapForB.size());
		//
		assertTrue(mapForB.containsKey(D));
		assertEquals(BtoD, mapForB.get(D));
	}

}
