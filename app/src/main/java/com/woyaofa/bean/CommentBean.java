package com.woyaofa.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by LoaR on 15/11/23.
 */
public class CommentBean implements Serializable {
    private Integer id;                //唯一ID
    private String content;            //评论内容
    private Integer score;            //评论分数
    private Date createTime;        //评论时间
    private Integer accountId;        //评论人ID
    private Integer lineId;            //评论线路ID
    private String imageIds;        //评论上传图片ID
    private Integer isDeleted;        //0表示未删除，1表示已删除
    private UserBean user;
    private List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public String getImageIds() {
        return imageIds;
    }

    public void setImageIds(String imageIds) {
        this.imageIds = imageIds;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
