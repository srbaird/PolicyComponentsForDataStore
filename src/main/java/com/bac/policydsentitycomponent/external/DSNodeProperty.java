package com.bac.policydsentitycomponent.external;

/**
 * 
 * Represents the mapping between any class and a DataStore fieldName. Typically
 * allows an enum to be mapped to a field name. e.g. LONG_ENUM_NAME ->
 * longEnumName
 * 
 * @author Simon Baird
 *
 */
public interface DSNodeProperty extends NodeProperty{

	/**
	 * Get the field name responsible for storing the class value
	 * 
	 * @return a String representing the DataStore field name for this instance
	 */
	String fieldName();
}
