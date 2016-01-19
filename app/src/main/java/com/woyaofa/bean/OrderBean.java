package com.woyaofa.bean;

import java.io.Serializable;

/**
 * Created by LoaR on 15/11/23.
 */
public class OrderBean implements Serializable {
    private Integer id;                //主键ID
    private String number;            //订单号
    private Integer receiver;        //收货人地址
    private Integer shipper;        //发货人地址
    private Integer accountId;        //下单账号
    private Integer lineId;            //受理的物流公司的某条线路ID
    private String detail;            //货物信息
    private Double fee;                //总运费
    private Double pickUpFee;        //上门取货运费
    private Double sendFee;            //上门送货费用
    private Integer status;            //0表示待受理，1表示代发货，2表示待收货,3表示待确认，4表示待评价，-1表示已完成
    private long buyTime;            //下单时间
    private long dueTime;            //预约时间
    private long dealTime;            //受理时间
    private long sendTime;            //发货时间
    private long arrivalTime;        //到达时间
    private long completeTime;        //确认时间
    private Integer isDeleted;        //0表示未删除，1表示已删除
    private LineBean line;        //0表示未删除，1表示已删除

    public LineBean getLine() {
        return line;
    }

    public void setLine(LineBean line) {
        this.line = line;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public Integer getShipper() {
        return shipper;
    }

    public void setShipper(Integer shipper) {
        this.shipper = shipper;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getPickUpFee() {
        return pickUpFee;
    }

    public void setPickUpFee(Double pickUpFee) {
        this.pickUpFee = pickUpFee;
    }

    public Double getSendFee() {
        return sendFee;
    }

    public void setSendFee(Double sendFee) {
        this.sendFee = sendFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(long buyTime) {
        this.buyTime = buyTime;
    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
    }

    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
