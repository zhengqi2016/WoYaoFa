package com.woyaofa.bean;

import java.io.Serializable;

/**
 * Created by LoaR on 15/11/10.
 */
public class UserBean implements Serializable {
    private Integer id;                    //唯一ID
    private String logo;                //用户头像
    private String name;                //用户昵称
    private Integer sex;                //用户性别:0表示男,1表示女
    private long birthday;                //生日
    private String phone;                //用户联系方式
    private int credit;                //用户积分
    private int defaultAddress;        //默认发货地址
    private Integer accountId;            //账号ID
    private Integer isDeleted;            //0表示未删除，1表示删除
    private AddressBean addressBook;

    public int getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(int defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public AddressBean getAddressBook() {
        return addressBook;
    }

    public void setAddressBook(AddressBean addressBook) {
        this.addressBook = addressBook;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
