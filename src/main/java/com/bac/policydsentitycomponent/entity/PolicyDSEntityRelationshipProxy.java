/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.bac.policydsentitycomponent.external.ClassEntityComponent;
import com.bac.policydsentitycomponent.external.DataType;
import com.bac.policydsentitycomponent.external.EntityComponent;
import com.bac.policydsentitycomponent.external.NodeRelationship;

/**
 *
 * @author user0001
 */
public class PolicyDSEntityRelationshipProxy implements ClassEntityComponent, Serializable {

    private static final long serialVersionUID = 438680123286556L;

    private Object relationshipId;
    //
    private NodeRelationship relationshipType;
    //
    private boolean reverseRelationship;
    //
    private final ClassEntityComponent delegate;
    //
    private final String NULL_DELEGATE_MSG = "Instantiation with null delegate is not permitted";

    public PolicyDSEntityRelationshipProxy(ClassEntityComponent delegate) {

        Objects.requireNonNull(delegate, NULL_DELEGATE_MSG);
        this.delegate = delegate;
    }

    ClassEntityComponent getDelegate() {
        return delegate;
    }

    @Override
    public boolean isReverseRelationship() {
        return reverseRelationship;
    }

    void setReverseRelationship(boolean isReverseRelationship) {
        this.reverseRelationship = isReverseRelationship;
    }

    @Override
    public Object getRelationshipId() {
        return relationshipId;
    }

    void setRelationshipId(Object relationshipId) {
        this.relationshipId = relationshipId;
    }

    @Override
    public NodeRelationship getRelationshipType() {
        return relationshipType;
    }

    void setRelationshipType(NodeRelationship relationshipType) {
        this.relationshipType = relationshipType;
    }

    @Override
    public DataType getPolicyDataType() {
        return delegate.getPolicyDataType();
    }

    @Override
    public void setPolicyDataType(DataType type) {
        delegate.setPolicyDataType(type);
    }

    @Override
    public Long getId() {
        return delegate.getId();
    }

    @Override
    public Long getTimestamp() {
        return delegate.getTimestamp();
    }

    @Override
    public DataType getDataType() {
        return delegate.getDataType();
    }

    @Override
    public void setDataType(DataType type) {
        delegate.setDataType(type);
    }

    @Override
    public String getIcon() {
        return delegate.getIcon();
    }

    @Override
    public void setIcon(String name) {
        delegate.setIcon(name);
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public void setName(String name) {
        delegate.setName(name);
    }

    @Override
    public Long getValueDate() {
        return delegate.getValueDate();
    }

    @Override
    public void setValueDate(Long valueDate) {
        delegate.setValueDate(valueDate);
    }

    @Override
    public String getContent() {
        return delegate.getContent();
    }

    @Override
    public void setContent(String text) {
        delegate.setContent(text);
    }

    @Override
    public List<EntityComponent> getEntityComponents() {
        return delegate.getEntityComponents();
    }

    @Override
    public void setEntityComponents(List<EntityComponent> entityComponents) {
        delegate.setEntityComponents(entityComponents);
    }

    @Override
    public void addEntityComponent(EntityComponent entityComponent) {
        delegate.addEntityComponent(entityComponent);
    }
}
