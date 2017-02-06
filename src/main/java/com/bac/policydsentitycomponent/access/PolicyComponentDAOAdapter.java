package com.bac.policydsentitycomponent.access;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bac.policydsentitycomponent.entity.PolicyDSEntity;
import com.bac.policydsentitycomponent.entity.PolicyDSEntityRelationshipProxy;
import com.bac.policydsentitycomponent.external.ClassEntityComponent;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.DataTypeResolver;
import com.bac.policydsentitycomponent.external.NodeRelationship;
import com.bac.policydsentitycomponent.external.PolicyComponentDAO;

/**
 * Acts as an adapter between the reduced set of requirements for the Policy
 * class and the full entity maintenance functionality. It ensures that only
 * instances of Class entities are handled
 * 
 * @author Simon Baird
 *
 */
public class PolicyComponentDAOAdapter implements PolicyComponentDAO<PolicyDSEntity> {

	private final DataAccessor instance;

	private final DataTypeResolver resolver;

	private final String NULL_DELEGATE_MSG = "Attempt to instantiate with a null DataAccessor instance";
	private final String NULL_RESOLVER_MSG = "Attempt to instantiate with a null DataTypeResolver instance";

	public PolicyComponentDAOAdapter(DataAccessor instance, DataTypeResolver resolver) {

		Objects.requireNonNull(instance, NULL_DELEGATE_MSG);
		Objects.requireNonNull(resolver, NULL_RESOLVER_MSG);
		this.instance = instance;
		this.resolver = resolver;
	}

	@Override
	public PolicyDSEntity createPolicyComponent(ClassEntityComponent entityComponent) {

		return instance.createEntityComponent(entityComponent);
	}

	@Override
	public PolicyDSEntity createPolicyAssociation(ClassEntityComponent parentComponent,
			ClassEntityComponent childComponent, NodeRelationship relationship) {

		return instance.createEntityAssociation(parentComponent, childComponent, relationship);
	}

	@Override
	public void deletePolicy(ClassEntityComponent entityComponent) {

		instance.deleteEntityComponent(entityComponent);
	}

	@Override
	public PolicyDSEntity deletePolicyAssociation(ClassEntityComponent entityComponent) {

		return instance.deleteAssociation(entityComponent);
	}

	@Override
	public List<? extends PolicyDSEntity> listPolicies() {

		return instance.listRootNodes(resolver.getEntityComponentClassDataType()).stream().map(e -> (PolicyDSEntity) e)
				.collect(Collectors.toList());
	}
	
	@Override
	public Map<DataType, Map<DataType, NodeRelationship>> mapPolicies() {

		List<? extends PolicyDSEntity> policies = listPolicies();

		if (policies.isEmpty()) {
			return Collections.emptyMap();
		}

		return policies.stream()
				.collect(Collectors.toMap(p -> p.getPolicyDataType(),
						p -> p.getEntityComponents().stream().collect(Collectors.toMap(
								e -> ((PolicyDSEntityRelationshipProxy) e).getPolicyDataType(),
								e -> ((PolicyDSEntityRelationshipProxy) e).getRelationshipType()))));
	}


	@Override
	public PolicyDSEntity newPolicyComponent() {

		PolicyDSEntity returnValue = instance.newEntityComponent(resolver.getEntityComponentClassDataType());
		returnValue.setDataType(resolver.getEntityComponentClassDataType());
		return returnValue;
	}

	@Override
	public PolicyDSEntity readPolicyComponent(ClassEntityComponent entityComponent) {

		return instance.readEntityComponent(entityComponent);
	}

	@Override
	public PolicyDSEntity updatePolicyComponent(ClassEntityComponent entityComponent) {

		return instance.updateEntityComponent(entityComponent);
	}
}
