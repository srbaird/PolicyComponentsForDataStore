package com.bac.policydsentitycomponent.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.bac.policydsentitycomponent.external.ApplicationContextProvider;

/**
 * Simple provider to inject for testing purposes
 * 
 * @author simon
 *
 */
public class ApplicationContextProviderImpl implements ApplicationContextProvider {

	@Autowired
	private ApplicationContext applicationContest;

	@Override
	public ApplicationContext getApplicationContext() {

		return applicationContest;
	}

}
