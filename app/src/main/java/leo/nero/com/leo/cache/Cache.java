package leo.nero.com.leo.cache;

import android.util.LruCache;

import java.util.Map;

/**
 * Created by zhouyou on 2015/7/29.
 */
public class Cache extends LruCache{
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public Cache(int maxSize) {
        super(maxSize);
    }

    public void putAll(Map<String, ?> map) {

        if (map == null) throw new NullPointerException("The data hasn't initialized yet.");

        for (String key : map.keySet()) {
            if (!map.containsKey(key)) continue;

            put(key, map.get(key));
        }
    }

    public boolean clearAll(Map<String, ?> map) {
        if (map == null) throw new NullPointerException("The data hasn't initialized yet.");

        for (String key : map.keySet()) {
            if (!map.containsKey(key)) continue;

            remove(key);
        }

        if (map.size() == 0) {
            return true;
        }
        return false;
    }
}
