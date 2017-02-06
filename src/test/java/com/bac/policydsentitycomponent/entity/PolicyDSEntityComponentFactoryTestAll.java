/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.entity;

import static com.bac.policydsentitycomponent.external.EntityComponentDataType.ENTITY_COMPONENT_CLASS;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bac.policydsentitycomponent.external.ClassEntityComponent;
import com.bac.policydsentitycomponent.external.DataType;

/**
 *
 * @author Simon Baird
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class PolicyDSEntityComponentFactoryTestAll {

	@Autowired
	ApplicationContext appContext;

	@Autowired
	PolicyDSEntityComponentFactory instance;

	static Logger logger = LoggerFactory.getLogger(PolicyDSEntityComponentFactoryTestAll.class);

	@Before
	public void setUp() {
		//
		// Initialize the instance
		//
		MockitoAnnotations.initMocks(this);
		//
	}

	/**
	 * Requesting a class component should return one.
	 */
	@Test
	public void generating_A_Component_For_ENTITY_COMPONENT_CLASS_Should_Return_A_ClassEntityComponent() {

		logger.info("generating_A_Component_For_ENTITY_COMPONENT_CLASS_Should_Return_A_ClassEntityComponent");

		DataType policyDataType = ENTITY_COMPONENT_CLASS;

		Object resultBean = instance.createBeanByDataType(policyDataType);
		assertTrue(resultBean instanceof ClassEntityComponent);
	}

	/**
	 * The factory is initialised is only able to create components from a list
	 * of known data types. Attempting to create a component with an unknown
	 * type should should throw an exception
	 */
	@Test(expected = IllegalStateException.class)
	public void generating_A_Component_For_An_Unknown_Datatype_Should_Result_In_Exception() {

		logger.info("generating_A_Component_For_An_Unknown_Datatype_Should_Result_In_Exception");

		instance.createBeanByDataType(mock(DataType.class));
	}

	/**
	 * Requesting a component with a null Datatype should throw an exception
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void generating_A_Component_With_A_Null_Datatype_Should_Throw_An_Exception() {

		logger.info("generating_A_Component_With_A_Null_Datatype_Should_Throw_An_Exception");

		DataType type = null;

		instance.getEntityComponentClass(type);
	}
}
