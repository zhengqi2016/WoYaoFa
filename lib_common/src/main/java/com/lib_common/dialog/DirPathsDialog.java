package com.lib_common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.lib_common.R;
import com.lib_common.activity.ImageListActivity;
import com.lib_common.activity.ImageListActivity.ImageBean;
import com.lib_common.util.MImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirPathsDialog extends Dialog {
	private Context context;
	/**
	 * 包含图片的目录集合以及目录下的图片路径集合
	 */
	private HashMap<String, List<ImageListActivity.ImageBean>> mDirFilePaths = new HashMap<String, List<ImageBean>>();
	/**
	 * 包含图片的目录集合（用于获取目录下的图片集合）
	 */
	private List<String> mDirPaths = new ArrayList<String>();
	/**
	 * sd卡的跟目录
	 */
	private String rootPath = Environment.getExternalStorageDirectory()
			.getPath();
	private ListView lvDirs;
	private DirPathsDialogAdapter adapter;

	public DirPathsDialog(Context context, int theme,
			HashMap<String, List<ImageListActivity.ImageBean>> mDirFilePaths,
			List<String> mDirPaths) {
		super(context, theme);
		this.mDirFilePaths = mDirFilePaths;
		this.mDirPaths = mDirPaths;
		this.init(context);
	}

	protected DirPathsDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.init(context);
	}

	public DirPathsDialog(Context context,
			HashMap<String, List<ImageBean>> mDirFilePaths,
			List<String> mDirPaths) {
		super(context);
		this.mDirFilePaths = mDirFilePaths;
		this.mDirPaths = mDirPaths;
		this.init(context);
	}

	private void init(Context context) {
		this.context = context;
		View view = View.inflate(context, R.layout.dir_paths_dialog, null);
		setContentView(view);
		lvDirs = (ListView) findViewById(R.id.dir_paths_dialog_lv_dirs);
		adapter = new DirPathsDialogAdapter();
		lvDirs.setAdapter(adapter);
		Window window = getWindow(); // 得到对话框
		window.setGravity(Gravity.BOTTOM);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	class DirPathsDialogAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDirPaths.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(context,
						R.layout.dir_paths_dialog_adapter, null);
				holder.ivFirstImage = (ImageView) convertView
						.findViewById(R.id.dir_paths_dialog_adapter_iv_first_image);
				holder.llRoot = (LinearLayout) convertView
						.findViewById(R.id.dir_paths_dialog_adapter_ll_root);
				holder.tvNum = (TextView) convertView
						.findViewById(R.id.dir_paths_dialog_adapter_tv_number);
				holder.tvPath = (TextView) convertView
						.findViewById(R.id.dir_paths_dialog_adapter_tv_path);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			try {
				// 这个顺序主要是为了有异常之后功能不缺失
				String item = mDirPaths.get(position);
				setListener(holder, item, position);
				// 加载图片
				setImage(mDirFilePaths.get(item).get(0), holder);
				String[] ss = item.split("/", 4);
				holder.tvPath.setText(ss[ss.length - 1]);
				List<ImageBean> files = mDirFilePaths.get(item);
				holder.tvNum.setText("" + files.size());
				// 设置单击事件
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

		private void setListener(Holder holder, final String item,
				final int position) {
			holder.llRoot.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (onClickDirListener != null) {
						onClickDirListener.clickDirListener(item, position);
					}
				}

			});
		}

		/**
		 * 根据url加载图片
		 * 
		 * @param holder
		 * @param entity
		 */
		private void setImage(ImageListActivity.ImageBean entity, Holder holder) {
			if (entity.getImageUrl().contains(rootPath)) {
				MImageLoader.displayWithDefaultOptions(context,
						entity.getImageUrl(), holder.ivFirstImage);
			} else {
				MImageLoader.displayWithDefaultOptions(context,
						entity.getImageUrl(), holder.ivFirstImage);
			}
			holder.ivFirstImage.setTag(entity.getImageUrl());
		}

		@Override
		public Object getItem(int arg0) {
			return mDirPaths.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
	}

	public class Holder {
		public ImageView ivFirstImage;
		public TextView tvPath;
		public TextView tvNum;
		public LinearLayout llRoot;
	}

	private OnClickDirListener onClickDirListener;

	public void setOnClickDirListener(OnClickDirListener onClickDirListener) {
		this.onClickDirListener = onClickDirListener;
	}

	public interface OnClickDirListener {
		void clickDirListener(String item, int position);
	}

}
