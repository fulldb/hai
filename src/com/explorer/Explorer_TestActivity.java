package com.explorer;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.explorer.adapter.FileExplorerAdapter;
import com.explorer.adapter.FileExplorerAdapter2;
import com.explorer.utils.FileUtil;
import com.explorer.utils.MyFileFilter;
import com.explorer.utils.OpenFiles;

public class Explorer_TestActivity extends Activity implements
		OnItemClickListener,OnClickListener {

	private static final String TAG = "Explorer_TestActivity";
	private File parentPath;
	private ListView listView;
	private Context context;
	private File[] listFiles;
	private FileExplorerAdapter adapter;
	private Parcelable state;
	private Parcelable state2;
	private TextView pathInfo;
	private ImageButton multiChoose;
	private TextView emptyFile;
	private ImageButton paste;
	private GridView gridView;
	
	private String copyPath;
	
	private boolean isPaste = false;
	
	private boolean isMultiChoose = false;
	
	private String currentPath = "/mnt/sdard/";
	
	private ImageButton showType;
	
	private FileExplorerAdapter2 adapter2;

	String fileType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main);
		context = this;
		initUI();
		pathInfo.setText("/SDCard");
		

		listFiles = getlistFiles();
		for (File file : listFiles) {
			Log.d("dead", file.getName() + "^^");
		}
		adapter = new FileExplorerAdapter(context, listFiles, listView,false);
		adapter2 = new FileExplorerAdapter2(context, listFiles, gridView, false);
		// 5.listview显示Adapter的数据
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(longlistener);
		multiChoose.setOnClickListener(this);
		paste.setOnClickListener(this);
		showType.setOnClickListener(this);
		gridView.setOnItemClickListener(itemClickListener);
	}

	private File[] getlistFiles() {
		// 1.先判断SDCARD是否正常
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// 2.获得SDCARD路径
			File file = Environment.getExternalStorageDirectory();
			// 3.得到所有的SDCARD的文件
			listFiles = file.listFiles(new MyFileFilter());
		} else {
			Toast.makeText(context, getString(R.string.sdcard_error),
					Toast.LENGTH_LONG).show();
		}
		// 4.将SDCARD的所有文件封装到Adapter上
		return listFiles = FileUtil.sort(listFiles);
	}

	private void initUI() {
		listView = (ListView) findViewById(R.id.listview);
		pathInfo = (TextView) findViewById(R.id.dir_info);
		multiChoose = (ImageButton) findViewById(R.id.multi_choose);
		emptyFile = (TextView) findViewById(R.id.empty_file);
		paste = (ImageButton) findViewById(R.id.paste);
		showType = (ImageButton) findViewById(R.id.show_type);
		gridView = (GridView) findViewById(R.id.gridview);
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			// 如果是目录，就进入到文件夹内部
						if (listFiles[position].isDirectory()) {
							parentPath = listFiles[position].getParentFile();
							String subPathName = listFiles[position].getName();
							pathInfo.append("/" + subPathName);
							listFiles = listFiles[position].listFiles(new MyFileFilter());
							if (listFiles.length <= 0) {
								emptyFile.setVisibility(View.VISIBLE);
							} else {
								emptyFile.setVisibility(View.GONE);
							}
							listFiles = FileUtil.sort(listFiles);
							adapter.updateFiles(listFiles);
							adapter.notifyDataSetChanged();
							
							adapter2.updateFiles(listFiles);
							adapter2.notifyDataSetChanged();
						} else {// 是文件的处理逻辑
							OpenFiles.open(context, listFiles[position]);
						}
		}
	};
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			// 如果是目录，就进入到文件夹内部
			if (listFiles[position].isDirectory()) {
				parentPath = listFiles[position].getParentFile();
				String subPathName = listFiles[position].getName();
				pathInfo.append("/" + subPathName);
				listFiles = listFiles[position].listFiles(new MyFileFilter());
				if (listFiles.length <= 0) {
					emptyFile.setVisibility(View.VISIBLE);
				} else {
					emptyFile.setVisibility(View.GONE);
				}
				listFiles = FileUtil.sort(listFiles);
				adapter.updateFiles(listFiles);
				adapter.notifyDataSetChanged();
			} else {// 是文件的处理逻辑
				OpenFiles.open(context, listFiles[position]);
			}
	}
	
	private OnItemLongClickListener  longlistener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {

			File file = listFiles[position];
			
			if (file.isDirectory()) {
				fileType = "文件夹";
			} else {
				fileType = "文件";
			}
			listView.setOnCreateContextMenuListener(menuList);
			return false;
		}
		
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 当用户按返回键的时候，返回上一级的目录
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (parentPath != null && !parentPath.getName().equals("") && !parentPath.getName().equals("mnt")) {
				listFiles = parentPath.listFiles(new MyFileFilter());
				listFiles = FileUtil.sort(listFiles);
				adapter.updateFiles(listFiles);
				adapter.notifyDataSetChanged();
				adapter2.updateFiles(listFiles);
				adapter2.notifyDataSetChanged();
				parentPath = parentPath.getParentFile();

				String currentPath = pathInfo.getText().toString();
				int indexOf = currentPath.lastIndexOf("/");
				if (indexOf != -1) {
					currentPath = currentPath.substring(0, indexOf);
					pathInfo.setText(currentPath);
				}
				emptyFile.setVisibility(View.GONE);
				setListViewIndex();
			} else {// 到了根目录，就关闭
				finish();
			}
		}

//		return super.onKeyDown(keyCode, event);
		return false;//返回true，菜单键可能无法显示
	}

	private void setListViewIndex() {
		if (adapter2.state != null) {
			listView.setAdapter(adapter);
			listView.onRestoreInstanceState(adapter.state);
			
			gridView.setAdapter(adapter2);
			gridView.onRestoreInstanceState(adapter2.state);
			Log.e(TAG,
					"adapter.state not null :"
							+ adapter.state.toString());
		} else {
			Log.e(TAG, "adapter.state null :" + adapter.state);
		}
	}
	
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//		switch (scrollState) {
//
//		case OnScrollListener.SCROLL_STATE_FLING:
//			Log.d(TAG, "SCROLL_STATE_FLING");// 滑动中
//			break;
//		case OnScrollListener.SCROLL_STATE_IDLE:
//			state = listView.onSaveInstanceState();
//			Log.d(TAG, "SCROLL_STATE_IDLE");// 停止滑动时
//			break;
//		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//			Log.d(TAG, "SCROLL_STATE_TOUCH_SCROLL");// 触摸滚动屏幕，并且手指仍然在屏幕上
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//
//	}

	ListView.OnCreateContextMenuListener menuList = new ListView.OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			// 添加菜单项
			//动态设置图标或者标题
			menu.setHeaderTitle("操作" + fileType);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.file_context_menu, menu);

		}
	};

	// 选中菜单Item后触发
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// 关键代码在这里
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		//当前item的position
		int position = menuInfo.position;
		final File file = listFiles[position];
		
		switch (item.getItemId()) {
		case R.id.copy:
			// 输出position
			copyPath = file.getAbsolutePath();
			Toast.makeText(Explorer_TestActivity.this, "当前文件名：" 
					+ file.getName() 
					+ " 复制 "
					+ "这是一个" + fileType
					,
					Toast.LENGTH_LONG).show();
			isPaste = true;
			paste.setImageResource(R.drawable.toolbar_paste);
			
			break;
		case R.id.rename:
			final EditText et = new EditText(context);
			et.setText(file.getName());
			et.selectAll();
			et.setHint("输入文件夹名字");
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("重命名");
			dialog.setView(et);
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String path = file.getAbsolutePath();
					if (path != null && !"".equals(path) && et.getText().toString().length() > 0) {
						int lastIndexOf = path.lastIndexOf("/");
						String path1 = path.substring(0, lastIndexOf + 1);
						boolean b = file.renameTo(new File(path1 + et.getText().toString()));
						if (b) {
							adapter.updateFiles(getlistFiles());
							adapter.notifyDataSetChanged();
							Toast.makeText(context, "rename success!,new name is " + file.getName(), 0).show();
						} else {
							Toast.makeText(context, "rename fail!", 0).show();
						}
					}
				}
			});
			dialog.show();
			
			break;
		case R.id.delete:
			
			break;
		default:
			break;
		}
		
		return super.onContextItemSelected(item);

	}

//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		menu.add(0, 1, Menu.NONE, "复制");
//		menu.add(0, 2, Menu.NONE, "粘贴");
//		menu.add(0, 3, Menu.NONE, "重命名");
//		menu.add(0, 4, Menu.NONE, "删除");
//	}
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);  
        getMenuInflater().inflate(R.menu.app_menu, menu);
		return true;  
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.create_dir:
			
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("新建文件夹");
			final EditText et = new EditText(this);
			b.setView(et);
			b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					File file = new File("/mnt/"+pathInfo.getText().toString().toLowerCase() + "/" + et.getText().toString());
					Toast.makeText(context, "/mnt"+pathInfo.getText().toString().toLowerCase()  + "/" + et.getText().toString(), 1).show();
					if (!file.exists()) {
						file.mkdir();
						Toast.makeText(context, "不存在，创建", 1).show();
					} else {
						Toast.makeText(context, "存在，不用创建", 1).show();
					}
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			b.show();
			break;

		default:
			break;
		}
        return true;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.multi_choose:
			state = adapter.state;
			if (isMultiChoose) {
				Toast.makeText(context, "未选中", 0).show();
				isMultiChoose = false;
				
				multiChoose.setImageResource(R.drawable.toolbar_select);
				adapter = new FileExplorerAdapter(context, listFiles, listView,false);
				listView.setAdapter(adapter);
				
				if (state != null) {
					listView.onRestoreInstanceState(state);
				} else {
					Log.e(TAG, "state null :" + state);
				}
				if (state == null && state2 != null) {
					listView.onRestoreInstanceState(state2);
				}
				
			} else {
				state = adapter.state;
				isMultiChoose = true;
				
				multiChoose.setImageResource(R.drawable.toolbar_select_focus);
				adapter = new FileExplorerAdapter(context, listFiles, listView,true);
				listView.setAdapter(adapter);
				
				if (state != null) {
					listView.onRestoreInstanceState(state);
					state2 = state;
				} else {
					Log.e(TAG, "state null :" + state);
				}
				if (state == null && state2 != null) {
					listView.onRestoreInstanceState(state2);
				}
			}
			
			break;
		case R.id.paste:
			try {
				boolean b = FileUtil.copy(copyPath, "/mnt"+pathInfo.getText().toString().toLowerCase() + "/");
				if(b) {
					Toast.makeText(context, "复制成功", 1).show();
					adapter.updateFiles(new File("/mnt"+pathInfo.getText().toString().toLowerCase() + "/").listFiles(new MyFileFilter()));
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(context, "复制失败", 1).show();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case R.id.show_type:
			gridView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			gridView.setAdapter(adapter2);
			break;
		default:
			break;
		}
		
	}



}