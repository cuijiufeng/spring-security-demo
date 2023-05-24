package io.inferiority.demo.springsecurity.config.mybatis;

import io.inferiority.demo.springsecurity.config.ApplicationContextHolder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.springframework.cache.CacheManager;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Class: RedisMybatis2LevelCache
 * @Date: 2021/8/18 13:44
 * @auth: cuijiufeng
 */
public class CacheManagerCache implements Cache {
    private final String id;
    private final CacheManager cacheManager;

    public CacheManagerCache(String id) {
        this.id = id;
        this.cacheManager = ApplicationContextHolder.getApplicationContext()
                .getBean(CacheManager.class);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        cacheManager.getCache(id).put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        try {
            return cacheManager.getCache(id).get(key).get();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public Object removeObject(Object key) {
        Object object = getObject(key);
        cacheManager.getCache(id).evict(key);
        return object;
    }

    @Override
    public void clear() {
        cacheManager.getCache(id).clear();
    }

    @Override
    public int getSize() {
        return -1;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        // 缓存击穿是指缓存中没有但数据库中有的数据（一般是缓存时间到期），这时由于并发用户特别多，同时读缓存没读到数据，又同时去数据库去取数据，引起数据库压力瞬间增大，造成过大压力
        // 解决方案：加互斥读锁 ReentrantReadWriteLock
        return new ReentrantReadWriteLock();
    }

    @Override
    public boolean equals(Object o) {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cache)) {
            return false;
        }

        Cache otherCache = (Cache) o;
        return getId().equals(otherCache.getId());
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        return getId().hashCode();
    }
}
