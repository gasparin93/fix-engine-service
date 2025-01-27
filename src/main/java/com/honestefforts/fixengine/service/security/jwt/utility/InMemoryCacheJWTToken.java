package com.honestefforts.fixengine.service.security.jwt.utility;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.map.LRUMap;

/**
 * Reference: Crunchify.com and minor modifications made by Jason Wu
 */
public class InMemoryCacheJWTToken<K, T> {

	private long timeToLive;
	private LRUMap<K, CacheObject> cacheMap;
	
	protected class CacheObject {
		public long firstCreated = System.currentTimeMillis();
		public T value;

		protected CacheObject(T value) {
			this.value = value;
		}
	}
	
	public InMemoryCacheJWTToken(long timeToLive, final long timerInterval, int maxItems) {
		this.timeToLive = timeToLive * 1000;

		cacheMap = new LRUMap<K, CacheObject>(maxItems);

		if (this.timeToLive > 0 && timerInterval > 0) {
			Thread t = new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(timerInterval * 1000);
					} catch (InterruptedException ex) {
					}
					cleanup();
				}
			});

			t.setDaemon(true);
			t.start();
		}
	}
	
	public void put(K key, T value) {
		synchronized (cacheMap) {
			cacheMap.put(key, new CacheObject(value));
		}
	}
	
	public T get(K key) {
		synchronized (cacheMap) {
			CacheObject c = cacheMap.get(key);

			if (c == null)
				return null;
			else {
				return c.value;
			}
		}
	}
	
	public void remove(K key) {
		synchronized (cacheMap) {
			cacheMap.remove(key);
		}
	}
	
	public int size() {
		synchronized (cacheMap) {
			return cacheMap.size();
		}
	}
	
	public void cleanup() {
		long now = System.currentTimeMillis();
        List<K> deleteKeys = null;
        
        synchronized (cacheMap) {
        	deleteKeys = new ArrayList<K>((cacheMap.size() / 2) + 1);
        	deleteKeys = cacheMap.keySet().stream()
	            .filter(key -> cacheMap.get(key) != null && 
	            	now > (cacheMap.get(key).firstCreated + timeToLive))
	            .toList();
        
        }
        
        for (K key : deleteKeys) {
        	synchronized (cacheMap) {
        		cacheMap.remove(key);
        	}
        	
        	Thread.yield();
        }
	}
}
