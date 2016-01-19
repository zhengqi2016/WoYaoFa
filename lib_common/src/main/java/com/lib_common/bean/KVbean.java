package com.lib_common.bean;

import java.io.Serializable;

/**
 * Created by loar on 2015/8/28.
 */
public class KVbean implements Serializable {
    public String key;
    public String value;
    public String tag;
    /**
     * 任意对象
     */
    public Object obj;

    public KVbean() {
    }

    public KVbean(String key, String value, String tag, Object obj) {
        this.key = key;
        this.value = value;
        this.tag = tag;
        this.obj = obj;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
