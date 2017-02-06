package com.bac.policydsentitycomponent.external;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Simon Baird
 */
@XmlRootElement
public enum EntityComponentNodeProperty implements DSNodeProperty {

    POLICYDATATYPE("Policy node data type", "policyDataType"),
    RELATIONSHIPTYPE("Relationship", "relationshipType"),
    ENTITYCOMPONENT("Entity component class", "entityComponent"),
    ICON("Icon", "icon"),
    NAME("Name", "name"),
    CONTENT("Content", "content"),
    VALUEDATE("Date", "date"),
    BINARYDATA("Shouldn't be seeing this anywhere", "binaryData"),
    // Following is for backwards compatibility 
    TEXT("Note", "note");

	   private final String displayName;
	   private final String fieldName;

    
     EntityComponentNodeProperty(String displayName, String fieldName) {

        this.displayName = displayName;
        this.fieldName = fieldName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

	@Override
	public String fieldName() {

		return fieldName;
	}
    
}
