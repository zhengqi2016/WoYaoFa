package com.woyaofa.bean;

import java.io.Serializable;

/**
 * Created by LoaR on 15/11/23.
 */
public class ImageBean implements Serializable {
    private Integer id;                //主键ID
    private String url;                //图片存储路径
    private String host;            //主机
    private Integer isDeleted;        //0表示未删除，1表示已删除

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
