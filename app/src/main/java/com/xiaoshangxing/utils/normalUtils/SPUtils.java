package com.xiaoshangxing.utils.normalUtils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.xiaoshangxing.utils.XSXApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SPUtils
{
	public static final String DEFAULT_STRING ="DEFAULT_STRING";//默认字符
	public static final int DEFAULT_int=0;
	public static final Long DEFAULT_LONG=0L;
	/*
	**describe:登录注册有关键值对
	*/
	public static final String IS_FIRS_COME = "IS_FIRS_COME";//记录是否为第一次进入APP
	public static final String IS_NEED_GUIDE = "IS_NEED_GUIDE";//记录是否需要引导
	public static final String IS_QUIT = "IS_QUIT";//记录是否有退出账号的操作
	public static final String PHONENUMNBER = "PHONENUMNBER";//记录当前存储的账号
	public static final String CURRENT_COUNT_HEAD = "CURRENT_COUNT_HEAD";//记录当前存储的账号的头像
	public static final String ID="ID"; //当前账号的id

	public static final String DIGEST="DIGEST";
	public static final String TOKEN = "TOKEN";

	/*
	**describe:遇见
	*/
	public static final String EarPhone = "EarPhone";//听筒状态
	public static final String NewNotice = "NewNotice";//新消息通知
	public static final String HideNewsDetail = "HideNewsDetail";//新消息详情
	public static final String NewsSound = "NewsSound";//新消息声音
	public static final String NewsVibrate = "NewsVibrate";//新消息振动
	public static final String NewsForXiaoyou = "NewsForXiaoyou";//校友圈更新
	public static final String NoDisturb = "NoDisturb";//消息免打扰选项 0:开启 1：夜间开启 2：关闭

	private final static String PERSONAL_SETTING = "PERSONAL_SETTING";

	/*
	**describe:动态发布拉取
	*/


	public SPUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 保存在手机里面的文件名
	 */
	public static final String FILE_NAME = "share_data";

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * 
	 * @param context
	 * @param key
	 * @param object
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
		} else
		{
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param context
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

	/*
	**describe:存储个人设置
	*/
	public static void savePersonalSetting(StatusBarNotificationConfig config) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("downTimeBegin", config.downTimeBegin);
			jsonObject.put("downTimeEnd", config.downTimeEnd);
			jsonObject.put("downTimeToggle", config.downTimeToggle);
			jsonObject.put("ring", config.ring);
			jsonObject.put("vibrate", config.vibrate);
			jsonObject.put("notificationSmallIconId", config.notificationSmallIconId);
			jsonObject.put("notificationSound", config.notificationSound);
			jsonObject.put("hideContent", config.hideContent);
			jsonObject.put("ledargb", config.ledARGB);
			jsonObject.put("ledonms", config.ledOnMs);
			jsonObject.put("ledoffms", config.ledOffMs);
			jsonObject.put("titleOnlyShowAppName", config.titleOnlyShowAppName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		put(XSXApplication.getInstance(), PERSONAL_SETTING, jsonObject.toString());
	}

	/*
	**describe:获取个人设置
	*/
	public static StatusBarNotificationConfig getConfig() {
		StatusBarNotificationConfig config = new StatusBarNotificationConfig();
		String jsonString = (String) get(XSXApplication.getInstance(), PERSONAL_SETTING, "");
		try {
			JSONObject jsonObject = JSONObject.parseObject(jsonString);
			if (jsonObject == null) {
				return null;
			}
			config.downTimeBegin = jsonObject.getString("downTimeBegin");
			config.downTimeEnd = jsonObject.getString("downTimeEnd");
			config.downTimeToggle = jsonObject.getBoolean("downTimeToggle");
			config.ring = jsonObject.getBoolean("ring");
			config.vibrate = jsonObject.getBoolean("vibrate");
			config.notificationSmallIconId = jsonObject.getIntValue("notificationSmallIconId");
			config.notificationSound = jsonObject.getString("notificationSound");
			config.hideContent = jsonObject.getBoolean("hideContent");
			config.ledARGB = jsonObject.getIntValue("ledargb");
			config.ledOnMs = jsonObject.getIntValue("ledonms");
			config.ledOffMs = jsonObject.getIntValue("ledoffms");
			config.titleOnlyShowAppName = jsonObject.getBoolean("titleOnlyShowAppName");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return config;
	}


	/**
	 * 移除某个key值已经对应的值
	 * 
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
	 * 
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
	 * 
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

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author zhy
	 * 
	 */
	private static class SharedPreferencesCompat
	{
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
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
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
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
			} catch (IllegalArgumentException e)
			{
			} catch (IllegalAccessException e)
			{
			} catch (InvocationTargetException e)
			{
			}
			editor.commit();
		}
	}

}