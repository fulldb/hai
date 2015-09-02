package com.explorer.adapter;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.explorer.R;
import com.explorer.utils.ApkUtil;
import com.explorer.utils.AsyncLoadImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileExplorerAdapter extends BaseAdapter implements OnScrollListener {

	private static final String TAG = "FileExplorerAdapter";
	private File[] listFiles;
	private LayoutInflater inflater;
	private AsyncLoadImage asyncLoadImage;
	private Context context;
	private static int LISTVIEW_STATE = -1;
	private List<ImageView> imageViewList;
	public TextView emptyText;
	public Parcelable  state;
	private  Map<Integer, Boolean> isSelected;    //使用checkbox选中效果
	private boolean isShow;
	private boolean isLoadImage = true;
	
	public FileExplorerAdapter(Context context,File[] listFiles,ListView listView,boolean isShow){
		this.context = context;
		this.listFiles = listFiles;
		this.inflater = LayoutInflater.from(context);
		this.asyncLoadImage = new AsyncLoadImage(context,new Handler());
		listView.setOnScrollListener(this);
		init();
		this.isShow = isShow;
		imageViewList = new ArrayList<ImageView>();
	}
	
	
	 //初始化    
	private void init() {    
        //定义isSelected这个map记录每个listitem的状态，初始状态全部为false未勾选状态。 
		isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < listFiles.length; i++) {    
            isSelected.put(i, false);    
        }    
    } 
	
	public void updateFiles(File[] listFiles){
		this.listFiles = listFiles;
	}
	
	@Override
	public int getCount() {
		return listFiles == null ? 0 : this.listFiles.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.file_item, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.file_icon);
			holder.textView = (TextView) convertView.findViewById(R.id.file_name);
			holder.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
			holder.cb.setVisibility(this.isShow ? View.VISIBLE : View.GONE);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//如果是文件夹就显示文件夹的图标
		if(listFiles[position].isDirectory()){
			File[] listFiles2 = listFiles[position].listFiles();
			
			if(listFiles2 == null || listFiles2.length == 0) {//空文件夹
				holder.imageView.setImageResource(R.drawable.folder);
				holder.textView.setText(listFiles[position].getName());
			}else{//长度大于0就是有文件的文件夹
				holder.imageView.setImageResource(R.drawable.folder_);
				holder.textView.setText(listFiles[position].getName());
			}
		}else {//如果是文件，就显示文件图标
			String fileName = listFiles[position].getName().toLowerCase();
			if(fileName.endsWith(".doc")){
				
			}else if(fileName.endsWith(".jpg")
					|| fileName.endsWith(".jpeg")
					|| fileName.endsWith(".png")
					|| fileName.endsWith(".bmp")
					){
				final String path = listFiles[position].getAbsolutePath();
				
				/**
				 * 方法1：不推荐
				 */
//				BitmapFactory.Options option = new BitmapFactory.Options();
//				option.inSampleSize = 2;
//				Bitmap bm = BitmapFactory.decodeFile(path, option);
//				holder.imageView.setImageBitmap(bm);
				
				holder.imageView.setTag(path);
				
				/**
				 * 方法2：不太推荐
				 */
//				if (isLoadImage == true) {
//					asyncLoadImage.loadImage(holder.imageView, 100);
//				} else {
//					imageViewList.add(holder.imageView);
//					Log.d(TAG, "不加载,isLoadImage:" + isLoadImage);
//				}
				
				/**
				 * 方法3：推荐
				 */
				asyncLoadImage.loadImage2(holder.imageView, 100);
				
				holder.textView.setText(listFiles[position].getName());
			} else if (fileName.endsWith(".apk")){
				holder.imageView.setImageDrawable(ApkUtil.getApkIcon3(context, listFiles[position].getAbsolutePath()));
				holder.textView.setText(listFiles[position].getName());
			} else if (fileName.endsWith(".mp4")
					||fileName.endsWith(".3gp")
					||fileName.endsWith(".avi")
					||fileName.endsWith(".flv")
					){
				Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(listFiles[position].getAbsolutePath(), Thumbnails.MICRO_KIND);
				holder.imageView.setImageBitmap(bitmap);
				holder.textView.setText(listFiles[position].getName());
			} else if (fileName.endsWith(".mp3")
					||fileName.endsWith(".ape")
					||fileName.endsWith(".ogg")
					){
				holder.imageView.setImageResource(R.drawable.format_music);
				holder.textView.setText(listFiles[position].getName());
			} else {
				holder.imageView.setImageResource(R.drawable.file);
				holder.textView.setText(listFiles[position].getName());
			}
		}
		final CheckBox cb = holder.cb;
		cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Boolean b = isSelected.get(position);
				if (b) {
					isSelected.put(position, false);
					cb.setChecked(false);
				} else {
					isSelected.put(position, true);
					cb.setChecked(true);
					String path = listFiles[position].getAbsolutePath();
					Toast.makeText(context, path, 1).show();
				}
			}
		});
		if (holder.cb == null) {
			Log.e(TAG, "cb is null");
		}
		if (isSelected == null ) {
			Log.e(TAG, "isSelected is null");
		}
		holder.cb.setChecked(isSelected.get(position));
		
		return convertView;
		
	}
	private static final class ViewHolder{
		private ImageView imageView;
		private TextView textView;
		private CheckBox cb;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_FLING:
			Log.e(TAG, "adapter:"+"SCROLL_STATE_FLING");
			isLoadImage = false;
			if (asyncLoadImage != null ) {
				asyncLoadImage.lock();
			}
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			state = view.onSaveInstanceState();
			Log.e(TAG, "adapter:"+"SCROLL_STATE_IDLE" + " state:" + state);
			@Deprecated
			String Deprecated;
			/**
			 * 方法2：不太推荐
			 */
//			isLoadImage = true;
//			for (ImageView img : imageViewList) {
//				asyncLoadImage.loadImage(img, 100);
//			}
//			imageViewList.clear();
			
			/**
			 * 方法3：推荐
			 */
			if (asyncLoadImage != null ) {
				asyncLoadImage.unlock();
			}
			
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			Log.e(TAG, "adapter:"+"SCROLL_STATE_TOUCH_SCROLL");
			isLoadImage = false;
			if (asyncLoadImage != null ) {
				asyncLoadImage.lock();
			}
			break;

		default:
			break;
	}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
			
}
