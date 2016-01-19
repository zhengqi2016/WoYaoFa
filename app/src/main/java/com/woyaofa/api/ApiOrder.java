package com.woyaofa.api;

import com.lib_common.activity.BaseActivity;
import com.lib_common.observer.ActivityObserver;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.ui.main.OrderFrg;

import java.util.List;

/**
 * Created by LoaR on 15/11/23.
 */
public class ApiOrder extends Api {

    /**
     * 订单状态(0表示待受理，1表示待发货，2表示待收货，3表示待确认，4表示待评价，-1表示已完成)
     */
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_DELIVERY = 1;
    public static final int STATUS_RECEIPT = 2;
    public static final int STATUS_CONFIRM = 3;
    public static final int STATUS_EVALUATE = 4;
    public static final int STATUS_COMPLETE = -1;

    public static final String URL_LIST = HOST + "orders/list";
    public static final String URL_ADD = HOST + "orders/add";
    public static final String URL_DELETE = HOST + "orders/delete";
    public static final String URL_CANCEL = HOST + "orders/cancel";
    public static final String URL_CONFIRM = HOST + "orders/comfirm";

    private static ApiOrder apiOrder;

    public static ApiOrder getInstance() {
        if (apiOrder == null) {
            apiOrder = new ApiOrder();
        }
        return apiOrder;
    }

    public void getList(ActivityObserver act, List<Object> body) {
        getRequest(act, URL_LIST, body);
    }

    public void postAdd(BaseActivity act, RequestBody body) {
        postRequest(act, URL_ADD, body);
    }

    public void postDelete(ActivityObserver act, RequestBody body) {
        postRequest(act, URL_DELETE, body);
    }

    public void postRemindDelivery(OrderFrg act, RequestBody body) {
//        postRequest(act, URL_DELETE, body);
    }

    public void postReminderProcessing(OrderFrg act, RequestBody body) {
//        postRequest(act, URL_DELETE, body);
    }

    public void getCancel(OrderFrg act, List<Object> body) {
        getRequest(act, URL_CANCEL, body);
    }

    public void getConfirmReceipt(OrderFrg act, List<Object> body) {
        getRequest(act, URL_CONFIRM, body);
    }

}
