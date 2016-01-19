package com.lib_common.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.lib_common.R;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 个人资料
 * 
 * @author loar
 * 
 */
public class TouchImageAct extends Activity {

	public static final String BUNDLE_KEY = "imageUrl";
	public static final String BUNDLE_KEY_POSITION = "position";

	ViewPager vpImages;

	private ArrayList<ImageListActivity.ImageBean> entites;

	private ImageAdapter adapter;
	private int position;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_touch_image);
		try {
			entites = (ArrayList<ImageListActivity.ImageBean>) getIntent()
					.getExtras().getSerializable(BUNDLE_KEY);
			position = getIntent().getExtras().getInt(BUNDLE_KEY_POSITION);
			init();
		} catch (Exception e) {
			ToastUtil.printErr(e);
		}
	}

	private void init() {
		vpImages = (ViewPager) findViewById(R.id.touch_image_vp_images);
		adapter = new ImageAdapter();
		vpImages.setAdapter(adapter);
		vpImages.setCurrentItem(position);
	}

	class ImageAdapter extends PagerAdapter {

		public ImageAdapter() {
			setData();
		}

		private void setData() {
			if (entites == null) {
				entites = new ArrayList<ImageListActivity.ImageBean>();
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return entites == null ? 0 : entites.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View view, int position, Object object) {
			View v = ((View) object);
			PhotoViewAttacher mAttacher = (PhotoViewAttacher) v.getTag();
			mAttacher.cleanup();
			((ViewPager) view).removeView(v);
		}

		@Override
		public Object instantiateItem(View view, int position) {
			ImageView iv = new ImageView(TouchImageAct.this);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			iv.setLayoutParams(params);
			final PhotoViewAttacher mAttacher = new PhotoViewAttacher(iv);
			iv.setTag(mAttacher);
			mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

				@Override
				public void onViewTap(View view, float x, float y) {
					finish();
				}
			});
			ImageListActivity.ImageBean item = entites.get(position);
			MImageLoader.display(TouchImageAct.this, item.getImageUrl(), iv,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
													FailReason arg2) {
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
													  Bitmap arg2) {
							mAttacher.update();
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});
			((ViewPager) view).addView(iv);
			return iv;
		}
	}

}
