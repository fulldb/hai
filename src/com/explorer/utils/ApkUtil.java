package com.explorer.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

public class ApkUtil {
	
	public static Drawable getApkIcon3(Context context,String apkPath) {
		//获得包管理器
		PackageManager pm = context.getPackageManager();
		//获得指定路径的apk文件的相关信息
		PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.publicSourceDir = apkPath;
			Drawable icon = appInfo.loadIcon(pm);
			return icon;
		}
		
		return null;
	}

	public static Drawable getApkIcon2(Context context, String apkPath) {
		// 1.先获得包管理器
		PackageManager pm = context.getPackageManager();
		// 2.通过包管理器获得指定应用的包的相关信息
		PackageInfo info = pm.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
		if (info != null) {
			// 3.通过PackageInfo获得应用信息类ApplicationInfo
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.publicSourceDir = apkPath;
			// 4.通过ApplicationInfo获得该apk的图标
			Drawable icon = appInfo.loadIcon(pm);
			return icon;
		}
		return null;
	}

	public static Drawable test(Context context, String apkPath) {

		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);

		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			String appName = pm.getApplicationLabel(appInfo).toString();
			String packageName = appInfo.packageName; // 得到安装包名称
			String version = info.versionName; // 得到版本信息
			Toast.makeText(
					context,
					"packageName:" + packageName + ";version:" + version
							+ "name:" + appName, Toast.LENGTH_LONG).show();
			appInfo.publicSourceDir = apkPath;
			Drawable icon = pm.getApplicationIcon(appInfo);// 得到图标信息
			return icon;
		}
		return null;
	}
}
