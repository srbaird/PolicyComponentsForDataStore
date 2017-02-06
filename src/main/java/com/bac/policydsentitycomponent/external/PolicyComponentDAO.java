
package com.bac.policydsentitycomponent.external;

import java.util.List;
import java.util.Map;

/**
 *
 * @author user0001
 */
public interface PolicyComponentDAO<T> {

	T createPolicyComponent(ClassEntityComponent entityComponent);

	T createPolicyAssociation(ClassEntityComponent parentComponent, ClassEntityComponent childComponent,
			NodeRelationship relationship);

	void deletePolicy(ClassEntityComponent entityComponent);

	T deletePolicyAssociation(ClassEntityComponent entityComponent);

	List<? extends T> listPolicies();

	Map<DataType, Map<DataType, NodeRelationship>> mapPolicies();

	T newPolicyComponent();

	T readPolicyComponent(ClassEntityComponent entityComponent);

	T updatePolicyComponent(ClassEntityComponent entityComponent);
}
