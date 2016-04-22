package com.base.engine.rendering;

import java.util.HashMap;

public class ValuesOfType<K> {
	private HashMap<String, K> map;
	
	public ValuesOfType()
	{
		map = new HashMap<String, K>();
	}

	public void addValue(String name, K value) {
		map.put(name, value);
	}

	public boolean contains(String name){
		return map.containsKey(name);
	}
	
	public K getValue(String name)
	{
		K result = map.get(name);
		if(result != null)
			return result;

		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValuesOfType<?> other = (ValuesOfType<?>) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}

}
