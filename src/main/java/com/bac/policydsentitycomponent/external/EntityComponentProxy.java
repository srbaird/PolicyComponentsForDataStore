/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bac.policydsentitycomponent.external;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAnyElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user0001
 */
public abstract class EntityComponentProxy implements EntityComponent, Serializable {

    private static final long serialVersionUID = 89942482937099L;

    protected EntityComponent delegate;
    // logger
    private static Logger logger = LoggerFactory.getLogger(EntityComponentProxy.class);

    public EntityComponentProxy() {

    }

    public EntityComponentProxy(EntityComponent entityComponent) {
        delegate = entityComponent;
    }

    public EntityComponent getDelegate() {
        return delegate;
    }

    public void setDelegate(EntityComponent delegate) {
        this.delegate = delegate;

    }
    
    @Override
    public Long getTimestamp() {
        
        return delegate == null ? 0L : delegate.getTimestamp();
    }

    //  ==============================
    //  Entity Component proxy methods
    //  ==============================
    @Override
    public Long getId() {

        if (delegate == null) {
            return null;
        }
        return delegate.getId();
    }

    @XmlAnyElement
    @Override
    public DataType getDataType() {
        if (delegate == null) {
            return null;
        }
        return delegate.getDataType();
    }

    @Override
    public void setDataType(DataType type) {
        if (delegate == null) {
            return;
        }
        delegate.setDataType(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<EntityComponent> getEntityComponents() {
        if (delegate == null) {
            return Collections.EMPTY_LIST;
        }
        return delegate.getEntityComponents();
    }

    @XmlAnyElement
    @Override
    public void setEntityComponents(List<EntityComponent> entityComponents) {
        if (delegate == null) {
            return;
        }
        delegate.setEntityComponents(entityComponents);
    }

    @Override
    public String getIcon() {

        return delegate == null ? null : delegate.getIcon();
    }

    @Override
    public void setIcon(String icon) {
        if (delegate != null) {
            delegate.setIcon(icon);
        }
    }

    @Override
    public String getName() {
        if (delegate == null) {
            return null;
        }
        return delegate.getName();
    }

    @Override
    public void setName(String name) {
        if (delegate == null) {
            return;
        }
        delegate.setName(name);
    }

    @Override
    public String getContent() {

        return delegate == null ? null : delegate.getContent();
    }

    @Override
    public void setContent(String content) {

        if (delegate != null) {
            delegate.setContent(content);
        }
    }

    @Override
    public Long getValueDate() {
        if (delegate == null) {
            return null;
        }
        return delegate.getValueDate();
    }

    @Override
    public void setValueDate(Long valueDate) {
        if (delegate == null) {
            return;
        }
        delegate.setValueDate(valueDate);
    }

    @Override
    public void addEntityComponent(EntityComponent entityComponent) {

        if (delegate == null) {
            return;
        }
        delegate.addEntityComponent(entityComponent);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.delegate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        return EntityComponents.equals(this, obj);
    }

    public boolean componentEquals(EntityComponent other) {

        if (other == null) {
            return false;
        }
        // Tests ids
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        // Test value dates
        if (!Objects.equals(this.getValueDate(), other.getValueDate())) {
            return false;
        }

        // Test data types
        if (!Objects.equals(this.getDataType(), other.getDataType())) {
            return false;
        }
        // Test names
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        // Test icons
        if (!Objects.equals(this.getIcon(), other.getIcon())) {
            return false;
        }
        // Test content
        if (!Objects.equals(this.getContent(), other.getContent())) {
            return false;
        }
        //
        //  Test the children count
        //
        int thisChildSize = getEntityComponents() == null ? 0 : getEntityComponents().size();
        int otherChildSize = other.getEntityComponents() == null ? 0 : other.getEntityComponents().size();
        if (thisChildSize != otherChildSize) {
            return false;
        }
        //
        //
        //
        @SuppressWarnings("unchecked") // Doesn't matter
        List<EntityComponent> otherComponents = other.getEntityComponents() == null ? Collections.EMPTY_LIST : other.getEntityComponents();
        for (EntityComponent child : getEntityComponents()) {
            if (!otherComponents.contains(child)) {
                return false;
            }
        }
        return true;
    }
}
