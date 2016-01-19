package com.woyaofa.bean;

import java.io.Serializable;

/**
 * Created by LoaR on 15/11/23.
 */
public class MessageBean implements Serializable {
    private Integer id;                        //唯一ID
    private String content;
    private String message;
    private String title;
    private long createTime;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}
