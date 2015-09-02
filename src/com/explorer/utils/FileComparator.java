package com.explorer.utils;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {

	@Override
	public int compare(File f1, File f2) {
		//1.�ȱȽ��ļ���
		if (f1.isDirectory() && f2.isDirectory()) {
			return f1.getName().compareToIgnoreCase(f2.getName());
		} else {
			if (f1.isDirectory() && f2.isFile()) {//2.�Ƚ��ļ��к��ļ�
				return -1;//Ϊ�����ļ��������ļ�ǰ�ߣ����Է���0������С��0������f1�ͻ�����f2ǰ��
			} else if (f1.isFile() && f2.isDirectory()){//3.�Ƚ��ļ����ļ���
				return 1;//Ϊ�����ļ��������ļ�ǰ�ߣ����Է���1�����ش���0������f1�ͻ�����f2���
			} else {//4.�Ƚ��ļ�
				return f1.getName().compareToIgnoreCase(f2.getName());
			}
		}
	}

}
