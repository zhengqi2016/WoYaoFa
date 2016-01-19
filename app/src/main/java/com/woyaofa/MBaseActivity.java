package com.woyaofa;

import android.graphics.Rect;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lib_common.activity.BaseActivity;
import com.woyaofa.config.Config;
import com.woyaofa.js.JSComm;
import com.woyaofa.ui.widget.HeadView;

import butterknife.Bind;

/**
 * Created by LoaR on 15/11/12.
 */
public class MBaseActivity extends BaseActivity {

    @Nullable
    @Bind(R.id.hv_head)
    public HeadView hvHead;



    @Override
    public void setContentViews(int layoutResID) {
        super.setContentViews(layoutResID);
        if (hvHead != null) {
            hvHead.setOnClickLeftListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBack(v);
                }
            });
        }
    }

    /**
     * 用于键盘挡住视图时用的
     *
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    protected void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }
}
