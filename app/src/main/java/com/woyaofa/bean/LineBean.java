package com.woyaofa.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LoaR on 15/11/23.
 */
public class LineBean implements Serializable {
    private int id;                        //唯一ID
    private String beginProvince;            //始发地省
    private String beginCity;                //始发地城市
    private String beginDistrict;            //始发地区
    private String beginStreet;                //始发地街道
    private String beginAddress;            //始发地详细地址
    private String endProvince;                //目的地省
    private String endCity;                    //目的地城市
    private String endDistrict;                //目的地区
    private String endStreet;                //目的地街道
    private String endAddress;                //目的地详细地址
    private double minPrice;                //运送最低价格
    private double maxPrice;                //运送最高价格
    private int minDay;                    //运送最早到达时间
    private int maxDay;                    //运送最晚到达时间
    private int volume;                    //成交量
    private String phone;                    //联系电话
    private int commentNum;                //总评论数量
    private float commentTotalScore;        //总评论分数
    private Date createTime;                //线路创建时间
    private int accountId;                //线路所属公司的账号ID
    private int isDeleted;                //0表示未删除，1表示删除
    private String mark;                     //地名关键字
    private int status;                    //状态：0，创建中；1，已保存；2，待审核；3，已发布；4，审核未通过；
    private CompanyBean company;
    private double lightMaxPrice;
    private double lightMinPrice;

    public double getLightMaxPrice() {
        return lightMaxPrice;
    }

    public void setLightMaxPrice(double lightMaxPrice) {
        this.lightMaxPrice = lightMaxPrice;
    }

    public double getLightMinPrice() {
        return lightMinPrice;
    }

    public void setLightMinPrice(double lightMinPrice) {
        this.lightMinPrice = lightMinPrice;
    }

    public CompanyBean getCompany() {
        return company;
    }

    public void setCompany(CompanyBean company) {
        this.company = company;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeginProvince() {
        return beginProvince;
    }

    public void setBeginProvince(String beginProvince) {
        this.beginProvince = beginProvince;
    }

    public String getBeginCity() {
        return beginCity;
    }

    public void setBeginCity(String beginCity) {
        this.beginCity = beginCity;
    }

    public String getBeginDistrict() {
        return beginDistrict;
    }

    public void setBeginDistrict(String beginDistrict) {
        this.beginDistrict = beginDistrict;
    }

    public String getBeginStreet() {
        return beginStreet;
    }

    public void setBeginStreet(String beginStreet) {
        this.beginStreet = beginStreet;
    }

    public String getBeginAddress() {
        return beginAddress;
    }

    public void setBeginAddress(String beginAddress) {
        this.beginAddress = beginAddress;
    }

    public String getEndProvince() {
        return endProvince;
    }

    public void setEndProvince(String endProvince) {
        this.endProvince = endProvince;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getEndDistrict() {
        return endDistrict;
    }

    public void setEndDistrict(String endDistrict) {
        this.endDistrict = endDistrict;
    }

    public String getEndStreet() {
        return endStreet;
    }

    public void setEndStreet(String endStreet) {
        this.endStreet = endStreet;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinDay() {
        return minDay;
    }

    public void setMinDay(int minDay) {
        this.minDay = minDay;
    }

    public int getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(int maxDay) {
        this.maxDay = maxDay;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public float getCommentTotalScore() {
        return commentTotalScore;
    }

    public void setCommentTotalScore(float commentTotalScore) {
        this.commentTotalScore = commentTotalScore;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
