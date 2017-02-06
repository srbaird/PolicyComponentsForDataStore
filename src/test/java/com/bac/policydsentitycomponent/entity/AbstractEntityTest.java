/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.entity;

import static com.bac.policydsentitycomponent.external.EntityComponentDataType.ENTITY_COMPONENT_CLASS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Closeable;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bac.policydsentitycomponent.external.ApplicationContextProvider;
import com.bac.policydsentitycomponent.external.ClassEntityComponent;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.PolicyComponentDAO;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;

/**
 *
 * @author Simon Baird
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public abstract class AbstractEntityTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	//
	private Closeable closeable;
	//
	@Resource(name = "policyDSDaoAdapter")
	PolicyComponentDAO<PolicyDSEntity> instance;
	//
	@Autowired
	ApplicationContext appContext;
	
	final DataType classDataType = ENTITY_COMPONENT_CLASS;

	Calendar calendar;
	//
	// logger
	static Logger logger = LoggerFactory.getLogger(AbstractEntityTest.class);

	@Before
	public void setUp() {

		//
		// Initialize the instance
		//
		MockitoAnnotations.initMocks(this);
		//
		helper.setUp();
		//
		// DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		//
		closeable = ObjectifyService.begin();
		//
		calendar = Calendar.getInstance();
	}

	@After
	public void tearDown() throws IOException {

		closeable.close();
		//
		helper.tearDown();
	}

	Date dateOnly(Date date) {
		//
		// Strip the time component from the date
		//
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0); // Remove the time component ...
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Date(calendar.getTimeInMillis());
	}

	String getRandomString() {

		return RandomStringUtils.random(24);
	}

	DataType getMockDataType() {

		DataType returnMock = mock(DataType.class);
		String name = getRandomString();
		when(returnMock.name()).thenReturn(name);
		return returnMock;
	}

	ApplicationContextProvider getMockFacesUtilities() {

		ApplicationContextProvider returnMock = mock(ApplicationContextProvider.class);
		when(returnMock.getApplicationContext()).thenReturn(appContext);
		return returnMock;
	}
	//
	//
	//
	ClassEntityComponent getMockClassEntityComponent(DataType type) {

		ClassEntityComponent returnValue = getMockClassEntityComponent();
		when(returnValue.getPolicyDataType()).thenReturn(type);
		return returnValue;
	}

	ClassEntityComponent getMockClassEntityComponent() {

		ClassEntityComponent returnValue = mock(ClassEntityComponent.class);
		when(returnValue.getDataType()).thenReturn(classDataType);
		return returnValue;
	}
}
