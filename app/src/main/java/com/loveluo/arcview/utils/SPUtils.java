package com.loveluo.arcview.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 罗富清
 */
public class SPUtils {
    /**
     * file name
     */
    private static final String FILE_NAME = "share_data";

    public static  final String LANGUAGE = "LANGUAGE";

    public static  final String UPDATE_LANGUAGE = "UPDATE_LANGUAGE";

    public static final String COUNTRY = "COUNTRY";

    public static final String USER_NAME = "USER_NAME";

    public static final String ASSET_PASS = "ASSET_PASS";

    public static final String IS_GA = "IS_GA";

    public static final String UID = "UID";

    public static final String APP_ID = "APP_ID";

    public static final String GT_COOKIE = "GT_COOKIE";
    /**
     * 是否已经等录了
     */
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String BC = "BTC";
    public static final String OC = "USDT";
    /**
     * 是那种币
     */
    public static String FEX = "FEX";

    /**
     * save date
     *
     * @param context c
     * @param key k
     * @param object value
     */
    public static void put(Context context, String key, Object object)
    {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String)
        {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer)
        {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean)
        {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float)
        {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long)
        {
            editor.putLong(key, (Long) object);
        } else {
            if(object == null){
                editor.putString(key, null);
            } else {
                editor.putString(key, object.toString());
            }
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context 上
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String)
        {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer)
        {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean)
        {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float)
        {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long)
        {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     * @param context
     * @param key
     */
    public static void remove(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     * @param context
     */
    public static void clear(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    public static String[] getSharedPreference(Context mContext, String key) {
        String regularEx = "#";
        String[] str;
        SharedPreferences sp = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        String values;
        values = sp.getString(key, "");
        if(values.length() <= 0) {
            return null;
        }
        str = values.split(regularEx);

        return str;
    }

    public static void setSharedPreference(Context mContext, String key, String[] values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
        }
        SharedPreferences.Editor et = sp.edit();
        et.putString(key, str);
        et.apply();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     *
     */
    private static class SharedPreferencesCompat
    {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return r
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private static Method findApplyMethod()
        {
            try
            {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor e
         */
        public static void apply(SharedPreferences.Editor editor)
        {
            try
            {
                if (sApplyMethod != null)
                {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
            editor.commit();
        }
    }


}
