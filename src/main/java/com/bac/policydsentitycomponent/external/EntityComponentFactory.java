package com.bac.policydsentitycomponent.external;

/**
 *
 * @author user0001
 * @param <K>
 * @param <T>
 */
public interface EntityComponentFactory<K,T> extends ApplicationBeanFactory<K,T> {

    Class<? extends T> getEntityComponentClass(K type);
}
