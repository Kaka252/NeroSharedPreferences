package leo.nero.com.leo.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.LruCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import leo.nero.com.leo.App;
import leo.nero.com.leo.cache.Cache;

/**
 * Created by zhouyou on 2015/7/17.
 */
public class Nero {

    /**
     * 声明实例：该实例在App启动之后声明，并在Application的生命周期里保存存活状态
     */
    private static final Nero INSTANCE = new Nero();

    private static String SP_NAME = App.getInstance().getPackageName();

    private Context mContext;

    /**
     * 该标记位用来确保在访问实例的时候，该实例已经初始化
     */
    private boolean wasInitialized = false;


    /**
     * in-memory data | 在内存中记录偏好数据（有缓存的）
     */
    private Cache cache;

    private Nero() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        cache = new Cache(cacheSize);
    }

    private void initContext(Context context) {
        mContext = context.getApplicationContext();
        SharedPreferences sp = getSharedPreferences();
        cache.putAll(sp.getAll());
        wasInitialized = true;
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 核心调用方法，在App的onCreate()的方法中进行注册
     * 单例模式
     */
    public static synchronized Nero init(Context context) {
        if (context == null) {
            throw new RuntimeException("You have no context and you need to initialize it first.");
        }

        if (!INSTANCE.wasInitialized) {
            INSTANCE.initContext(context);
        }

        return INSTANCE;
    }

    private static Nero getInstance() {
        if (!INSTANCE.wasInitialized) {
            throw new RuntimeException("You must call Nero.init() before using this.");
        }
        return INSTANCE;
    }

    /**
     * *********************************** 保存|获取数据 ******************************************
     */
    /**
     * 写入数据
     *
     * @param key
     * @param value
     * @return
     */
    private boolean saveIn(String key, Object value) {
        boolean isSuccess = false;

        SharedPreferences.Editor editor = getSharedPreferences().edit();

        boolean isPut = true;
        if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            isPut = false;
        }

        if (isPut) {
            isSuccess = editor.commit();
            cache.put(key, value);
        }

        return isSuccess;
    }

    /**
     * 从内存中读取偏好数据
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T get(String key, Class<T> clazz) {
        T obj = null;
        Object value;
        if (containsKey(key)) {
            value = cache.get(key);
        } else {
            SharedPreferences sp = getInstance().getSharedPreferences();
            Map<String, ?> map = sp.getAll();
            if (!map.containsKey(key)) return null;
            value = map.get(key);
        }

        if (clazz.isInstance(value)) {
            obj = clazz.cast(value);
        }
        return obj;
    }

    /**
     * *********************************** 以下为put、get ******************************************
     */

    public static void putFloat(String key, float value) {
        getInstance().saveIn(key, value);
    }

    public static void putLong(String key, long value) {
        getInstance().saveIn(key, value);
    }

    public static void putString(String key, String value) {
        getInstance().saveIn(key, value);
    }

    public static void putBoolean(String key, boolean value) {
        getInstance().saveIn(key, value);
    }

    public static void putInt(String key, int value) {
        getInstance().saveIn(key, value);
    }

    public static float getFloat(String key, float defValue) {
        Float value = getInstance().get(key, Float.class);
        if (value != null) {
            return value;
        }
        return defValue;
    }

    public static int getInt(String key, int defValue) {
        Integer value = getInstance().get(key, Integer.class);
        if (value != null) {
            return value;
        }
        return defValue;
    }

    public static long getLong(String key, long defValue) {
        Long value = getInstance().get(key, Long.class);
        if (value != null) {
            return value;
        }
        return defValue;
    }

    public static String getString(String key, String defValue) {
        String value = getInstance().get(key, String.class);
        if (value != null) {
            return value;
        }
        return defValue;
    }

    public static boolean getBoolean(String key, boolean defValue) {
        Boolean value = getInstance().get(key, Boolean.class);
        if (value != null) {
            return value;
        }
        return defValue;
    }

    /**
     * ******************************* 额外判断key值是否存在的方法 ********************************
     */
    public static boolean containsKey(String key) {
        if (getInstance().cache.get(key) == null) return false;
        return true;
    }

    /**
     * ******************************* 删除偏好 *****************************************************
     */
    public static void removeAt(String key) {
        getInstance().cache.remove(key);
        SharedPreferences.Editor editor = getInstance().getSharedPreferences().edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clearAll() {
        SharedPreferences sp = getInstance().getSharedPreferences();
        getInstance().cache.clearAll(sp.getAll());

        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


}
