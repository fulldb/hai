package com.explorer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.util.Log;

public class FileUtil {
	
	private static final String TAG = FileUtil.class.getSimpleName();

	/**
	 * ���ļ��к��ļ�����
	 * @param files
	 * @return File[] listFiles
	 */
	public static File[] sort(File[] files){
//		List<File> list = new ArrayList<File>();
		List<File> list = Arrays.asList(files);
		
//		for(File file : files){
//				list.add(file);
//		}

		Collections.sort(list, new FileComparator());
		File[] array = list.toArray(new File[list.size()]);
		return array;
	}
	static boolean isSuccess = false;
	public static boolean copy(String fromPath,String toPath) throws IOException {
		
		//1.��ԭʼ·����Ŀ��·��תΪFile
		File fromFile = new File(fromPath);
		if (!fromFile.exists()) {
			isSuccess = false;
		}
		File toFile = new File(toPath);
		//2.���Ŀ��·�������ڣ��򴴽���·��
		if (!toFile.exists()) {
			toFile.mkdir();
		}
		//3.�ж�ԭʼ·����һ��Ŀ¼����һ���ļ��������һ���ļ���ֱ�Ӹ��ƣ�
		//		�ݹ����
		
		if (fromFile.isFile()) {
			isSuccess = copyFile(fromFile.getAbsolutePath(),toPath + fromFile.getName());
		} else if (fromFile.isDirectory()) {
//			fromFile = new File(fromFile.getAbsoluteFile() + "/");
			File[] listFiles = fromFile.listFiles();
			
			for (File file : listFiles) {
				if (file.isDirectory()) {
					//4.�����һ���ļ��У��õ����ļ����µ������ļ�����Ҫ�����ļ��е������ļ������ƣ��ļ�����Ҫ
					//		�ݹ����,��Ϊ���ļ��У��������Ҫ����"/"
					copy(file.getAbsolutePath() + "/", toPath + file.getName() + "/");
				} else {
					//5.��һ��Ŀ¼����һ���ļ��������һ���ļ���ֱ�Ӹ��ƣ�
					isSuccess = copyFile(file.getAbsolutePath(),toPath + file.getName());
				}
			}
		}
		
		if (!isSuccess) {
			Log.d(TAG, "fail");
		}
		
		return isSuccess;
	}

	/**
	 * �����ļ�
	 * @param fromPath
	 * @param toPath
	 * @throws IOException 
	 */
	private static boolean copyFile(String fromPath, String toPath) {
		
		boolean isSuccess = true;
		
		InputStream is = null ;
		OutputStream os = null;
		try {
			//1.��Դ�ļ�·����Ŀ���ļ�·��תΪFile����
			File fromFile = new File(fromPath);
			File toFile = new File(toPath);
			//2.�õ�һ���������������
			is = new FileInputStream(fromFile);
			os = new FileOutputStream(toFile);
			//3.���������ļ��������д�ļ�������һ������������߶�дЧ��
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			
		} catch (FileNotFoundException e) {
			isSuccess = false;
			Log.e(TAG, "copyFile",e);
		} catch (IOException e) {
			isSuccess = false;
			Log.e(TAG, "copyFile",e);
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				isSuccess = false;
				Log.e(TAG, "copyFile",e);
			}
		}
		return isSuccess;
	}
	
}
