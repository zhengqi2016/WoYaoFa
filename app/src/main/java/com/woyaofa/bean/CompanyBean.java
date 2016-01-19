package com.woyaofa.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LoaR on 15/11/23.
 */
public class CompanyBean implements Serializable {
    private Integer id;                    //唯一ID
    private String name;                //公司名称
    private String phone;                //联系方式
    private String logo;                //公司logo
    private String province;            //省
    private String city;                //市
    private String district;            //区
    private String street;                //街道
    private String address;                //详细地址'
    private String mark;
    private String lat;                    //纬度
    private String lng;                    //经度
    private String imageIds;            //公司展示图ID
    private Integer cAIC;                //工商认证标志,0表示未认证，1表示已经认证
    private String cAICImageIds;        //工商认证图片ID
    private Integer qC;                    //资质认证，0表示未认证，1表示已认证
    private String qCImageId;            //资质认证图片ID
    private Double registeredCapital;    //注册资金
    private Integer carNum;                //卡车数量
    private Integer staffNum;            //员工人数
    private Integer areaCovered;        //占地面积
    private Integer accountId;            //账号ID
    private String summary;
    private Integer isDeleted;            //
    private List<LineBean> lines;
    private int status;
    private String tel;
    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<LineBean> getLines() {
        return lines;
    }

    public void setLines(List<LineBean> lines) {
        this.lines = lines;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Double getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(Double registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public Integer getCarNum() {
        return carNum;
    }

    public void setCarNum(Integer carNum) {
        this.carNum = carNum;
    }

    public Integer getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(Integer staffNum) {
        this.staffNum = staffNum;
    }

    public Integer getAreaCovered() {
        return areaCovered;
    }

    public void setAreaCovered(Integer areaCovered) {
        this.areaCovered = areaCovered;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImageIds() {
        return imageIds;
    }

    public void setImageIds(String imageIds) {
        this.imageIds = imageIds;
    }

    public Integer getcAIC() {
        return cAIC;
    }

    public void setcAIC(Integer cAIC) {
        this.cAIC = cAIC;
    }

    public String getcAICImageIds() {
        return cAICImageIds;
    }

    public void setcAICImageIds(String cAICImageIds) {
        this.cAICImageIds = cAICImageIds;
    }

    public Integer getqC() {
        return qC;
    }

    public void setqC(Integer qC) {
        this.qC = qC;
    }

    public String getqCImageId() {
        return qCImageId;
    }

    public void setqCImageId(String qCImageId) {
        this.qCImageId = qCImageId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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
