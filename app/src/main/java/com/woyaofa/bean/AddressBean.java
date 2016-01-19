package com.woyaofa.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LoaR on 15/11/23.
 */
public class AddressBean implements Serializable {
    private int id;                    //主键
    private String name;                //收/发货人姓名
    private String phone;                //联系方式
    private String province;            //省
    private String city;                //市
    private String district;            //区
    private String street;                //街道
    private String detail;                //详细地址
    private Integer type;                //地址类型（0表示收货人，1表示发货人）
    private Integer accountId;            //账号ID
    private Date createTime;            //地址簿创建时间
    private Integer isDeleted;            //0表示未删除，1表示删除

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
