package org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence;

import java.util.List;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public interface Persistence<K, T> {

	boolean create(K key, T type);

	T read(K key);

	boolean update(K key, T type);

	boolean delete(K key);

	List<T> list();

}
