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
		//��ð�������
		PackageManager pm = context.getPackageManager();
		//���ָ��·����apk�ļ��������Ϣ
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
		// 1.�Ȼ�ð�������
		PackageManager pm = context.getPackageManager();
		// 2.ͨ�������������ָ��Ӧ�õİ��������Ϣ
		PackageInfo info = pm.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
		if (info != null) {
			// 3.ͨ��PackageInfo���Ӧ����Ϣ��ApplicationInfo
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.publicSourceDir = apkPath;
			// 4.ͨ��ApplicationInfo��ø�apk��ͼ��
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
			String packageName = appInfo.packageName; // �õ���װ������
			String version = info.versionName; // �õ��汾��Ϣ
			Toast.makeText(
					context,
					"packageName:" + packageName + ";version:" + version
							+ "name:" + appName, Toast.LENGTH_LONG).show();
			appInfo.publicSourceDir = apkPath;
			Drawable icon = pm.getApplicationIcon(appInfo);// �õ�ͼ����Ϣ
			return icon;
		}
		return null;
	}
}
