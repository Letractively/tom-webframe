package webFrame.cache;

//$Id: EhCacheProvider.java 9964 2006-05-30 15:40:54Z epbernard $
/**
 *  Copyright 2003-2006 Greg Luck, Jboss Inc
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.util.Hashtable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import webFrame.app.listener.Variable;

/**
 * Cache Provider plugin
 * 
 * Taken from EhCache 1.3 distribution
 * @author Winter Lau
 */
public class EHCacheProvider implements CacheProvider{

    private static final Log log = LogFactory.getLog(EHCacheProvider.class);

	private CacheManager manager;
	private Hashtable<String, EHCache> _CacheManager ;

    /**
     * Builds a Cache.
     * <p>
     * Even though this method provides properties, they are not used.
     * Properties for EHCache are specified in the ehcache.xml file.
     * Configuration will be read from ehcache.xml for a cache declaration
     * where the name attribute matches the name parameter in this builder.
     *
     * @param name the name of the cache. Must match a cache configured in ehcache.xml
     * @param properties not used
     * @return a newly built cache will be built and initialised
     * @throws CacheException inter alia, if a cache of the same name already exists
     */
    public EHCache buildCache(String name, boolean autoCreate) throws CacheException {
    	EHCache ehcache = _CacheManager.get(name);
    	if(ehcache == null && autoCreate){
		    try {
	            net.sf.ehcache.Cache cache = manager.getCache(name); //通过配置文件
	            //直接new的方式,设置属性
	         // Ehcache a = new Cache(name, maxElementsInMemory, memoryStoreEvictionPolicy, overflowToDisk, diskStorePath, eternal, timeToLiveSeconds, timeToIdleSeconds, diskPersistent, diskExpiryThreadIntervalSeconds, registeredEventListeners, bootstrapCacheLoader, maxElementsOnDisk)
	            if (cache == null) {
	                log.warn("Could not find configuration [" + name + "]; using defaults.");
	                manager.addCache(name);
	                cache = manager.getCache(name);
	                log.debug("started EHCache region: " + name);                
	            }
	            synchronized(_CacheManager){
		            ehcache = new EHCache(cache);
		            _CacheManager.put(name, ehcache);
		            return ehcache ;
	            }
		    } catch (net.sf.ehcache.CacheException e) {
	            throw new CacheException(e);
	        }
    	}
        return ehcache;
    }

	/**
	 * Callback to perform any necessary initialization of the underlying cache implementation
	 * during SessionFactory construction.
	 *
	 * @param properties current configuration settings.
	 */
	public void start() throws CacheException {
		if (manager != null) {
            log.warn("Attempt to restart an already started EhCacheProvider. Use sessionFactory.close() " +
                    " between repeated calls to buildSessionFactory. Using previously created EhCacheProvider." +
                    " If this behaviour is required, consider using net.sf.ehcache.hibernate.SingletonEhCacheProvider.");
            return;
        }
		try{
			manager = new CacheManager(Variable.path + "\\WEB-INF\\config\\ehcache.xml");
		}catch(Exception e){
			manager = new CacheManager();
		}
        _CacheManager = new Hashtable<String, EHCache>();
       
	}

	/**
	 * Callback to perform any necessary cleanup of the underlying cache implementation
	 * during SessionFactory.close().
	 */
	public void stop() {
		if (manager != null) {
            manager.shutdown();
            manager = null;
        }
	}

}
