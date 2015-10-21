package org.vasttrafik.wso2.carbon.apimgt.portal.api.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Daniel Oskarsson <daniel.oskarsson@gmail.com>
 */
public class InMemory<K, T> implements Persistence<K, T> {

	private Map<K, T> map = new HashMap<K, T>();

	public boolean create(K key, T type) {
		map.put(key, type);
		return true;
	}

	public T read(K key) {
		return map.get(key);
	}

	public boolean update(K key, T type) {
		map.put(key, type);
		return true;
	}

	public boolean delete(K key) {
		map.remove(key);
		return true;
	}

	public List<T> list() {
		return new ArrayList(map.values());
	}

}
