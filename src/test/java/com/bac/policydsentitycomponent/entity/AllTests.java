package com.bac.policydsentitycomponent.entity;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
		PolicyDSEntityComponentFactoryTestAll.class, 
		PolicyDSEntityTestAsFactory.class,
		PolicyDSEntityTestCreate.class, 
		PolicyDSEntityTestDelete.class, 
		PolicyDSEntityTestDeleteAssociations.class,
		PolicyDSEntityTestRead.class, 
		PolicyDSEntityTestUpdate.class ,
		PolicyDSEntityTestMapPolicies.class
})
public class AllTests {

}
