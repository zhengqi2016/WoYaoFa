package com.lib_common;

import android.app.Application;

import com.lib_common.bean.UserBean;
import com.lib_common.disk.UserSP;
import com.lib_common.observer.ActivityObserver;
import com.lib_common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseApp extends Application {
    protected String TAG = "BaseApp";
    protected List<ActivityObserver> acts = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        requestOnBack();
    }

    /**
     * 创建app是默认调用的方法
     */
    public void requestOnBack() {
    }

    public <T extends UserBean> T getUser(Class<T> t) {
        return getUserSP().getUserBean(t);
    }

//    /**
//     * 保存用户到缓存，异步
//     *
//     * @param userBean
//     */
//    protected void saveUserAsyn(final UserBean userBean) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                saveUserSync(userBean);
//            }
//        }).start();
//    }

    /**
     * 保存用户到缓存,同步
     *
     * @param userBean
     */
    protected void saveUserSync(UserBean userBean) {
        getUserSP().saveUserBean(userBean);
    }

    public UserSP getUserSP() {
        return new UserSP(this);
    }

    public void addActivity(ActivityObserver act) {
        acts.add(act);
        ToastUtil.log(TAG, "activity name = "
                + act.getClass().getCanonicalName());
    }

    public void removeActivity(ActivityObserver act) {
        acts.remove(act);
    }

    public void updateActivities() {
        for (ActivityObserver act : acts) {
            act.loadData();
        }
    }

    public void updateActivity(Class<? extends ActivityObserver> c) {
        for (ActivityObserver act : acts) {
            if (act.getClass().getCanonicalName().equals(c.getCanonicalName())) {
                act.loadData();
                break;
            }
        }
    }

    /**
     * 获取指定的activity对象
     *
     * @param c
     * @return 有可能为空
     */
    public ActivityObserver getActivity(Class<? extends ActivityObserver> c) {
        for (ActivityObserver act : acts) {
            if (act.getClass().getCanonicalName().equals(c.getCanonicalName())) {
                return act;
            }
        }
        return null;
    }
}
